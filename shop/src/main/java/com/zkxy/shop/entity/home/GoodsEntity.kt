package com.zkxy.shop.entity.home

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author zhangyuxiang
 * @date 2024/9/19
 */
@Keep
@Parcelize
data class GoodsEntity(
    @SerializedName("goodsId") val goodsId: Int?,
    @SerializedName("shopTypeId") val shopTypeId: Int?,
    @SerializedName("shopFace") val goodsUrl: String?,
    @SerializedName("goodsSource") val goodsSource: Int?,
    @SerializedName("goodsCost") val goodsCost: Double?,
    @SerializedName("goodsScorePrice") val goodsPoint: Double?,
    @SerializedName("goodsMoneyPrice") val goodsPrice: Double?,
    @SerializedName("buyEmption") val buyEmption: Int?,
    @SerializedName("goodsStatus") val goodsStatus: Int?,
    @SerializedName("goodsSuggest") val goodsSuggest: Int?,
    @SerializedName("deliveryMode") val deliveryMode: Int?,
    @SerializedName("deleteFlag") val deleteFlag: Int?,
    @SerializedName("goodsDetails") val goodsDetails: String?,
    @SerializedName("goodsName") val goodsName: String?,
    @SerializedName("sortIndex") val sortIndex: Int?,
    @SerializedName("createTime") val createTime: String?,
    @SerializedName("updateTime") val updateTime: String?,
    @SerializedName("goodsTotalStock") val goodsTotalStock: Int?,
    @SerializedName("goodsCurrentStock") val goodsCurrentStock: Int?,
    @SerializedName("goodsLossStock") val goodsLossStock: Int?,
    @SerializedName("goodsUseStock") val goodsUseStock: Int?,
    @SerializedName("priceType") val priceType: Int?,
    @SerializedName("userId") val userId: Int?,
    @SerializedName("userName") val userName: String?,
    @SerializedName("shopFaceWidth") val shopFaceWidth: String?,
    @SerializedName("shopFaceHeight") val shopFaceHeight: String?,
    @SerializedName("platformId") val platformId: Int?
) : Parcelable