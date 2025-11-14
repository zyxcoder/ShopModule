package com.zkxy.shop.utils

import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.zkxy.shop.entity.goods.PicDto
import com.zyxcoder.mvvmroot.utils.loadImage

//import android.content.Context
//import android.widget.ImageView
//import com.facebook.drawee.view.SimpleDraweeView
//import com.youth.banner.loader.ImageLoader
//import com.zkxy.shop.entity.goods.PicDto
//import com.zyxcoder.mvvmroot.utils.loadImage
//
//class GoodsDetailsImageLoader : ImageLoader() {
//    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
//        val picDto = path as? PicDto
//        imageView.loadImage(picDto?.picUrl ?: "")
//    }
//
//    override fun createImageView(context: Context): ImageView {
//        return SimpleDraweeView(context)
//    }
//}

class GoodsDetailsImageLoader(datas: List<PicDto>) :
    BannerImageAdapter<PicDto>(datas) {
    override fun onBindView(
        holder: BannerImageHolder,
        data: PicDto?,
        position: Int,
        size: Int
    ) {
        holder.imageView.loadImage(data?.picUrl ?: "")
    }
}