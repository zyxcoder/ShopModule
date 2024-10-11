package com.zkxy.shop.common.dialog

import android.content.Context
import android.os.Bundle
import com.gxy.common.common.dialog.CenterDialog
import com.zkxy.shop.databinding.DialogCancelOrderBinding
import com.zyxcoder.mvvmroot.ext.onContinuousClick

/**
 * @author zhangyuxiang
 * @date 2024/2/28
 */
class CancelOrderDialog(
    context: Context,
    var onConfirmClickListener: (() -> Unit)? = null,
    var onCancelClickListener: (() -> Unit)? = null
) : CenterDialog(context) {

    private lateinit var mBinding: DialogCancelOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogCancelOrderBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setCanceledOnTouchOutside(true)
        mBinding.apply {
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