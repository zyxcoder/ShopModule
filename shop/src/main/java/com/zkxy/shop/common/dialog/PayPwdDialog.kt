package com.zkxy.shop.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.zkxy.shop.R
import com.zkxy.shop.databinding.DialogPayPwdBinding
import com.zyxcoder.mvvmroot.ext.showToast

class PayPwdDialog(
    context: Context,

) : Dialog(context, R.style.dialog) {
    var onConfirmClickListener: ((payPwd: String) -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pwdBinding = DialogPayPwdBinding.inflate(layoutInflater)
        setContentView(pwdBinding.root)
        pwdBinding.apply {
            tvCancel.setOnClickListener { dismiss() }
            tvConfirm.setOnClickListener {
                val payPwd = settingPwvAg.text.toString().trim()
                if (payPwd.length != 6) {
                    context.showToast("请输入正确的支付密码")
                } else {
                    onConfirmClickListener?.invoke(payPwd)
                }
            }
        }

    }
}