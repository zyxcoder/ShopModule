package com.zkxy.shop.ui.category.adapter

import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemCategorySecondaryBinding
import com.zkxy.shop.entity.category.CategoryMinorEntity
import com.zkxy.shop.entity.category.CategorySecondaryEntity
import com.zkxy.shop.ui.home.decoration.CategoryAverageMarginDecoration
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder
import com.zyxcoder.mvvmroot.ext.onContinuousClick

/**
 * @author zhangyuxiang
 * @date 2024/9/24
 */
class CategorySecondaryAdapter :
    BaseViewBindingAdapter<CategorySecondaryEntity, ItemCategorySecondaryBinding>(
        ItemCategorySecondaryBinding::inflate, R.layout.item_category_secondary
    ) {
    var onCategorySecondaryClickListener: ((categorySecondaryEntity: CategorySecondaryEntity) -> Unit)? =
        null
    var onCategoryMinorClickListener: ((categoryMinorEntity: CategoryMinorEntity) -> Unit)? = null

    override fun convert(
        holder: BaseViewBindingHolder<ItemCategorySecondaryBinding>, item: CategorySecondaryEntity
    ) {
        holder.viewBind.apply {
            tvSecCateGoryName.onContinuousClick {
                onCategorySecondaryClickListener?.invoke(item)
            }
            tvSecCateGoryName.text = item.categoryName
            CategoryMinorAdapter(onCategoryMinorClickListener = onCategoryMinorClickListener).apply {
                setList(item.categoryMinorList)
                rvMinorCategory.adapter = this
                rvMinorCategory.addItemDecoration(CategoryAverageMarginDecoration())
            }
        }
    }
}