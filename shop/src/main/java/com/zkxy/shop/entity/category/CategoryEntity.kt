package com.zkxy.shop.entity.category

import androidx.annotation.Keep

/**
 * @author zhangyuxiang
 * @date 2024/9/24
 */
@Keep
data class CategoryEntity(
    val categoryId: Int?,
    val categoryName: String?,
    var isSelect: Boolean?,
    val categorySecondaryList: MutableList<CategorySecondaryEntity>?
)

@Keep
data class CategorySecondaryEntity(
    val categoryId: Int?,
    val categoryName: String?,
    val categoryMinorList: MutableList<CategoryMinorEntity>?
)

@Keep
data class CategoryMinorEntity(
    val categoryId: Int?, val categoryName: String?
)