package com.zkxy.shopmodule

import android.content.Context
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import com.gxy.common.common.dialog.CenterDialog
import com.zkxy.shop.appUserTel
import com.zkxy.shop.modeBaseUrl
import com.zkxy.shopmodule.databinding.DialogSelectUrlBinding
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast

/**
 * @author zhangyuxiang
 * @date 2024/2/28
 */
class ShopConfigDialog(
    context: Context,
    var onConfirmClickListener: (() -> Unit)? = null,
) : CenterDialog(context) {

    private lateinit var mBinding: DialogSelectUrlBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogSelectUrlBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setCanceledOnTouchOutside(false)
        mBinding.apply {
            llPhone.setPhone("18584896026")
            etHost.doAfterTextChanged {
                textView.text = "http://${etHost.text}:${etPort.text}"
            }

            var isTest = true
            radioGroup.setOnCheckedChangeListener { _, i ->
                isTest = i == R.id.rbTest
            }

            btConfirm.onContinuousClick {
                if (llPhone.contentIsEmptyAndShowToast()) {
                    context.showToast("请输入完整手机号")
                    return@onContinuousClick
                }
                modeBaseUrl = if (isTest) {
                    "http://192.168.1.188:88/"
//                    "http://39.103.61.134:9091/api/"
                } else {
                    "http://${etHost.text}:${etPort.text}"
                }
                appUserTel = llPhone.getPhone()
                onConfirmClickListener?.invoke()
                dismiss()
            }
        }
    }
}