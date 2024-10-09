package com.zkxy.shop.entity.goods

data class PlaceOrderEntity(
    val addressList: MutableList<Address>?,
    val goodsSpecDtoList: MutableList<GoodsSpecDto>?
)

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