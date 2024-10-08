package com.zkxy.shop.ui.goods.adapter

import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemZtPointBinding
import com.zkxy.shop.entity.goods.Address
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder

class ZtPointAdapter : BaseViewBindingAdapter<Address, ItemZtPointBinding>(
    ItemZtPointBinding::inflate,
    R.layout.item_zt_point
) {

    init {
        addChildClickViewIds(R.id.tvDialing, R.id.tvNavigation)
    }

    override fun convert(holder: BaseViewBindingHolder<ItemZtPointBinding>, item: Address) {
        holder.viewBind.apply {
            tvTitle.text = item.pickName
            tvAddress.text = item.pickDetailAddress
            tvDistance.text = "${item.distance}km"
        }
    }
}