package com.zkxy.shop.ui.order.adapter

import androidx.core.graphics.toColorInt
import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemTaxBinding
import com.zkxy.shop.entity.goods.Balance
import com.zkxy.shop.ext.formatAmount
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder

class SelectTaxAdapter :
    BaseViewBindingAdapter<Balance, ItemTaxBinding>(ItemTaxBinding::inflate, R.layout.item_tax) {
    override fun convert(holder: BaseViewBindingHolder<ItemTaxBinding>, item: Balance) {
        holder.viewBind.apply {
            tvTitle.text = item.name
            tvAmount.text = "¥${item.balance.formatAmount()}元"
            if (item.isCheck) {
                llRoot.setBackgroundResource(R.drawable.shape_select_tax)
                tvAmount.setTextColor("#3B82F6".toColorInt())
            } else {
                llRoot.setBackgroundResource(R.drawable.shape_no_select_tax)
                tvAmount.setTextColor("#3D3D3D".toColorInt())
            }
        }
    }
}