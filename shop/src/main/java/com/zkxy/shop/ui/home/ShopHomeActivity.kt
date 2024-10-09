package com.zkxy.shop.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.gxy.common.base.BaseViewBindActivity
import com.gxy.common.common.loadsir.LoadContentStatus
import com.gxy.common.common.loadsir.getLoadSir
import com.gxy.common.common.loadsir.setLoadContentStatus
import com.kingja.loadsir.core.LoadService
import com.zkxy.shop.databinding.ActivityShopHomeBinding
import com.zkxy.shop.isInit
import com.zkxy.shop.ui.goods.AllGoodsActivity
import com.zkxy.shop.ui.goods.GoodsDetailsActivity
import com.zkxy.shop.ui.home.adapter.GoodsAdapter
import com.zkxy.shop.ui.home.decoration.GoodsItemAverageMarginDecoration
import com.zkxy.shop.ui.search.SearchActivity
import com.zkxy.shop.utils.HomeBannerImageLoader
import com.zkxy.shop.web.ShopWebViewActivity
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast

/**
 * @author zhangyuxiang
 * @date 2024/9/19
 *
 * 商城首页
 */
class ShopHomeActivity : BaseViewBindActivity<ShopHomeViewModel, ActivityShopHomeBinding>() {

    private lateinit var mLoadService: LoadService<Any>

    private lateinit var goodsAdapter: GoodsAdapter

    companion object {
        fun startActivity(context: Context) {
            if (!isInit) {
                context.showToast("请先调用shopInit方法以初始化插件")
                return
            }
            context.startActivity(Intent(context, ShopHomeActivity::class.java))
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        mViewBind.apply {
            mLoadService = getLoadSir().register(viewLoad) {
                startSearch(isFirst = true, isRefresh = false, start = 0)
            }
            bannerHome.setImageLoader(HomeBannerImageLoader()).setOnBannerListener {
                mViewModel.topBannerDatas.value?.getOrNull(it)?.let { clickBannerEntity ->
                    //urlType 链接类型:1商品，2自定义内容，3无链接
                    when (clickBannerEntity.urlType) {
                        1 -> {
                            GoodsDetailsActivity.startActivity(
                                context = this@ShopHomeActivity, goodsId = clickBannerEntity.goodsId
                            )
                        }

                        2 -> {
                            ShopWebViewActivity.startActivity(
                                context = this@ShopHomeActivity,
                                title = clickBannerEntity.noticeName,
                                loadUrl = clickBannerEntity.noticeUrl ?: ""
                            )
                        }
                    }
                }
            }
            goodsAdapter = GoodsAdapter().apply {
                onGoodsItemClickListener = {
                    GoodsDetailsActivity.startActivity(
                        context = this@ShopHomeActivity, goodsId = it.goodsId
                    )
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
                AllGoodsActivity.startActivity(context = this@ShopHomeActivity)
            }
            toobarLayout.onRightIconClickListener = {
                SearchActivity.startActivity(context = this@ShopHomeActivity)
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
        //更新LoadService显示区域
        mViewBind.clLoad.post {
            val visibleHeight = Rect().apply {
                mViewBind.clLoad.getGlobalVisibleRect(this)
            }.height()
            mViewBind.clLoad.updateLayoutParams {
                height = visibleHeight
            }
        }
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.apply {
            loadContentStatus.observe(this@ShopHomeActivity) {
                mViewBind.clLoad.isVisible = it != LoadContentStatus.SUCCESS
                mViewBind.rvGoods.isVisible = it == LoadContentStatus.SUCCESS
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
                mViewBind.bannerHome.setImages(it).start()
            }
            dataHasMore.observe(this@ShopHomeActivity) {
                mViewBind.refreshLayout.setNoMoreData(!it)
            }
        }
    }
}