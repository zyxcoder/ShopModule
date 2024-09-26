package com.zkxy.shop.ui.goods.adapter

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemShopSelectAddressBinding
import com.zkxy.shop.entity.goods.AddressBookEntity
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder

class SelectAddressAdapter : BaseViewBindingAdapter<AddressBookEntity, ItemShopSelectAddressBinding>(
    ItemShopSelectAddressBinding::inflate,
    R.layout.item_shop_select_address
) {

    private var checkedDrawable: Drawable? = null
    private var normalDrawable: Drawable? = null

    init {
        addChildClickViewIds(R.id.tvNormal, R.id.tvDeleteAddress, R.id.tvEditAddress)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        checkedDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_save_book_checked)
        normalDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_save_book_normal)
    }

    override fun convert(
        holder: BaseViewBindingHolder<ItemShopSelectAddressBinding>,
        item: AddressBookEntity
    ) {
        holder.viewBind.apply {
            val drawable = if (item.isCheck) checkedDrawable else normalDrawable
            tvNormal.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
    }

    override fun convert(
        holder: BaseViewBindingHolder<ItemShopSelectAddressBinding>,
        item: AddressBookEntity,
        payloads: List<Any>
    ) {
        if (payloads[0].toString() == "check") {
            val drawable = if (item.isCheck) checkedDrawable else normalDrawable
            holder.viewBind.tvNormal.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
    }
}