package com.zkxy.shop.entity.home

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * @author zhangyuxiang
 * @date 2024/9/19
 */
@Keep
@Parcelize
data class GoodsEntity(
    val goodsId: Int?,
    val goodsUrl: String?,
    val goodsName: String?,
    val goodsPrice: Double?,
    val goodsPoint: Double?,
    val goodsInventory: Int?
) : Parcelable