package com.zkxy.shop.common.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import com.zk.common.common.dialog.CenterDialog
import com.zkxy.shop.databinding.DialogCancelOrderBinding
import com.zyxcoder.mvvmroot.ext.onContinuousClick

class CreateOrderDialog(
    context: Context,
    var onConfirmClickListener: (() -> Unit)? = null,
    private val message: String = "确定下单该商品？"
) : CenterDialog(context) {

    private lateinit var mBinding: DialogCancelOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogCancelOrderBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setCanceledOnTouchOutside(true)

        mBinding.apply {
            tvSubMessage.visibility = View.GONE
            tvMessage.text = message
            tvCancel.onContinuousClick { dismiss() }
            tvConfirm.onContinuousClick {
                onConfirmClickListener?.invoke()
                dismiss()
            }
        }
    }
}