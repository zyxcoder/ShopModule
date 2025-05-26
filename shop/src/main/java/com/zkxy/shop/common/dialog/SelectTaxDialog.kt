package com.zkxy.shop.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.zkxy.shop.R
import com.zkxy.shop.databinding.DialogSelectTaxBinding
import com.zkxy.shop.entity.goods.Balance
import com.zkxy.shop.ui.order.adapter.SelectTaxAdapter
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.utils.dpToPx

class SelectTaxDialog(context: Context) : Dialog(context, R.style.MyBottomDialogTheme) {

    var onConfirmClickListener: ((balance: Balance?) -> Unit)? = null

    private val selectTaxAdapter by lazy { SelectTaxAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val selectTaxBinding = DialogSelectTaxBinding.inflate(layoutInflater)
        setContentView(selectTaxBinding.root)
        selectTaxBinding.rlv.adapter = selectTaxAdapter
        window?.apply {
            setGravity(Gravity.BOTTOM)
            setWindowAnimations(R.style.shop_main_menu_animStyle);
            attributes.apply {
                height = dpToPx(416f).toInt()
                width = MATCH_PARENT
                attributes = this
            }
        }
        selectTaxAdapter.setOnItemClickListener { _, _, position ->
            selectTaxAdapter.data.forEachIndexed { index, balance ->
                balance.isCheck = position == index
            }
            selectTaxAdapter.notifyDataSetChanged()
        }
        selectTaxBinding.tvConfirm.onContinuousClick {
            onConfirmClickListener?.invoke(selectTaxAdapter.data.find { it.isCheck })
            dismiss()
        }
    }

    fun setData(list: MutableList<Balance>?) {
        list?.forEachIndexed { index, balance ->
            balance.isCheck = index == 0
        }
        selectTaxAdapter.setList(list)
    }
}