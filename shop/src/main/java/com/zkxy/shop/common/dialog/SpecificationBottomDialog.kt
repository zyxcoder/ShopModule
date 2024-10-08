package com.zkxy.shop.common.dialog

import androidx.fragment.app.FragmentManager
import com.gxy.common.base.BaseBottomSheetDialogFragment
import com.zkxy.shop.databinding.DialogShopSpecificationListBinding
import com.zkxy.shop.entity.goods.GoodsSpecDto
import com.zkxy.shop.ui.goods.adapter.SpecificationAdapter
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast

class SpecificationBottomDialog :
    BaseBottomSheetDialogFragment<DialogShopSpecificationListBinding>() {

    private val specificationAdapter by lazy { SpecificationAdapter() }
    var onItemClickListener: ((GoodsSpecDto) -> Unit)? = null

    override fun initView() {
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

    override fun resetDialogHeightPercent(): Float = 0.5f

    fun show(manager: FragmentManager) {
        if (specificationAdapter.data.isEmpty()) {
            context.showToast("暂无数据")
            return
        }
        super.show(manager, "SpecificationBottomDialog")
    }
}