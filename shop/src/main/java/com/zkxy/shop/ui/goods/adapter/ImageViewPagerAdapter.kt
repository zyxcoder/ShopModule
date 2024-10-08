package com.zkxy.shop.ui.goods.adapter

import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemImageViewPagerBinding
import com.zkxy.shop.entity.goods.PicDto
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder
import com.zyxcoder.mvvmroot.utils.loadImage

class ImageViewPagerAdapter : BaseViewBindingAdapter<PicDto, ItemImageViewPagerBinding>(
    ItemImageViewPagerBinding::inflate,
    R.layout.item_image_view_pager
) {
    override fun convert(holder: BaseViewBindingHolder<ItemImageViewPagerBinding>, item: PicDto) {
        holder.viewBind.sdPoundImg.loadImage(item.picUrl)
    }
}