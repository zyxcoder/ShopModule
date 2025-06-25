package com.zkxy.shop.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.zkxy.shop.R
import com.zkxy.shop.databinding.DialogRefundBinding
import com.zyxcoder.mvvmroot.utils.dpToPx

class RefundDialog(context: Context) : Dialog(context, R.style.MyBottomDialogTheme) {

    var onConfirmRefundClickListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mViewBind = DialogRefundBinding.inflate(layoutInflater)
        setContentView(mViewBind.root)
        window?.apply {
            setGravity(Gravity.BOTTOM)
            setWindowAnimations(R.style.shop_main_menu_animStyle)
            attributes.apply {
                height = dpToPx(416f).toInt()
                width = MATCH_PARENT
                attributes = this
            }
        }

        mViewBind.apply {
            rg.setOnCheckedChangeListener { _, id ->

            }
        }
    }
}