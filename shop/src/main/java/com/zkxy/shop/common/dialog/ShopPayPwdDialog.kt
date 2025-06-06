package com.zkxy.shop.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.zkxy.shop.R
import com.zkxy.shop.databinding.DialogShopPayPwdBinding
import com.zyxcoder.mvvmroot.ext.showToast

class ShopPayPwdDialog(context: Context) : Dialog(context, R.style.dialog) {
    var onConfirmClickListener: ((payPwd: String) -> Unit)? = null
    private var pwdBinding: DialogShopPayPwdBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pwdBinding.apply {
            pwdBinding = DialogShopPayPwdBinding.inflate(layoutInflater)
            pwdBinding?.apply {
                setContentView(root)
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

    override fun show() {
        super.show()
        pwdBinding?.settingPwvAg?.setText("")
    }
}