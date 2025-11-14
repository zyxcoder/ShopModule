package com.zk.common.common.loadsir

import android.content.Context
import android.view.View
import com.zk.common.R
import com.kingja.loadsir.callback.Callback

/**
 * @author zhangyuxiang
 * @date 2024/1/26
 */
class EmptyZkCallback : Callback() {
    override fun onCreateView(): Int = R.layout.view_zk_data_empty
    override fun onReloadEvent(context: Context?, view: View?) = true
}