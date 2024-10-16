package com.zkxy.shop.entity.order

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
    val statusId: Int?,
    val statusName: String?,
    val totalPoints: Int?,
    val totalPrice: Double?,
    val updateTime: String?
)
