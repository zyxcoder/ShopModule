package com.zkxy.shop.ui.home.adapter

import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemGoodsBinding
import com.zkxy.shop.entity.home.GoodsEntity
import com.zkxy.shop.utils.formatProductInfo
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.utils.ImageOptions
import com.zyxcoder.mvvmroot.utils.dpToPx
import com.zyxcoder.mvvmroot.utils.loadImage

/**
 * @author zhangyuxiang
 * @date 2024/9/19
 */
class GoodsAdapter : BaseViewBindingAdapter<GoodsEntity, ItemGoodsBinding>(
    ItemGoodsBinding::inflate, R.layout.item_goods
) {
    var onGoodsItemClickListener: ((goodsItem: GoodsEntity) -> Unit)? = null
    override fun convert(holder: BaseViewBindingHolder<ItemGoodsBinding>, item: GoodsEntity) {
        holder.viewBind.apply {
            root.onContinuousClick {
                onGoodsItemClickListener?.invoke(item)
            }
            ivGoods.loadImage(item.goodsUrl, ImageOptions().apply {
                topLeftRadius = dpToPx(6f)
                topRightRadius = dpToPx(6f)
                centerCrop = false
            })
            tvGoodsName.text = item.goodsName
            tvGoodsPrice.text = formatProductInfo(item.goodsPrice, item.goodsPoint)
        }
    }
}