package com.zkxy.shop.common.dialog

import android.content.Context
import android.os.Bundle
import androidx.core.view.isVisible
import com.gxy.common.common.dialog.CenterDialog
import com.zkxy.shop.databinding.DialogShopCommonBinding
import com.zyxcoder.mvvmroot.ext.onContinuousClick

/**
 * @author zhangyuxiang
 * @date 2024/2/28
 */
class ShopCommonDialog(
    context: Context,
    private val message: String,
    private val subMessage: String? = null,
    private val cancelContent: String = "取消",
    private var confirmContent: String = "确定"
) : CenterDialog(context) {

    var onConfirmClickListener: (() -> Unit)? = null
    var onCancelClickListener: (() -> Unit)? = null

    private lateinit var mBinding: DialogShopCommonBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogShopCommonBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setCanceledOnTouchOutside(true)
        mBinding.apply {
            tvMessage.text = message
            tvSubMessage.text = subMessage
            tvSubMessage.isVisible = !subMessage.isNullOrEmpty()
            tvCancel.onContinuousClick {
                onCancelClickListener?.invoke()
                dismiss()
            }
            tvConfirm.onContinuousClick {
                onConfirmClickListener?.invoke()
                dismiss()
            }
            tvCancel.text = cancelContent
            tvConfirm.text = confirmContent
        }
    }
}