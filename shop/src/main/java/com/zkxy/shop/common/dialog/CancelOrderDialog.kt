package com.zkxy.shop.common.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
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
    var onCancelClickListener: (() -> Unit)? = null,
    val isPay: Boolean = false
) : CenterDialog(context) {

    private lateinit var mBinding: DialogCancelOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogCancelOrderBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setCanceledOnTouchOutside(true)

        mBinding.apply {
            if (isPay) {
                tvSubMessage.visibility = View.GONE
                tvMessage.text = "确定支付此订单？"
                tvConfirm.text = "支付"
            }
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