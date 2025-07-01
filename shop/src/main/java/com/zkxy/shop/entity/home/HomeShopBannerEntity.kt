package com.zkxy.shop.entity.home

import androidx.annotation.Keep

/**
 * @author zhangyuxiang
 * @date 2024/9/19
 */
@Keep
data class HomeShopBannerEntity(
    val advertisingUrl: String?,
    val createBy: String?,
    val createTime: String?,
    val id: Int?,
    val imageTitle: String?,
    val imageUrl: String?,
    val length: Int?,
    val license: String?,
    val module: String?,
    val sortOrder: Int?,
    val start: Int?,
    val status: Int?,
    val sysId: Int?,
    val type: Int
)