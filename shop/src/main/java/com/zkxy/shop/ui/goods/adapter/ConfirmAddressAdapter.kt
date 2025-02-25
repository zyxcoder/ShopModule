package com.zkxy.shop.ui.goods.adapter

import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemConfirmAddressBinding
import com.zkxy.shop.entity.order.ConfirmAddressEntity
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder

class ConfirmAddressAdapter :
    BaseViewBindingAdapter<ConfirmAddressEntity, ItemConfirmAddressBinding>(
        ItemConfirmAddressBinding::inflate,
        R.layout.item_confirm_address
    ) {
    override fun convert(
        holder: BaseViewBindingHolder<ItemConfirmAddressBinding>,
        item: ConfirmAddressEntity
    ) {
        holder.viewBind.apply {
            tvAddress.text = item.label
            ivCheck.setImageResource(if (item.isCheck) R.drawable.ic_save_book_checked else R.drawable.ic_save_book_normal)
        }
    }
}