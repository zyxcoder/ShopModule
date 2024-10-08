package com.zkxy.shop.ui.goods

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.gxy.common.base.BaseViewBindActivity
import com.gyf.immersionbar.ImmersionBar
import com.zkxy.shop.R
import com.zkxy.shop.common.dialog.GoodsDetailsImgDialog
import com.zkxy.shop.databinding.ActivityGoodsDetailsBinding
import com.zkxy.shop.databinding.ItemGoodsDetailsImageBinding
import com.zkxy.shop.entity.goods.GoodsDetailsEntity
import com.zkxy.shop.entity.goods.PicDto
import com.zkxy.shop.ext.doubleToTwoDecimalPlaceString
import com.zkxy.shop.utils.GlideImageLoader
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast
import com.zyxcoder.mvvmroot.utils.dpToPx
import com.zyxcoder.mvvmroot.utils.loadImage

class GoodsDetailsActivity :
    BaseViewBindActivity<GoodsDetailsViewModel, ActivityGoodsDetailsBinding>() {

    companion object {
        const val GOODS_ID = "goodsId"
        fun startActivity(context: Context, goodsId: Int? = null) {
            context.startActivity(Intent(context, GoodsDetailsActivity::class.java).apply {
                putExtra(GOODS_ID, goodsId)
            })
        }
    }

    private var goodsDetailsImgDialog: GoodsDetailsImgDialog? = null

    override fun initView(savedInstanceState: Bundle?) {
        val goodsId = intent.getIntExtra(GOODS_ID, 15)
        mViewModel.goodsDetail(goodsId)
        val height = dpToPx(150f).toInt()
        mViewBind.apply {
            ImmersionBar.with(this@GoodsDetailsActivity).statusBarDarkFont(true)
                .statusBarView(titleBar).init()
            ivBack.onContinuousClick { finish() }

            banner.setImageLoader(GlideImageLoader())
                .setOnBannerListener {
                    goodsDetailsImgDialog?.show()
                    goodsDetailsImgDialog?.setPosition(it)
                }

            banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    tvIndicator.text = "${position + 1}/$bannerSize"
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })


            scrollerLayout.setOnVerticalScrollChangeListener { _, scrollY, _, _ ->

                val scroll = if (scrollY > height) height else scrollY
                val argb = Color.argb(
                    Math.round(scroll * 255f / height),
                    255,
                    255,
                    255
                )
                tvTitle.setTextColor(
                    Color.argb(
                        Math.round(scroll * 255f / height),
                        51,
                        51,
                        51
                    )
                )
                titleBar.setBackgroundColor(argb)
                clToolbar.setBackgroundColor(argb)
            }

            tvTakeOrder.onContinuousClick {
                if (goodsDetailsEntity == null) {
                    showToast("暂无数据")
                    return@onContinuousClick
                }

                PlaceOrderActivity.startActivity(
                    this@GoodsDetailsActivity,
                    goodsId = goodsId,
                    goodsDetailsEntity = goodsDetailsEntity!!
                )
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {

    }

    class ImageAdapter : BaseViewBindingAdapter<PicDto, ItemGoodsDetailsImageBinding>(
        ItemGoodsDetailsImageBinding::inflate,
        R.layout.item_goods_details_image
    ) {
        override fun convert(
            holder: BaseViewBindingHolder<ItemGoodsDetailsImageBinding>,
            item: PicDto
        ) {
            holder.viewBind.ivDetails.loadImage(item.picUrl)
        }
    }

    private var bannerSize = 0
    private var goodsDetailsEntity: GoodsDetailsEntity? = null

    override fun createObserver() {
        mViewModel.goodsDetailsEntity.observe(this) {
            it?.apply {
                goodsDetailsEntity = it
                bannerSize = bannerPicDtoList?.size ?: 0
                mViewBind.tvIndicator.text = "1/${bannerPicDtoList?.size ?: ""}"
                mViewBind.banner.setImages(bannerPicDtoList).start()
                goodsDetailsImgDialog = GoodsDetailsImgDialog(
                    context = this@GoodsDetailsActivity,
                    imageUrl = goodsDetailPicDtoList
                )
                mViewBind.imageRlv.adapter =
                    ImageAdapter().apply { setNewInstance(goodsDetailPicDtoList) }

                mViewBind.tvGoodsName.text = goodsName
                mViewBind.tvDes.text = goodsDetails
                mViewBind.tvDeliveryMode.text = if (deliveryMode == 2) "自提" else "快递"
                mViewBind.tvNum.text = if (buyEmption == -1) "不限" else "每人${buyEmption}件"
                mViewBind.tvPoints.text = goodsScorePrice.toString()
                mViewBind.tvMoney.text = goodsMoneyPrice.doubleToTwoDecimalPlaceString()
            }
        }
    }
}