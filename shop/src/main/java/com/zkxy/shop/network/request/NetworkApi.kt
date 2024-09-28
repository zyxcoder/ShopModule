package com.zkxy.shop.network.request

import android.os.SystemClock
import android.util.Log
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import com.gxy.common.BuildConfig
import com.zkxy.shop.modeBaseUrl
import com.zkxy.shop.network.api.ApiService
import com.zkxy.shop.network.api.ApiService.Companion.RESPONSE_CODE_SUCCESS
import com.zkxy.shop.network.api.ApiService.Companion.SPECIAL_API_SUCCESS_STATUS_CODE
import com.zkxy.shop.network.api.GxyApiErrotResult
import com.zkxy.shop.network.api.GxyApiResult
import com.zkxy.shop.network.deserializer.NumberDeserializer
import com.zkxy.shop.network.deserializer.StringDeserializer
import com.zyxcoder.mvvmroot.base.appContext
import com.zyxcoder.mvvmroot.network.BaseNetworkApi
import com.zyxcoder.mvvmroot.utils.logd
import com.zyxcoder.mvvmroot.utils.loge
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
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
            //示例：添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
            addInterceptor {
                it.request().toString().loge("REQUEST_BODY")
                val startRequestTime = SystemClock.elapsedRealtime()
                val response = it.proceed(it.request().newBuilder().build())
                val responseBody = response.body?.string()
                response.body
                val apiRequestAddress = it.request().url.toString().removePrefix(modeBaseUrl ?: "")
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