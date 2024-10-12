package com.gxy.common.common.dialog

import android.content.Context
import android.os.Bundle
import androidx.core.view.isVisible
import com.gxy.common.databinding.DialogCommonBinding

/**
 * @author zhangyuxiang
 * @date 2024/2/28
 */
class CommonDialog(
    context: Context,
    private val message: String,
    private val subMessage: String? = null
) :
    CenterDialog(context) {

    var onConfirmClickListener: (() -> Unit)? = null

    private lateinit var mBinding: DialogCommonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogCommonBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setCanceledOnTouchOutside(true)
        mBinding.apply {
            tvMessage.text = message
            tvSubMessage.text = subMessage
            tvSubMessage.isVisible = !subMessage.isNullOrEmpty()
            tvCancel.setOnClickListener {
                dismiss()
            }
            tvConfirm.setOnClickListener {
                onConfirmClickListener?.invoke()
                dismiss()
            }
        }
    }
}