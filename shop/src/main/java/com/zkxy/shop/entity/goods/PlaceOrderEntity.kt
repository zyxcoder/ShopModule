package com.zkxy.shop.entity.goods

import androidx.annotation.Keep

@Keep
data class PlaceOrderEntity(
    val addressList: MutableList<Address>?,
    val goodsSpecDtoList: MutableList<GoodsSpecDto>?
)

@Keep
data class Address(
    val createTime: String?,
    val deleteFlag: Int?,
    val pickAddressTel: String?,
    val pickDetailAddress: String?,
    val pickName: String?,
    val distance: String?,
    val loadLat: String?,
    val loadLon: String?,
    val supplierPickId: Int?,
    val updateTime: String?
)

@Keep
data class GoodsSpecDto(
    val createTime: String?,
    val currentStock: Int?,
    val deleteFlag: Int?,
    val goodsId: Int?,
    val goodsSpecId: Int?,
    val lossStock: Int?,
    val specName: String?,
    val totalStock: Int?,
    val updateTime: String?,
    val useStock: Int?
)

@Keep
data class OrderEntity(
    val orderId: Int?,
    val desc: String?,
    val appId: String?,
    val nonceStr: String?,
    val packageValue: String?,
    val partnerId: String?,
    val prepayId: String?,
    val sign: String?,
    val timeStamp: String?
)