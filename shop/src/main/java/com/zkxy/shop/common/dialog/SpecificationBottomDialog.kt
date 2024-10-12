package com.zkxy.shop.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.zkxy.shop.R
import com.zkxy.shop.databinding.DialogShopSpecificationListBinding
import com.zkxy.shop.entity.goods.GoodsSpecDto
import com.zkxy.shop.ui.goods.adapter.SpecificationAdapter
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.utils.dpToPx

class SpecificationBottomDialog(context: Context) : Dialog(context, R.style.MyBottomDialogTheme) {

    private val specificationAdapter by lazy { SpecificationAdapter() }
    var onItemClickListener: ((GoodsSpecDto) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mViewBind = DialogShopSpecificationListBinding.inflate(layoutInflater)
        setContentView(mViewBind.root)
        window?.apply {
            setGravity(Gravity.BOTTOM)
            setWindowAnimations(R.style.shop_main_menu_animStyle);
            attributes.apply {
                height = dpToPx(450f).toInt()
                width = MATCH_PARENT
                attributes = this
            }
        }
        mViewBind.apply {
            rlv.adapter = specificationAdapter
            ivClose.onContinuousClick { dismiss() }
        }

        specificationAdapter.setOnItemClickListener { _, _, position ->
            onItemClickListener?.invoke(specificationAdapter.data[position])
            dismiss()
        }
    }

    fun setData(goodsSpecDtoList: MutableList<GoodsSpecDto>?) {
        specificationAdapter.setList(goodsSpecDtoList)
    }

}