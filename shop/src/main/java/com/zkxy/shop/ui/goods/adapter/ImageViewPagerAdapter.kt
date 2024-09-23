package com.zkxy.shop.ui.goods.adapter

import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemImageViewPagerBinding
import com.zkxy.shop.entity.home.HomeShopBannerEntity
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder
import com.zyxcoder.mvvmroot.utils.loadImage

class ImageViewPagerAdapter : BaseViewBindingAdapter<HomeShopBannerEntity, ItemImageViewPagerBinding>(
    ItemImageViewPagerBinding::inflate,
    R.layout.item_image_view_pager
) {
    override fun convert(holder: BaseViewBindingHolder<ItemImageViewPagerBinding>, item: HomeShopBannerEntity) {
        holder.viewBind.sdPoundImg.loadImage(item.imageUrl)
    }
}