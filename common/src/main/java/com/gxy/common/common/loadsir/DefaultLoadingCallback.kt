package com.gxy.common.common.loadsir

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.gxy.common.R
import com.kingja.loadsir.callback.Callback
import com.zyxcoder.mvvmroot.utils.loadImage

/**
 * @author zhangyuxiang
 * @date 2024/1/26
 */
class DefaultLoadingCallback : Callback() {
    override fun onCreateView(): Int = R.layout.layout_loading_dialog

    override fun onViewCreate(context: Context, view: View) {
        view.findViewById<ImageView>(R.id.ivLoading).loadImage(R.drawable.gif_loading)
    }
}