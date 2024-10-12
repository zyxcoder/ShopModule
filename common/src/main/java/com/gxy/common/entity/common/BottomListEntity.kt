package com.gxy.common.entity.common

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * @author zhangyuxiang
 * @date 2024/3/11
 */
@Keep
@Parcelize
data class BottomListEntity(
    val id: Int?,
    val name: String?,
    var isCheck: Boolean = false,
    var isCanClick: Boolean = true,
    var data: @RawValue Any? = null
) : Parcelable