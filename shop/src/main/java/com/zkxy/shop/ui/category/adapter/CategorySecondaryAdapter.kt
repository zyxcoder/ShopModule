package com.zkxy.shop.ui.category.adapter

import androidx.recyclerview.widget.GridLayoutManager
import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemCategorySecondaryBinding
import com.zkxy.shop.entity.category.GoodsCategoryEntity
import com.zkxy.shop.ui.home.decoration.CategoryAverageMarginDecoration
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder
import com.zyxcoder.mvvmroot.ext.onContinuousClick

/**
 * @author zhangyuxiang
 * @date 2024/9/24
 */
class CategorySecondaryAdapter :
    BaseViewBindingAdapter<GoodsCategoryEntity, ItemCategorySecondaryBinding>(
        ItemCategorySecondaryBinding::inflate, R.layout.item_category_secondary
    ) {
    var onCategorySecondaryClickListener: ((categorySecondaryEntity: GoodsCategoryEntity) -> Unit)? =
        null
    var onCategoryMinorClickListener: ((categorySecondaryEntity: GoodsCategoryEntity, categoryMinorEntity: GoodsCategoryEntity) -> Unit)? =
        null

    override fun convert(
        holder: BaseViewBindingHolder<ItemCategorySecondaryBinding>, item: GoodsCategoryEntity
    ) {
        holder.viewBind.apply {
            tvSecCateGoryName.onContinuousClick {
                onCategorySecondaryClickListener?.invoke(item)
            }
            tvSecCateGoryName.text = item.name
            CategoryMinorAdapter().apply {
                onCategoryClickListener = {
                    onCategoryMinorClickListener?.invoke(item, it)
                }
                setList(item.children)
                rvMinorCategory.adapter = this
                rvMinorCategory.layoutManager = GridLayoutManager(context, 3)
                rvMinorCategory.addItemDecoration(CategoryAverageMarginDecoration())
            }
        }
    }
}