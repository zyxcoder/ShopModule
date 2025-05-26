package com.zkxy.shop.entity.goods

import androidx.annotation.Keep

@Keep
data class RateEntity(
    val oil: MutableList<Balance>?,
    val shippingFee: MutableList<Balance>?
)

@Keep
data class Balance(
    val balance: Double?,
    val name: String?,
    val taxRate: Int?,
    var isCheck: Boolean = false
)
