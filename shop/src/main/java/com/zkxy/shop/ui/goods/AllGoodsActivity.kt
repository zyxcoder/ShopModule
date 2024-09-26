package com.zkxy.shop.ui.goods

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.google.android.material.tabs.TabLayout
import com.gxy.common.base.BaseViewBindActivity
import com.gxy.common.common.loadsir.getLoadSir
import com.gxy.common.common.loadsir.setLoadContentStatus
import com.kingja.loadsir.core.LoadService
import com.zkxy.shop.R
import com.zkxy.shop.databinding.ActivityAllGoodsBinding
import com.zkxy.shop.databinding.ItemCategoryTabCenterTextBinding
import com.zkxy.shop.ui.category.CategoryActivity
import com.zkxy.shop.ui.home.adapter.GoodsAdapter
import com.zkxy.shop.ui.home.decoration.GoodsItemAverageMarginDecoration
import com.zkxy.shop.ui.search.SearchActivity
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.utils.dpToPx


/**
 * @author zhangyuxiang
 * @date 2024/9/25
 */


enum class AllGoodsType(val title: String) {
    AllGoodsPoint("积分商品"), AllGoodsCash("现金商品")
}


class AllGoodsActivity : BaseViewBindActivity<AllGoodsViewModel, ActivityAllGoodsBinding>() {

    private lateinit var mPageLoadService: LoadService<Any>
    private lateinit var mGoodsLoadService: LoadService<Any>
    private lateinit var goodsAdapter: GoodsAdapter


    private var currentAllGoodsType = AllGoodsType.AllGoodsPoint


    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, AllGoodsActivity::class.java))
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        mViewBind.apply {
            mPageLoadService = getLoadSir().register(clRoot) {
                mViewModel.fetchCategory()
            }
            mGoodsLoadService = getLoadSir().register(refreshLayout) {
                mViewModel.getGoodsData(isFirst = true, isRefresh = false, start = 0)
            }
            toobarLayout.onRightIconClickListener = {
                SearchActivity.startActivity(context = this@AllGoodsActivity)
            }
            goodsAdapter = GoodsAdapter().apply {
                onGoodsItemClickListener = {
                    GoodsDetailsActivity.startActivity(this@AllGoodsActivity)
                }
                rvGoods.adapter = this
            }
            rvGoods.addItemDecoration(GoodsItemAverageMarginDecoration())
            refreshLayout.apply {
                setOnRefreshListener {
                    mViewModel.getGoodsData(isFirst = false, isRefresh = true, start = 0)
                }
                setOnLoadMoreListener {
                    mViewModel.getGoodsData(
                        isFirst = false, isRefresh = false, start = rvGoods.adapter?.itemCount ?: 0
                    )
                }
            }
            ivCategory.onContinuousClick {
                CategoryActivity.startActivity(context = this@AllGoodsActivity)
            }
            tabLayoutGoods.apply {
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        currentAllGoodsType = AllGoodsType.values()[tab?.position ?: 0]
                        refreshSortRuleUi()
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {

                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                })
                repeat(AllGoodsType.values().size) {
                    addTab(newTab().apply {
                        text = AllGoodsType.values()[it].title
                    })
                }
            }
            tabLayoutCategory.apply {
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        updateCategoryTabView(tab, true)
                        mViewModel.getGoodsData(isFirst = true, isRefresh = false, start = 0)
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        updateCategoryTabView(tab, false)
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                })
            }
        }
        mViewModel.fetchCategory()
    }

    @SuppressLint("ResourceAsColor")
    private fun updateCategoryTabView(tab: TabLayout.Tab?, isSelect: Boolean) {
        tab?.customView?.apply {
            val tvTabText = findViewById<TextView>(R.id.tvTabText)
            tvTabText.setTextColor(
                ContextCompat.getColor(
                    context, if (isSelect) R.color.clolor_566beb else R.color.clolor_666666
                )
            )
            tvTabText.paint.isFakeBoldText = isSelect
        }
    }

    private fun refreshSortRuleUi() {
        mViewBind.apply {
            clSortRule.updatePadding(
                left = if (currentAllGoodsType == AllGoodsType.AllGoodsPoint) dpToPx(
                    20f
                ).toInt() else dpToPx(80f).toInt(),
                right = if (currentAllGoodsType == AllGoodsType.AllGoodsPoint) dpToPx(
                    20f
                ).toInt() else dpToPx(80f).toInt()
            )
            tvPointSort.isVisible = currentAllGoodsType == AllGoodsType.AllGoodsPoint
        }
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.apply {
            loadCategoryContentStatus.observe(this@AllGoodsActivity) {
                mPageLoadService.setLoadContentStatus(it)
            }
            loadGoodsContentStatus.observe(this@AllGoodsActivity) {
                mGoodsLoadService.setLoadContentStatus(it)
            }
            categoryDataList.observe(this@AllGoodsActivity) {
                it.forEach {
                    mViewBind.tabLayoutCategory.apply {
                        addTab(newTab().apply {
                            customView =
                                ItemCategoryTabCenterTextBinding.inflate(layoutInflater).apply {
                                    tvTabText.text = it.categoryName
                                }.root
                        })
                    }
                }
            }
            isRefreshing.observe(this@AllGoodsActivity) {
                if (!it) {
                    mViewBind.refreshLayout.finishRefresh()
                }
            }
            isLoading.observe(this@AllGoodsActivity) {
                if (!it) {
                    mViewBind.refreshLayout.finishLoadMore()
                }
            }
            firstGoodsDatas.observe(this@AllGoodsActivity) {
                goodsAdapter.setNewInstance(it)
            }
            moreGoodsDatas.observe(this@AllGoodsActivity) {
                goodsAdapter.addData(it)
            }
            dataHasMore.observe(this@AllGoodsActivity) {
                mViewBind.refreshLayout.setNoMoreData(!it)
            }
        }
    }
}