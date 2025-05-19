package com.zkxy.shop.entity.order

import androidx.annotation.Keep

@Keep
data class OrderListEntity(
    val title: String?,
    val consignee: String?,
    val consigneeTel: String?,
    val createTime: String?,
    val deleteFlag: Int?,
    val deliveryAddress: String?,
    val deliveryType: Int?,
    val deliveryTypeName: String?,
    val goodsId: Int?,
    val goodsName: String?,
    val goodsNum: Int?,
    val goodsSource: String?,
    val paymentAmount: String?,
    val goodsSpecId: Int?,
    val orderCode: String?,
    val goodsImg: String?,
    val orderId: Int?,
    val orderPlacer: String?,
    val orderPlacerTel: String?,
    val platformId: Int?,
    val platformName: String?,
    val platformPrice: String?,
    val deliveryCode: String?,
    val logisticsCompany: String?,
    val expressNumber: String?,
    val shippingTime: String?,
    val orderDesc: String?,
    val cancelTime: String?,
    val shipmentsAddress: String?,
    val dTime: String?,
    val dAddress: String?,
    val deliveryTime: String?,
    val points: Int?,
    val price: Double?,
    val priceRatio: Double?,
    var statusId: Int?,
    val statusName: String?,
    val totalPoints: Int?,
    val payWay: Int?,
    val scorePayFlag: Int?,
    val totalPrice: Double?,
    val updateTime: String?,
    val prepayParams: PrepayParams?
)

@Keep
data class PrepayParams(
    val appId: String?,
    val nonceStr: String?,
    val packageValue: String?,
    val partnerId: String?,
    val prepayId: String?,
    val sign: String?,
    val timeStamp: String?
)