package com.zkxy.shop.ui.category.adapter

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemCategoryPrimaryBinding
import com.zkxy.shop.entity.category.CategoryEntity
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder
import com.zyxcoder.mvvmroot.ext.onContinuousClick

/**
 * @author zhangyuxiang
 * @date 2024/9/24
 */
class CategoryPrimaryAdapter : BaseViewBindingAdapter<CategoryEntity, ItemCategoryPrimaryBinding>(
    ItemCategoryPrimaryBinding::inflate, R.layout.item_category_primary
) {
    var onCategoryPrimaryClickListener: ((category: CategoryEntity) -> Unit)? = null
    override fun convert(
        holder: BaseViewBindingHolder<ItemCategoryPrimaryBinding>,
        item: CategoryEntity
    ) {
        holder.viewBind.apply {
            root.onContinuousClick {
                onCategoryPrimaryClickListener?.invoke(item)
            }
            viewSelect.isVisible = item.isSelect == true
            tvTitle.text = item.categoryName
            tvTitle.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (item.isSelect == true) R.color.clolor_566beb else R.color.clolor_666666
                )
            )
            tvTitle.setTextSize(if (item.isSelect == true) 16f else 14f)
            clRoot.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    if (item.isSelect == true) R.color.white else R.color.gary_f2f5f9
                )
            )
        }
    }
}