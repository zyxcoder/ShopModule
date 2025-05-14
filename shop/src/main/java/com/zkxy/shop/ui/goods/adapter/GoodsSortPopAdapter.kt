package com.zkxy.shop.ui.goods.adapter

import androidx.core.view.isVisible
import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemGoodsSortSelectBinding
import com.zkxy.shop.entity.goods.GoodsPointEntity
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder
import com.zyxcoder.mvvmroot.ext.onContinuousClick

/**
 * @author zhangyuxiang
 * @date 2024/9/26
 */
class GoodsSortPopAdapter : BaseViewBindingAdapter<GoodsPointEntity, ItemGoodsSortSelectBinding>(
    ItemGoodsSortSelectBinding::inflate, R.layout.item_goods_sort_select
) {
    var onGoodsPointSelectListener: ((goodsPointEntity: GoodsPointEntity) -> Unit)? = null
    override fun convert(
        holder: BaseViewBindingHolder<ItemGoodsSortSelectBinding>, item: GoodsPointEntity
    ) {
        holder.viewBind.apply {
            root.onContinuousClick {
                onGoodsPointSelectListener?.invoke(item)
            }
            tvPoint.text = item.name
            ivCheck.isVisible = item.isSelect == true
        }
    }
}