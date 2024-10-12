package com.zkxy.shop.entity.order

import androidx.annotation.Keep

@Keep
data class OrderDetailsEntity(
    val consignee: String?,
    val consigneeTel: String?,
    val consigner: String?,
    val consignerTel: String?,
    val createTime: String?,
    val deleteFlag: Int?,
    val deliveryAddress: String?,
    val deliveryType: Int?,
    val expressNumber: String?,
    val goodsId: Int?,
    val goodsName: String?,
    val goodsNum: Int?,
    val goodsSource: String?,
    val goodsSpecId: Int?,
    val goodsSpecName: String?,
    val logisticsCompany: String?,
    val orderCode: String?,
    val goodsImg: String?,
    val orderId: Int?,
    val orderPlacer: String?,
    val orderPlacerTel: String?,
    val paymentAmount: String?,
    val platformId: Int?,
    val platformName: String?,
    val platformPrice: String?,
    val deliveryCode: String?,
    val points: Int?,
    val priceRatio: Double?,
    val refundProgress: Int?,
    val refundTime: String?,
    val shipmentsAddress: String?,
    val shippingFee: Double?,
    val shippingTime: String?,
    val statusId: Int?,
    val statusName: String?,
    val totalPoints: Int?
)