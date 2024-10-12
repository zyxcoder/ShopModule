package com.gxy.common.entity.common

import androidx.annotation.Keep

/**
 * @author zhangyuxiang
 * @date 2024/4/24
 */
@Keep
data class SearchVoList(
    val formId: Int?,
    val inputName: String?,
    val inputValue: String? = null,
    var length: Int? = 30,
    val start: Int? = 0
)

@Keep
data class BottomRequestEntity(
    val searchVoList: List<SearchVoList>?
)