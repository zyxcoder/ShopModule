package com.gxy.common.network.api

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.zyxcoder.mvvmroot.network.ApiException

/**
 * @author zhangyuxiang
 * @date 2024/2/29
 */
@Keep
data class ApiResult<T>(
    @SerializedName("code")
    var statusCode: String?,
    @SerializedName("desc")
    val statusDesc: String?,
    @SerializedName("hasMore")
    val hasMore: Boolean?,
    @SerializedName("data")
    private val data: T?,
    @SerializedName("recordsTotal")
    val listCount: Int?
) {
    fun apiData(): T {
        if (data != null && statusCode == CommonApiService.RESPONSE_CODE_SUCCESS) {
            return data
        } else {
            throw ApiException(statusCode?.toIntOrNull() ?: -1, statusDesc ?: "")
        }
    }

    fun apiNoData(): T? {
        if (statusCode == CommonApiService.RESPONSE_CODE_SUCCESS) {
            return data
        } else {
            throw ApiException(statusCode?.toIntOrNull() ?: -1, statusDesc ?: "")
        }
    }

    //用于ocr识别
    fun apiOcrData(): T {
        if (data != null && statusCode == CommonApiService.RESPONSE_CODE_SUCCESS) {
            return data
        } else if (data != null && statusCode == "-700") {
            return data
        } else {
            throw ApiException(statusCode?.toIntOrNull() ?: -1, statusDesc ?: "")
        }
    }
}

@Keep
data class ApiErrotResult(
    @SerializedName("code") var statusCode: String?,
    @SerializedName("desc") val statusDesc: String?
) {
    fun apiErrorData() {
        if (statusCode != CommonApiService.RESPONSE_CODE_SUCCESS) {
            throw ApiException(statusCode?.toIntOrNull() ?: -1, statusDesc ?: "")
        }
    }
}