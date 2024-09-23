package com.zkxy.shop.ui.goods

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.gxy.common.base.BaseViewBindActivity
import com.gyf.immersionbar.ImmersionBar
import com.zkxy.shop.R
import com.zkxy.shop.databinding.ActivityGoodsDetailsBinding
import com.zkxy.shop.databinding.ItemGoodsDetailsImageBinding
import com.zkxy.shop.entity.home.HomeShopBannerEntity
import com.zkxy.shop.ui.goods.dialog.GoodsDetailsImgDialog
import com.zkxy.shop.utils.GlideImageLoader
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.utils.dpToPx
import com.zyxcoder.mvvmroot.utils.loadImage

class GoodsDetailsActivity :
    BaseViewBindActivity<GoodsDetailsViewModel, ActivityGoodsDetailsBinding>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, GoodsDetailsActivity::class.java))
        }
    }

    private val images = mutableListOf(
        HomeShopBannerEntity("https://gd-hbimg.huaban.com/4bd2502a1859e4bcc9d0afeda5b8851d98a267dd18c54-81OUAo_fw1200webp"),
        HomeShopBannerEntity("https://gd-hbimg.huaban.com/efd67641fbefe040be67e09709ecc06f7721a1751338e-dUZfor_fw1200webp"),
        HomeShopBannerEntity("https://gd-hbimg.huaban.com/bbbaf5b863d654a241df97b4f1135b3af770b5b95fe9f-p1uGUi_fw1200webp"),
        HomeShopBannerEntity("https://gd-hbimg.huaban.com/8fab765e0b0e7403e95b5c9bc439157b10a322e1119b9-aWLIOx_fw1200webp")
    )

    override fun initView(savedInstanceState: Bundle?) {

        val height = dpToPx(150f).toInt()
        val goodsDetailsImgDialog = GoodsDetailsImgDialog(context = this, imageUrl = images)

        mViewBind.apply {
            ImmersionBar.with(this@GoodsDetailsActivity).statusBarDarkFont(true)
                .statusBarView(titleBar).init()
            ivBack.onContinuousClick { finish() }
            tvIndicator.text = "1/${images.size}"
            banner.setImageLoader(GlideImageLoader())
                .setOnBannerListener {
                    goodsDetailsImgDialog.show()
                    goodsDetailsImgDialog.setPosition(it)
                }
                .setImages(images).start()
            banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    tvIndicator.text = "${position + 1}/${images.size}"
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })

            imageRlv.adapter = ImageAdapter().apply { setList(images) }
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

            tvTakeOrder.onContinuousClick { PlaceOrderActivity.startActivity(this@GoodsDetailsActivity) }
        }

    }

    override fun init(savedInstanceState: Bundle?) {
    }

    class ImageAdapter : BaseViewBindingAdapter<HomeShopBannerEntity, ItemGoodsDetailsImageBinding>(
        ItemGoodsDetailsImageBinding::inflate,
        R.layout.item_goods_details_image
    ) {
        override fun convert(
            holder: BaseViewBindingHolder<ItemGoodsDetailsImageBinding>,
            item: HomeShopBannerEntity
        ) {
            holder.viewBind.ivDetails.loadImage(item.imageUrl)
        }
    }
}