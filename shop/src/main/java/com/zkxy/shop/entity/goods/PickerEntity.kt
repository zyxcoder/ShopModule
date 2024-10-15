package com.zkxy.shop.entity.goods

import androidx.annotation.Keep

@Keep
data class PickerEntity(
    val options1Items: List<Any>?,
    val options2Items: List<MutableList<String>>?,
    val options3Items: ArrayList<ArrayList<ArrayList<String>>>?
)