package com.zkxy.shop.ui.goods.adapter

import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemShopSelectSpecificationBinding
import com.zkxy.shop.entity.goods.GoodsSpecDto
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder

class SpecificationAdapter :
    BaseViewBindingAdapter<GoodsSpecDto, ItemShopSelectSpecificationBinding>(
        ItemShopSelectSpecificationBinding::inflate,
        R.layout.item_shop_select_specification
    ) {
    override fun convert(
        holder: BaseViewBindingHolder<ItemShopSelectSpecificationBinding>,
        item: GoodsSpecDto
    ) {
        holder.viewBind.apply {
            tvSpecName.text = item.specName
            tvCurrentStock.text = "剩余库存${item.currentStock}"
        }
    }
}