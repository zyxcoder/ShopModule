package com.zkxy.shop.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.RadioButton
import com.zkxy.shop.R
import com.zkxy.shop.databinding.DialogAfterSalesBinding
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast
import com.zyxcoder.mvvmroot.utils.dpToPx

class AfterSalesDialog(context: Context) : Dialog(context, R.style.MyBottomDialogTheme) {

    var onConfirmClickListener: ((des: String) -> Unit)? =
        null

    private lateinit var mViewBind: DialogAfterSalesBinding
    private var des = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBind = DialogAfterSalesBinding.inflate(layoutInflater)
        setContentView(mViewBind.root)
        window?.apply {
            setGravity(Gravity.BOTTOM)
            setWindowAnimations(R.style.shop_main_menu_animStyle);
            attributes.apply {
                height = dpToPx(416f).toInt()
                width = MATCH_PARENT
                attributes = this
            }
        }

        mViewBind.apply {
            rg.setOnCheckedChangeListener { _, id ->
                des = findViewById<RadioButton>(id).text.toString()
            }
            tvConfirm.onContinuousClick {
                if (des.isEmpty()) {
                    context.showToast("请选择退款原因")
                } else {
                    onConfirmClickListener?.invoke(des)
                    dismiss()
                }
            }
        }
    }
}