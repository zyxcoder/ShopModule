package com.zkxy.shop.entity.category

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * @author zhangyuxiang
 * @date 2024/9/24
 */
@Keep
@Parcelize
data class CategoryEntity(
    val categoryId: Int?,
    val categoryName: String?,
    var isSelect: Boolean?,
    val categorySecondaryList: MutableList<CategorySecondaryEntity>?
) : Parcelable

@Keep
@Parcelize
data class CategorySecondaryEntity(
    val categoryId: Int?,
    val categoryName: String?,
    val categoryMinorList: MutableList<CategoryMinorEntity>?
) : Parcelable

@Keep
@Parcelize
data class CategoryMinorEntity(
    val categoryId: Int?, val categoryName: String?, var isSelect: Boolean? = false
) : Parcelable