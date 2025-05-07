package com.zkxy.shop.ui.goods

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.gxy.common.base.BaseViewBindActivity
import com.gxy.common.common.loadsir.getLoadSir
import com.gxy.common.common.loadsir.setLoadContentStatus
import com.kingja.loadsir.core.LoadService
import com.zkxy.shop.R
import com.zkxy.shop.databinding.ActivityAllGoodsBinding
import com.zkxy.shop.databinding.ItemCategoryTabCenterTextBinding
import com.zkxy.shop.entity.category.GoodsCategoryEntity
import com.zkxy.shop.entity.goods.AllGoodsType
import com.zkxy.shop.entity.goods.RuleType
import com.zkxy.shop.entity.goods.SortRule
import com.zkxy.shop.entity.goods.goodsPointRuleList
import com.zkxy.shop.ui.category.CategoryActivity
import com.zkxy.shop.ui.goods.popup.GoodsPointPopup
import com.zkxy.shop.ui.home.adapter.GoodsAdapter
import com.zkxy.shop.ui.home.decoration.GoodsItemAverageMarginDecoration
import com.zkxy.shop.ui.search.SearchActivity
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.utils.dpToPx


/**
 * @author zhangyuxiang
 * @date 2024/9/25
 */

class AllGoodsActivity : BaseViewBindActivity<AllGoodsViewModel, ActivityAllGoodsBinding>() {

    private lateinit var mPageLoadService: LoadService<Any>
    private lateinit var mGoodsLoadService: LoadService<Any>
    private lateinit var goodsAdapter: GoodsAdapter


    //选中的商品类型：积分商品，现金商品
    private var currentAllGoodsType = AllGoodsType.AllGoodsPoint

    //选择的分类
    private var currentGoodsCategory: GoodsCategoryEntity? = null

    //默认选择无规则排序
    private var currentSortRule = SortRule.DEFAULT_SORT

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, AllGoodsActivity::class.java))
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        mViewBind.apply {
            //1.0版本先隐藏现金商品，只展示积分商品
            tabLayoutGoods.isVisible=true
            mPageLoadService = getLoadSir().register(clRoot) {
                mViewModel.fetchCategory()
            }
            mGoodsLoadService = getLoadSir().register(refreshLayout) {
                fetchGoodsData(isFirst = true, isRefresh = false)
            }
            toobarLayout.onRightIconClickListener = {
                SearchActivity.startActivity(context = this@AllGoodsActivity)
            }
            goodsAdapter = GoodsAdapter().apply {
                onGoodsItemClickListener = {
                    GoodsDetailsActivity.startActivity(
                        context = this@AllGoodsActivity,
                        goodsId = it.goodsId
                    )
                }
                rvGoods.adapter = this
            }
            rvGoods.apply {
                layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).also {
                        it.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                    }
                addItemDecoration(GoodsItemAverageMarginDecoration())
            }
            refreshLayout.apply {
                setOnRefreshListener {
                    fetchGoodsData(isFirst = false, isRefresh = true)
                }
                setOnLoadMoreListener {
                    fetchGoodsData(isFirst = false, isRefresh = false)
                }
            }
            ivCategory.onContinuousClick {
                CategoryActivity.startActivity(
                    context = this@AllGoodsActivity,
                    allGoodsType = currentAllGoodsType
                )
            }
            tabLayoutGoods.apply {
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        currentAllGoodsType = AllGoodsType.values()[tab?.position ?: 0]
                        //重置分类数据
                        tabLayoutCategory.selectTab(tabLayoutCategory.getTabAt(0))
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
                        currentGoodsCategory =
                            mViewModel.categoryDataList.value?.getOrNull(tab?.position ?: 0)
                        currentSortRule = SortRule.DEFAULT_SORT
                        refreshSortRuleAndFetchData()
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        updateCategoryTabView(tab, false)
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                        currentGoodsCategory =
                            mViewModel.categoryDataList.value?.getOrNull(tab?.position ?: 0)
                        currentSortRule = SortRule.DEFAULT_SORT
                        refreshSortRuleAndFetchData()
                    }
                })
            }
            tvDefaultSort.onContinuousClick {
                currentSortRule = SortRule.DEFAULT_SORT
                refreshSortRuleAndFetchData()
            }
            tvPriceSort.onContinuousClick {
                if (currentSortRule != SortRule.PRICE_SORT) {
                    currentSortRule = SortRule.PRICE_SORT.apply {
                        ruleType = RuleType.PRICE_DOWN_SORT
                    }
                } else {
                    if (currentSortRule.ruleType == RuleType.PRICE_DOWN_SORT) {
                        currentSortRule.ruleType = RuleType.PRICE_UP_SORT
                    } else {
                        currentSortRule.ruleType = RuleType.PRICE_DOWN_SORT
                    }
                }
                refreshSortRuleAndFetchData()
            }
            tvPointSort.onContinuousClick {
                GoodsPointPopup(context = this@AllGoodsActivity,
                    goodsPointEntitys = goodsPointRuleList.onEach {
                        it.isSelect = it.ruleType == currentSortRule.ruleType
                    }).apply {
                    onPointSelectListener = {
                        currentSortRule = SortRule.POINT_SORT.apply {
                            ruleType = it.ruleType
                        }
                        refreshSortRuleAndFetchData()
                    }
                }.showPopupWindow(clSortRule)
            }
        }
        mViewModel.fetchCategory()
    }

    /**
     * 请求商品数据
     */
    private fun fetchGoodsData(isFirst: Boolean, isRefresh: Boolean) {
        mViewModel.getGoodsData(
            isFirst = isFirst,
            isRefresh = isRefresh,
            start = if (!isFirst && !isRefresh) mViewBind.rvGoods.adapter?.itemCount ?: 0 else 0,
            selectGoodsCategory = currentGoodsCategory,
            selectAllGoodsType = currentAllGoodsType,
            selectSortRule = currentSortRule
        )
    }

    private fun updateCategoryTabView(tab: TabLayout.Tab?, isSelect: Boolean) {
        tab?.customView?.apply {
            findViewById<TextView>(R.id.tvTabText).apply {
                setTextColor(
                    ContextCompat.getColor(
                        context, if (isSelect) R.color.clolor_566beb else R.color.clolor_666666
                    )
                )
                paint.isFakeBoldText = isSelect
            }
        }
    }

    /**
     * 刷新排序规则UI，并且请求商品接口
     */
    private fun refreshSortRuleAndFetchData() {
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
            //先初始化所有排序规则的默认UI
            arrayListOf(tvDefaultSort, tvPriceSort, tvPointSort).forEach {
                it.setTextColor(
                    ContextCompat.getColor(
                        this@AllGoodsActivity, R.color.clolor_666666
                    )
                )
            }
            arrayListOf(tvPriceSort, tvPointSort).forEach {
                it.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, ContextCompat.getDrawable(
                        this@AllGoodsActivity, R.drawable.ic_sort_down_unselect
                    ), null
                )
            }
            tvPointSort.text = "积分区间"
            when (currentSortRule) {
                SortRule.DEFAULT_SORT -> {
                    tvDefaultSort.setTextColor(
                        ContextCompat.getColor(
                            this@AllGoodsActivity, R.color.clolor_566beb
                        )
                    )
                }

                SortRule.PRICE_SORT -> {
                    tvPriceSort.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                this@AllGoodsActivity, R.color.clolor_566beb
                            )
                        )
                        setCompoundDrawablesWithIntrinsicBounds(
                            null, null, ContextCompat.getDrawable(
                                this@AllGoodsActivity,
                                if (currentSortRule.ruleType == RuleType.PRICE_DOWN_SORT) {
                                    R.drawable.ic_sort_down_select
                                } else {
                                    R.drawable.ic_sort_up_select
                                }
                            ), null
                        )
                    }
                }

                SortRule.POINT_SORT -> {
                    tvPointSort.apply {
                        text = currentSortRule.ruleType?.content
                        setTextColor(
                            ContextCompat.getColor(
                                this@AllGoodsActivity, R.color.clolor_566beb
                            )
                        )
                        setCompoundDrawablesWithIntrinsicBounds(
                            null, null, ContextCompat.getDrawable(
                                this@AllGoodsActivity, R.drawable.ic_sort_down_select
                            ), null
                        )
                    }
                }
            }
        }
        fetchGoodsData(isFirst = true, isRefresh = false)
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
                                    tvTabText.text = it.name
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
                mViewBind.rvGoods.postDelayed({
                    mViewBind.rvGoods.scrollToPosition(0)
                }, 100)

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