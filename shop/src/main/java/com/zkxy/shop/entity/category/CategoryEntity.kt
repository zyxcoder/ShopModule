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
data class GoodsCategoryEntity(
    val children: List<GoodsCategoryEntity>?,
    val createTime: String?,
    val deleteFlag: Int?,
    val levelType: Int?,
    val name: String?,
    val parentId: Int?,
    val remark: String?,
    val sortNumber: Int?,
    val typeId: Int?,
    val updateTime: String?,
    var isSelect: Boolean?
) : Parcelable