package com.zkxy.shop.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gxy.common.base.BaseViewBindActivity
import com.gxy.common.common.loadsir.getLoadSir
import com.gxy.common.common.loadsir.setLoadContentStatus
import com.kingja.loadsir.core.LoadService
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.zkxy.shop.databinding.ActivityShopHomeBinding
import com.zkxy.shop.entity.home.HomeShopBannerEntity
import com.zkxy.shop.ui.home.adapter.GoodsAdapter
import com.zkxy.shop.ui.home.decoration.GoodsItemAverageMarginDecoration
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.utils.loadImage

/**
 * @author zhangyuxiang
 * @date 2024/9/19
 *
 * 商城首页
 */
class ShopHomeActivity : BaseViewBindActivity<ShopHomeViewModel, ActivityShopHomeBinding>() {

    private lateinit var mLoadService: LoadService<Any>
    private lateinit var topBannerAdapter: BannerImageAdapter<HomeShopBannerEntity>
    private lateinit var goodsAdapter: GoodsAdapter

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ShopHomeActivity::class.java))
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        mViewBind.apply {
            mLoadService = getLoadSir().register(refreshLayout) {
                startSearch(isFirst = true, isRefresh = false, start = 0)
            }
            topBannerAdapter = object : BannerImageAdapter<HomeShopBannerEntity>(arrayListOf()) {
                override fun onBindView(
                    holder: BannerImageHolder, data: HomeShopBannerEntity, position: Int, size: Int
                ) {
                    holder.imageView.loadImage(data.imageUrl ?: "")
                }
            }.apply {
                bannerHome.setAdapter(this)
                bannerHome.setIndicator(CircleIndicator(this@ShopHomeActivity))
                bannerHome.addBannerLifecycleObserver(this@ShopHomeActivity)
            }
            goodsAdapter = GoodsAdapter().apply {
                onGoodsItemClickListener = {

                }
                rvGoods.adapter = this
            }
            rvGoods.addItemDecoration(GoodsItemAverageMarginDecoration())
            refreshLayout.apply {
                setOnRefreshListener {
                    startSearch(isFirst = false, isRefresh = true, start = 0)
                }
                setOnLoadMoreListener {
                    startSearch(
                        isFirst = false, isRefresh = false, start = rvGoods.adapter?.itemCount ?: 0
                    )
                }
            }
            tvMyOrder.onContinuousClick {

            }
            tvAllShop.onContinuousClick {

            }
            toobarLayout.onRightIconClickListener = {

            }
        }
        startSearch(isFirst = true, isRefresh = false, start = 0)
    }

    /**
     * 开始搜索
     */
    private fun startSearch(isFirst: Boolean, isRefresh: Boolean, start: Int) {
        mViewModel.getData(
            isFirst = isFirst, isRefresh = isRefresh, start = start
        )
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.apply {
            loadContentStatus.observe(this@ShopHomeActivity) {
                mLoadService.setLoadContentStatus(it)
            }
            isRefreshing.observe(this@ShopHomeActivity) {
                if (!it) {
                    mViewBind.refreshLayout.finishRefresh()
                }
            }
            isLoading.observe(this@ShopHomeActivity) {
                if (!it) {
                    mViewBind.refreshLayout.finishLoadMore()
                }
            }
            firstGoodsDatas.observe(this@ShopHomeActivity) {
                goodsAdapter.setNewInstance(it)
            }
            moreGoodsDatas.observe(this@ShopHomeActivity) {
                goodsAdapter.addData(it)
            }
            topBannerDatas.observe(this@ShopHomeActivity) {
                topBannerAdapter.setDatas(it)
            }
            dataHasMore.observe(this@ShopHomeActivity) {
                mViewBind.refreshLayout.setNoMoreData(!it)
            }
        }
    }
}