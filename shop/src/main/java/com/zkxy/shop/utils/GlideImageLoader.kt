package com.zkxy.shop.utils

import android.content.Context
import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.youth.banner.loader.ImageLoader
import com.zkxy.shop.entity.goods.PicDto
import com.zyxcoder.mvvmroot.utils.loadImage

/**
 * @author zhangyuxiang
 * @date 2024/9/19
 */
class GlideImageLoader : ImageLoader() {
    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        val homeShopBannerEntity = path as? PicDto
        imageView.loadImage(homeShopBannerEntity?.picUrl ?: "")
    }

    override fun createImageView(context: Context): ImageView {
        return SimpleDraweeView(context)
    }
}