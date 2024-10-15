package com.zkxy.shop.utils

import android.content.Context
import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.youth.banner.loader.ImageLoader
import com.zkxy.shop.entity.home.HomeShopBannerEntity
import com.zyxcoder.mvvmroot.utils.loadImage

/**
 * @author zhangyuxiang
 * @date 2024/10/9
 */
class HomeBannerImageLoader : ImageLoader() {
    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        val homeShopBannerEntity = path as? HomeShopBannerEntity
        imageView.loadImage(homeShopBannerEntity?.imgUrl)
    }

    override fun createImageView(context: Context): ImageView {
        return SimpleDraweeView(context)
    }
}