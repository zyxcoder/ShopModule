package com.zkxy.shop.common.dialog

import androidx.fragment.app.FragmentManager
import com.gxy.common.base.BaseBottomSheetDialogFragment
import com.zkxy.shop.databinding.DialogSelectNavigationBinding
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast

class SelectNavigationDialog : BaseBottomSheetDialogFragment<DialogSelectNavigationBinding>() {

    var onSelectNavigationListener: ((isBaidu: Boolean) -> Unit)? = null

    override fun initView() {
        mViewBind.apply {
            tvBaidu.onContinuousClick {
                try {
                    context?.packageManager?.getPackageInfo("com.baidu.BaiduMap", 0)
                    onSelectNavigationListener?.invoke(true)
                } catch (e: Exception) {
                    context.showToast("请先安装百度地图")
                }
                dismiss()
            }
            tvGd.onContinuousClick {
                try {
                    context?.packageManager?.getPackageInfo("com.autonavi.minimap", 0)
                    onSelectNavigationListener?.invoke(false)
                } catch (e: Exception) {
                    context.showToast("请先安装高德地图")
                }
                dismiss()
            }
        }
    }

    override fun resetDialogHeightPercent(): Float = 0.3f

    fun show(manager: FragmentManager) {
        super.show(manager, "SelectNavigationDialog")
    }
}