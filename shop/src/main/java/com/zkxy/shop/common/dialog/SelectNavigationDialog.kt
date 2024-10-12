package com.zkxy.shop.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.zkxy.shop.R
import com.zkxy.shop.databinding.DialogSelectNavigationBinding
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast
import com.zyxcoder.mvvmroot.utils.dpToPx

class SelectNavigationDialog(context: Context) : Dialog(context, R.style.MyBottomDialogTheme) {

    var onSelectNavigationListener: ((isBaidu: Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mViewBind = DialogSelectNavigationBinding.inflate(layoutInflater)
        setContentView(mViewBind.root)
        window?.apply {
            setGravity(Gravity.BOTTOM)
            setWindowAnimations(R.style.shop_main_menu_animStyle);
            attributes.apply {
                height = dpToPx(170f).toInt()
                width = MATCH_PARENT
                attributes = this
            }
        }

        mViewBind.apply {
            tvBaidu.onContinuousClick {
                try {
                    context.packageManager?.getPackageInfo("com.baidu.BaiduMap", 0)
                    onSelectNavigationListener?.invoke(true)
                } catch (e: Exception) {
                    context.showToast("请先安装百度地图")
                }
                dismiss()
            }
            tvGd.onContinuousClick {
                try {
                    context.packageManager?.getPackageInfo("com.autonavi.minimap", 0)
                    onSelectNavigationListener?.invoke(false)
                } catch (e: Exception) {
                    context.showToast("请先安装高德地图")
                }
                dismiss()
            }
        }
    }
}