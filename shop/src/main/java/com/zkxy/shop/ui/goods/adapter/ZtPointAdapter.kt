package com.zkxy.shop.ui.goods.adapter

import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemZtPointBinding
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder

class ZtPointAdapter : BaseViewBindingAdapter<String, ItemZtPointBinding>(
    ItemZtPointBinding::inflate,
    R.layout.item_zt_point
) {
    override fun convert(holder: BaseViewBindingHolder<ItemZtPointBinding>, item: String) {
    }
}