package com.zkxy.shop.entity.goods

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class GoodsDetailsEntity(
    val bannerPicDtoList: MutableList<PicDto>?,
    val buyEmption: Int?,
    val deliveryMode: Int?,
    val goodsDetailPicDtoList: MutableList<PicDto>?,
    val goodsDetails: String?,
    val goodsMoneyPrice: Double?,
    val goodsName: String?,
    val goodsScorePrice: Int?
) : Serializable

@Keep
data class PicDto(
    val createTime: String?,
    val deleteFlag: Int?,
    val goodsId: Int?,
    val picHeight: String?,
    val picId: Int?,
    val picUrl: String?,
    val picWidth: String?,
    val updateTime: String?
) : Serializable
