package com.zkxy.shop.entity.goods

data class PickerEntity(
    val options1Items: List<Any>?,
    val options2Items: List<MutableList<String>>?,
    val options3Items: ArrayList<ArrayList<ArrayList<String>>>?
)