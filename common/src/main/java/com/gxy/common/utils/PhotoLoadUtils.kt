package com.gxy.common.utils

import android.net.Uri
import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.zyxcoder.mvvmroot.utils.loadImage

/**
 * @author zhangyuxiang
 * @date 2024/3/13
 */
fun photoLoadUtils(imageView: ImageView, result: ArrayList<LocalMedia>) {
    val media = result[0]
    val path = media.availablePath
    if (PictureMimeType.isContent(path) && !media.isCut && !media.isCompressed) {
        imageView.loadImage(Uri.parse(path))
    } else {
        imageView.loadImage(path)
    }
}

fun photoLoadUtils(imageView: SimpleDraweeView, result: ArrayList<LocalMedia>) {
    val media = result[0]
    val path = media.availablePath
    if (PictureMimeType.isContent(path) && !media.isCut && !media.isCompressed) {
        imageView.setImageURI(Uri.parse(path))
    } else {
        imageView.setImageURI(path)
    }
}