package com.zkxy.shop.ext

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import com.zyxcoder.mvvmroot.ext.showToast

//子线程使用
fun Context.save(bitmap: Bitmap) {
    val saveUri = contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        ContentValues()
    )
    saveUri?.let {
        if (it.toString().isNotEmpty()) {
            val outputStream = contentResolver.openOutputStream(it)
            if (outputStream != null) {
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)) {
                    this.showToast("保存到相册成功")
                }
            }
        }
    }
}