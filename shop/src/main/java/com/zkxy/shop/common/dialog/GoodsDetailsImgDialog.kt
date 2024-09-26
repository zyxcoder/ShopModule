package com.zkxy.shop.common.dialog

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.gxy.common.common.dialog.CenterDialog
import com.zkxy.shop.databinding.DialogGoodsDetailImgBinding
import com.zkxy.shop.entity.home.HomeShopBannerEntity
import com.zkxy.shop.ui.goods.adapter.ImageViewPagerAdapter
import com.zyxcoder.mvvmroot.ext.onContinuousClick

class GoodsDetailsImgDialog(
    context: Context,
    private val imageUrl: MutableList<HomeShopBannerEntity>
) : CenterDialog(context = context) {

    private val mBinding by lazy { DialogGoodsDetailImgBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setCanceledOnTouchOutside(true)
        window?.attributes?.apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
            window?.attributes = this
        }
        mBinding.apply {
            ivClose.onContinuousClick { dismiss() }
            val imageViewPagerAdapter = ImageViewPagerAdapter()
            imageViewPagerAdapter.setNewInstance(imageUrl)
            vpImage.adapter = imageViewPagerAdapter
        }
    }

    fun setPosition(position: Int) {
        mBinding.vpImage.setCurrentItem(position, false)
    }
}