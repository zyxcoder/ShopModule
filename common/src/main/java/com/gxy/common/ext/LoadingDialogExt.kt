package com.gxy.common.ext

import androidx.fragment.app.FragmentActivity
import com.gxy.common.common.dialog.LoadingDialog

/**
 * Create by zyx_coder on 2023/1/10
 */

private var loadingDialog: LoadingDialog? = null

/**
 * 打开等待框
 */
fun FragmentActivity.showLoadingExt() {
    if (!this.isFinishing) {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog().apply {
                isCancelable = false
            }
        }
        loadingDialog?.show(supportFragmentManager, "LoadingDialog")
    }
}

/**
 * 关闭等待框
 */
fun dismissLoadingExt() {
    loadingDialog?.dismiss()
    loadingDialog = null
}