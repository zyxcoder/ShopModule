package com.zkxy.shop.network.api

import com.gxy.common.network.api.ApiResult
import com.zkxy.shop.entity.category.GoodsCategoryEntity
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    companion object {
        const val RESPONSE_CODE_SUCCESS = "0"

        //一般我们成功的状态码返回是200，如果遇到特殊的成功状态码，需要在这里补充
        val SPECIAL_API_SUCCESS_STATUS_CODE = mapOf<String, String>()
    }


    @POST("app/mall/goods/goodsType")
    @FormUrlEncoded
    suspend fun getGoodsCategory(
        @Field("platformId") platformId: Int?,
        @Field("currentPage") currentPage:Long?,
        @Field("pageSize") pageSize:Int?
    ): ApiResult<Any>

}