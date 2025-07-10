package com.zkxy.shop.common.dialog

import android.content.Context
import android.os.Bundle
import com.gxy.common.common.dialog.CenterDialog
import com.zkxy.shop.databinding.DialogCancelBinding
import com.zyxcoder.mvvmroot.ext.onContinuousClick

class CancelDialog(
    context: Context,
    var onConfirmClickListener: (() -> Unit)? = null,
    var onCancelClickListener: (() -> Unit)? = null,
    private val message: String? = "确定取消此订单？"
) : CenterDialog(context) {

    private lateinit var mBinding: DialogCancelBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogCancelBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setCanceledOnTouchOutside(true)

        mBinding.apply {
            tvMessage.text = message
            tvCancel.onContinuousClick {
                onCancelClickListener?.invoke()
                dismiss()
            }
            tvConfirm.onContinuousClick {
                onConfirmClickListener?.invoke()
                dismiss()
            }
        }
    }
}