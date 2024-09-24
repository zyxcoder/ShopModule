package com.zkxy.shop.ui.category.adapter

import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemCategoryMinorBinding
import com.zkxy.shop.entity.category.CategoryMinorEntity
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder
import com.zyxcoder.mvvmroot.ext.onContinuousClick

/**
 * @author zhangyuxiang
 * @date 2024/9/24
 */
class CategoryMinorAdapter(
    private val onCategoryMinorClickListener: ((categoryMinorEntity: CategoryMinorEntity) -> Unit)? = null
) : BaseViewBindingAdapter<CategoryMinorEntity, ItemCategoryMinorBinding>(
    ItemCategoryMinorBinding::inflate, R.layout.item_category_minor
) {
    override fun convert(
        holder: BaseViewBindingHolder<ItemCategoryMinorBinding>, item: CategoryMinorEntity
    ) {
        holder.viewBind.apply {
            root.onContinuousClick {
                onCategoryMinorClickListener?.invoke(item)
            }
            tvCategoryName.text = item.categoryName
        }
    }
}