package com.zkxy.shop.ui.category.adapter

import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemCategoryMinorBinding
import com.zkxy.shop.entity.category.GoodsCategoryEntity
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder
import com.zyxcoder.mvvmroot.ext.onContinuousClick

/**
 * @author zhangyuxiang
 * @date 2024/9/24
 */
class CategoryMinorAdapter : BaseViewBindingAdapter<GoodsCategoryEntity, ItemCategoryMinorBinding>(
    ItemCategoryMinorBinding::inflate, R.layout.item_category_minor
) {
    var onCategoryClickListener: ((categoryMinorEntity: GoodsCategoryEntity) -> Unit)? = null
    override fun convert(
        holder: BaseViewBindingHolder<ItemCategoryMinorBinding>, item: GoodsCategoryEntity
    ) {
        holder.viewBind.apply {
            root.onContinuousClick {
                onCategoryClickListener?.invoke(item)
            }
            tvCategoryName.text = item.name
        }
    }
}