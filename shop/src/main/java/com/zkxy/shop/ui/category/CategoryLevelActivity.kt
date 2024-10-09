package com.zkxy.shop.ui.category

import android.content.Context
import android.content.Intent
import android.os.Build
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
import com.zkxy.shop.databinding.ActivityCategoryLevelBinding
import com.zkxy.shop.databinding.ItemCategoryTabCenterTextBinding
import com.zkxy.shop.entity.category.GoodsCategoryEntity
import com.zkxy.shop.entity.goods.AllGoodsType
import com.zkxy.shop.entity.goods.RuleType
import com.zkxy.shop.entity.goods.SortRule
import com.zkxy.shop.entity.goods.goodsPointRuleList
import com.zkxy.shop.ui.goods.GoodsDetailsActivity
import com.zkxy.shop.ui.goods.popup.GoodsPointPopup
import com.zkxy.shop.ui.home.adapter.GoodsAdapter
import com.zkxy.shop.ui.home.decoration.GoodsItemAverageMarginDecoration
import com.zkxy.shop.ui.search.SearchActivity
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.utils.dpToPx

/**
 * @author zhangyuxiang
 * @date 2024/9/27
 */
class CategoryLevelActivity :
    BaseViewBindActivity<CategoryLevelViewModel, ActivityCategoryLevelBinding>() {

    private lateinit var mLoadService: LoadService<Any>
    private lateinit var goodsAdapter: GoodsAdapter

    //分类标签
    private var categoryDataList: MutableList<GoodsCategoryEntity>? = null

    //选中的商品类型：积分商品，现金商品
    private var currentAllGoodsType = AllGoodsType.AllGoodsPoint

    //选择的分类
    private var currentGoodsCategory: GoodsCategoryEntity? = null

    //默认选择无规则排序
    private var currentSortRule = SortRule.DEFAULT_SORT

    companion object {
        private const val CATEGORY_SECONDARY_DATA = "category_secondary_data"//二级分类数据
        private const val CATEGORY_MINOR_DATA = "category_minor_data"//三级分类数据
        private const val ALL_GOODS_TYPE = "all_goods_type"//商品类型
        fun startActivity(
            context: Context,
            categorySecondaryEntity: GoodsCategoryEntity?,
            categoryMinorEntity: GoodsCategoryEntity? = null,
            allGoodsType: AllGoodsType
        ) {
            context.startActivity(Intent(context, CategoryLevelActivity::class.java).apply {
                putExtra(CATEGORY_SECONDARY_DATA, categorySecondaryEntity)
                putExtra(CATEGORY_MINOR_DATA, categoryMinorEntity)
                putExtra(ALL_GOODS_TYPE, allGoodsType)
            })
        }
    }


    override fun init(savedInstanceState: Bundle?) {
        currentAllGoodsType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(ALL_GOODS_TYPE, AllGoodsType::class.java)
        } else {
            intent.getSerializableExtra(ALL_GOODS_TYPE) as? AllGoodsType
        } ?: AllGoodsType.AllGoodsPoint
        val categorySecondaryData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                CATEGORY_SECONDARY_DATA, GoodsCategoryEntity::class.java
            )
        } else {
            intent.getParcelableExtra(CATEGORY_SECONDARY_DATA)
        }
        categoryDataList = categorySecondaryData?.children?.toMutableList() ?: arrayListOf()
        //这里添加一个”全部“标签
        //todo levelType 具体值待定
        categoryDataList?.add(
            0, GoodsCategoryEntity(
                children = null,
                createTime = null,
                deleteFlag = null,
                levelType = 2,
                name = "全部",
                parentId = null,
                remark = null,
                sortNumber = null,
                typeId = null,
                updateTime = null,
                isSelect = null
            )
        )
        currentGoodsCategory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                CATEGORY_MINOR_DATA, GoodsCategoryEntity::class.java
            )
        } else {
            intent.getParcelableExtra(CATEGORY_MINOR_DATA)
        } ?: categoryDataList?.getOrNull(0)?.apply {
            isSelect = true
        }

        mViewBind.apply {
            mLoadService = getLoadSir().register(refreshLayout) {
                fetchGoodsData(isFirst = true, isRefresh = false)
            }
            toobarLayout.apply {
                setTitleContent(categorySecondaryData?.name)
                onRightIconClickListener = {
                    SearchActivity.startActivity(context = this@CategoryLevelActivity)
                }
            }
            goodsAdapter = GoodsAdapter().apply {
                onGoodsItemClickListener = {
                    GoodsDetailsActivity.startActivity(this@CategoryLevelActivity)
                }
                rvGoods.adapter = this
            }
            rvGoods.addItemDecoration(GoodsItemAverageMarginDecoration())
            refreshLayout.apply {
                setOnRefreshListener {
                    fetchGoodsData(isFirst = false, isRefresh = true)
                }
                setOnLoadMoreListener {
                    fetchGoodsData(isFirst = false, isRefresh = false)
                }
            }
            tabLayoutCategory.apply {
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        updateCategoryTabView(tab, true)
                        currentGoodsCategory = categoryDataList?.getOrNull(tab?.position ?: 0)
                        currentSortRule = SortRule.DEFAULT_SORT
                        refreshSortRuleAndFetchData()
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        updateCategoryTabView(tab, false)
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                        currentGoodsCategory = categoryDataList?.getOrNull(tab?.position ?: 0)
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
                GoodsPointPopup(context = this@CategoryLevelActivity,
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
            //渲染Tab分类标签
            categoryDataList?.forEach {
                tabLayoutCategory.addTab(tabLayoutCategory.newTab().apply {
                    customView = ItemCategoryTabCenterTextBinding.inflate(layoutInflater).apply {
                        tvTabText.text = it.name
                    }.root
                }, it == currentGoodsCategory)
            }
            //滑动到选中项
            tabLayoutCategory.post {
                tabLayoutCategory.setScrollPosition(tabLayoutCategory.selectedTabPosition, 0f, true)
            }
        }
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
                        this@CategoryLevelActivity, R.color.clolor_666666
                    )
                )
            }
            arrayListOf(tvPriceSort, tvPointSort).forEach {
                it.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, ContextCompat.getDrawable(
                        this@CategoryLevelActivity, R.drawable.ic_sort_down_unselect
                    ), null
                )
            }
            tvPointSort.text = "积分区间"
            when (currentSortRule) {
                SortRule.DEFAULT_SORT -> {
                    tvDefaultSort.setTextColor(
                        ContextCompat.getColor(
                            this@CategoryLevelActivity, R.color.clolor_566beb
                        )
                    )
                }

                SortRule.PRICE_SORT -> {
                    tvPriceSort.apply {
                        setTextColor(
                            ContextCompat.getColor(
                                this@CategoryLevelActivity, R.color.clolor_566beb
                            )
                        )
                        setCompoundDrawablesWithIntrinsicBounds(
                            null, null, ContextCompat.getDrawable(
                                this@CategoryLevelActivity,
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
                                this@CategoryLevelActivity, R.color.clolor_566beb
                            )
                        )
                        setCompoundDrawablesWithIntrinsicBounds(
                            null, null, ContextCompat.getDrawable(
                                this@CategoryLevelActivity, R.drawable.ic_sort_down_select
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
            loadContentStatus.observe(this@CategoryLevelActivity) {
                mLoadService.setLoadContentStatus(it)
            }
            isRefreshing.observe(this@CategoryLevelActivity) {
                if (!it) {
                    mViewBind.refreshLayout.finishRefresh()
                }
            }
            isLoading.observe(this@CategoryLevelActivity) {
                if (!it) {
                    mViewBind.refreshLayout.finishLoadMore()
                }
            }
            firstGoodsDatas.observe(this@CategoryLevelActivity) {
                goodsAdapter.setNewInstance(it)
            }
            moreGoodsDatas.observe(this@CategoryLevelActivity) {
                goodsAdapter.addData(it)
            }
            dataHasMore.observe(this@CategoryLevelActivity) {
                mViewBind.refreshLayout.setNoMoreData(!it)
            }
        }
    }
}