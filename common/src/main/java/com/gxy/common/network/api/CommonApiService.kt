package com.gxy.common.network.api

import com.gxy.common.entity.common.BottomCommonOption
import com.gxy.common.entity.common.BottomRequestEntity
import retrofit2.http.Body
import retrofit2.http.POST


/**
 * @author zhangyuxiang
 * @date 2024/2/27
 */
interface CommonApiService {

    companion object {
        const val RESPONSE_CODE_SUCCESS = "0"

        //一般我们成功的状态码返回是200，如果遇到特殊的成功状态码，需要在这里补充
        val SPECIAL_API_SUCCESS_STATUS_CODE = mapOf<String, String>()
    }

    /**
     * 获取表单下拉框数据
     */
    @POST("v2/driver/select/listSearch")
    suspend fun getChooseList(
        @Body bottomRequestEntity: BottomRequestEntity?
    ):ApiResult<BottomCommonOption>
}