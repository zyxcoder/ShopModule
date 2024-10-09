package com.zkxy.shop.entity.home

import androidx.annotation.Keep

/**
 * @author zhangyuxiang
 * @date 2024/9/19
 */
@Keep
data class HomeShopBannerEntity(
    val createTime: String?,
    val deleteFlag: Int?,
    val endTime: String?,
    val goodsId: Int?,
    val noticeDesc: String?,
    val noticeId: Int?,
    val noticeName: String?,
    val noticeUrl: String?,
    val platformId: Int?,
    val sort: Int?,
    val status: Int?
)