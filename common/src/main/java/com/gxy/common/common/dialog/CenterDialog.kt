package com.gxy.common.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.gxy.common.R

/**
 * 需要缩放的dialog继承此类
 */
open class CenterDialog(context: Context) : Dialog(context, R.style.TransparentProgressDialog) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.attributes?.windowAnimations = R.style.centerDialogZoomAnim
    }
}