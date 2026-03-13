package com.zkxy.shop.entity.order

data class ConfirmAddressEntity(
    var isCheck: Boolean = false,
    val tel: String?,
    val pickName: String?,
    val shipmentsAddressId: Int?
)