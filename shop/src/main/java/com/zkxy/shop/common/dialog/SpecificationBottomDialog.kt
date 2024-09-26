package com.zkxy.shop.common.dialog

import androidx.fragment.app.FragmentManager
import com.gxy.common.base.BaseBottomSheetDialogFragment
import com.zkxy.shop.databinding.DialogShopSpecificationListBinding
import com.zkxy.shop.ui.goods.adapter.SpecificationAdapter
import com.zyxcoder.mvvmroot.ext.onContinuousClick

class SpecificationBottomDialog : BaseBottomSheetDialogFragment<DialogShopSpecificationListBinding>() {

    private val specificationAdapter by lazy { SpecificationAdapter() }

    override fun initView() {
        mViewBind.apply {
            rlv.adapter = specificationAdapter
            ivClose.onContinuousClick { dismiss() }
        }
        specificationAdapter.setList(mutableListOf("", "", "", "", "", "", "", "", "", ""))
    }

    override fun resetDialogHeightPercent(): Float = 0.5f

    fun show(manager: FragmentManager) {
        super.show(manager, "SpecificationBottomDialog")
    }
}