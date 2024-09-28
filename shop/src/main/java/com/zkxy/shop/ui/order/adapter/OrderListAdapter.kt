package com.zkxy.shop.ui.order.adapter

import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemOrderListBinding
import com.zkxy.shop.entity.order.OrderListEntity
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder

class OrderListAdapter : BaseViewBindingAdapter<OrderListEntity, ItemOrderListBinding>(
    ItemOrderListBinding::inflate,
    R.layout.item_order_list
) {
    override fun convert(
        holder: BaseViewBindingHolder<ItemOrderListBinding>,
        item: OrderListEntity
    ) {
    }
}