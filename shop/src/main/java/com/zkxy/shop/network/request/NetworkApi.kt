package com.zkxy.shop.network.request

import android.os.SystemClock
import android.util.Log
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.gxy.common.BuildConfig
import com.zkxy.shop.ext.isDouble
import com.zkxy.shop.ext.isInt
import com.zkxy.shop.ext.isLong
import com.zkxy.shop.modeBaseUrl
import com.zkxy.shop.network.api.ApiService
import com.zkxy.shop.network.api.ApiService.Companion.RESPONSE_CODE_SUCCESS
import com.zkxy.shop.network.api.ApiService.Companion.SPECIAL_API_SUCCESS_STATUS_CODE
import com.zkxy.shop.network.api.GxyApiErrotResult
import com.zkxy.shop.network.api.GxyApiResult
import com.zkxy.shop.network.deserializer.NumberDeserializer
import com.zkxy.shop.network.deserializer.StringDeserializer
import com.zkxy.shop.utils.AesUtils
import com.zkxy.shop.utils.RSAUtil
import com.zkxy.shop.utils.RandomKey
import com.zyxcoder.mvvmroot.base.appContext
import com.zyxcoder.mvvmroot.network.BaseNetworkApi
import com.zyxcoder.mvvmroot.utils.logd
import com.zyxcoder.mvvmroot.utils.loge
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author zhangyuxiang
 * @date 2024/2/27
 */

//双重校验锁式-单例 封装NetApiService 方便直接快速调用简单的接口
val apiService: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.INSTANCE.getApi(ApiService::class.java, modeBaseUrl ?: "")
}

class NetworkApi : BaseNetworkApi() {
    private val cookieJar: PersistentCookieJar by lazy {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(appContext))
    }

    //json解析器
    private val gsonParser by lazy {
        GsonBuilder().registerTypeAdapter(String::class.java, StringDeserializer())
            .registerTypeAdapter(Number::class.java, NumberDeserializer()).create()
    }

    companion object {
        val INSTANCE: NetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkApi()
        }
    }

    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return builder.apply {
            cookieJar(cookieJar)
            addInterceptor { chain ->
                val originalRequest = chain.request()
                //生成随机key，放在tag里，传递到后续拦截器
                val randomKey = RandomKey.generateRandomKey()
                val requestWithTag = originalRequest.newBuilder().tag(
                    String::class.java, randomKey
                ).build()
                // 检查是否是POST请求以及请求体是否是FormBody
                if (requestWithTag.method == "POST" && requestWithTag.body is FormBody) {
                    val formBody = requestWithTag.body as FormBody
                    val jsonMap = mutableMapOf<String, Any>()
                    // 转换 FormBody 数据为 JSON
                    for (i in 0 until formBody.size) {
                        val key = formBody.name(i)
                        val value = formBody.value(i)
                        val typedValue = when {
                            value.isLong() -> value.toLong()
                            value.isInt() -> value.toInt()
                            value.isDouble() -> value.toDouble()
                            else -> value // 默认情况使用字符串
                        }
                        jsonMap[key] = typedValue
                    }
                    val aesKey = RSAUtil.encryptByPublicKey(randomKey)
                    val content = AesUtils.encryptData(gsonParser.toJson(jsonMap), randomKey)
                    val requestJson = mapOf("aesKey" to aesKey, "content" to content)
                    // 创建新的 JSON 请求体
                    val requestBody = gsonParser.toJson(requestJson)
                        .toRequestBody("application/json".toMediaType())
                    // 创建新的请求
                    val newRequest =
                        requestWithTag.newBuilder().method(requestWithTag.method, requestBody)
                            .addHeader("Content-Type", "application/json") // 设置 Content-Type
                            .build()
                    // 继续执行请求
                    return@addInterceptor chain.proceed(newRequest)
                }
                // 如果不是需要转换的请求，直接进行
                chain.proceed(requestWithTag)
            }
            //示例：添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
            addInterceptor {
                val originalRequest = it.request()
                // 从请求中获取 randomKey
                val randomKey = originalRequest.tag(String::class.java)
                originalRequest.toString().loge("REQUEST_BODY")
                val startRequestTime = SystemClock.elapsedRealtime()
                val response = it.proceed(originalRequest.newBuilder().build())
                var responseBody = response.body?.string()
                response.body
                val apiRequestAddress =
                    originalRequest.url.toString().removePrefix(modeBaseUrl ?: "")
                //请求耗时,毫秒单位
                val requestUseTime = SystemClock.elapsedRealtime() - startRequestTime
                if (response.code != 200) {
                    //服务器错误
                    val errorResponse = response.newBuilder().body(
                        try {
                            gsonParser.toJson(
                                gsonParser.fromJson(
                                    responseBody, GxyApiErrotResult::class.java
                                )
                            )
                        } catch (e: Exception) {
                            e.message ?: ""
                        }.toResponseBody("text/plain".toMediaType())
                    ).build()
                    return@addInterceptor errorResponse
                }
                val originApiResult: GxyApiResult<String>? = try {
                    val type = object : TypeToken<GxyApiResult<String>>() {}.type
                    gsonParser.fromJson<GxyApiResult<String>>(responseBody, type)
                        ?: throw IllegalArgumentException()
                } catch (e: Exception) {
                    //响应体非标准json格式
                    val errorResponse = response.newBuilder().body(
                        try {
                            gsonParser.toJson(
                                gsonParser.fromJson(
                                    responseBody, GxyApiErrotResult::class.java
                                )
                            )
                        } catch (e: Exception) {
                            e.message ?: ""
                        }.toResponseBody("text/plain".toMediaType())
                    ).build()
                    return@addInterceptor errorResponse
                }
                if (originApiResult != null) {
                    //解密后的data数据，此时只是一个string
                    val decryptJsonData =
                        AesUtils.decryptData(originApiResult.apiOnlyData(), randomKey)
                    Log.d("解密的数据", decryptJsonData ?: "")
                    responseBody = responseBody?.let { result ->
                        JSONObject(result).apply {
                            //这步相当于替换掉responseBody中的data
                            put("data", decryptJsonData)
                        }.toString()
                    }
                }

                val apiResult: GxyApiResult<*>?
                try {
                    apiResult = gsonParser.fromJson(responseBody, GxyApiResult::class.java)
                        ?: throw IllegalArgumentException()
                } catch (e: Exception) {
                    //响应体非标准json格式
                    val errorResponse = response.newBuilder().body(
                        try {
                            gsonParser.toJson(
                                gsonParser.fromJson(
                                    responseBody, GxyApiErrotResult::class.java
                                )
                            )
                        } catch (e: Exception) {
                            e.message ?: ""
                        }.toResponseBody("text/plain".toMediaType())
                    ).build()
                    return@addInterceptor errorResponse
                }
                //状态码转换
                val mapSuccessCode = SPECIAL_API_SUCCESS_STATUS_CODE[apiRequestAddress]
                if (!mapSuccessCode.isNullOrEmpty()) {
                    if (apiResult.statusCode == mapSuccessCode) {
                        apiResult.statusCode = RESPONSE_CODE_SUCCESS
                    }
                } else {
                    //对于列表，后端返回的成功数据里面不包含code，所以此处作判空处理，空code或code=1代表成功
                    if (apiResult.statusCode == "1" || apiResult.statusCode.isNullOrEmpty()) {
                        apiResult.statusCode = RESPONSE_CODE_SUCCESS
                    }
                }
                val modifyBody = try {
                    gsonParser.toJson(apiResult)
                } catch (e: java.lang.NullPointerException) {
                    e.message ?: ""
                }.toResponseBody("text/plain".toMediaType())
                "接口：${apiRequestAddress}   耗时：${requestUseTime}".logd("HttpLoggingInterceptor")
                "------------------------------------------------------------------\t\t\t".logd("HttpLoggingInterceptor")
                response.newBuilder().body(modifyBody).build()
            }
            // 日志拦截器
            addInterceptor(HttpLoggingInterceptor { message ->
                Log.d("HttpLoggingInterceptor", message)
            }.apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            //超时时间 连接、读、写
            connectTimeout(20, TimeUnit.SECONDS)
            readTimeout(20, TimeUnit.SECONDS)
            writeTimeout(20, TimeUnit.SECONDS)
        }
    }

    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder =
        builder.addConverterFactory(GsonConverterFactory.create())
}