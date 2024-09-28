package com.zkxy.shop.network.api

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.gxy.common.network.api.CommonApiService
import com.zkxy.shop.network.api.ApiService.Companion.RESPONSE_CODE_SUCCESS
import com.zyxcoder.mvvmroot.network.ApiException

/**
 * @author zhangyuxiang
 * @date 2024/6/19
 */
@Keep
data class GxyApiResult<T>(
    @SerializedName("code")
    var statusCode: String?,
    @SerializedName("desc")
    val statusDesc: String?,
    @SerializedName("hasMore")
    val hasMore: Boolean?,
    @SerializedName("data")
    private val data: T?,
    @SerializedName("recordsTotal")
    val listCount: Int?,
    @SerializedName("recordsFiltered")
    val recordsFiltered: Int?
) {
    fun apiData(): T {
        if (data != null && statusCode == RESPONSE_CODE_SUCCESS) {
            return data
        } else {
            throw ApiException(statusCode?.toIntOrNull() ?: -1, statusDesc ?: "")
        }
    }

    fun apiNoData(): T? {
        if (statusCode == RESPONSE_CODE_SUCCESS) {
            return data
        } else {
            throw ApiException(statusCode?.toIntOrNull() ?: -1, statusDesc ?: "")
        }
    }
}

@Keep
data class GxyApiErrotResult(
    @SerializedName("code") var statusCode: String?,
    @SerializedName("desc") val statusDesc: String?
) {
    fun apiErrorData() {
        if (statusCode != CommonApiService.RESPONSE_CODE_SUCCESS) {
            throw ApiException(statusCode?.toIntOrNull() ?: -1, statusDesc ?: "")
        }
    }
}