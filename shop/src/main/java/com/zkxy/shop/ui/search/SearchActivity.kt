package com.zkxy.shop.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.gxy.common.base.BaseViewBindActivity
import com.gxy.common.common.loadsir.LoadContentStatus
import com.gxy.common.common.loadsir.getLoadSir
import com.gxy.common.common.loadsir.setLoadContentStatus
import com.kingja.loadsir.core.LoadService
import com.zkxy.shop.common.dialog.ShopCommonDialog
import com.zkxy.shop.databinding.ActivitySearchBinding
import com.zkxy.shop.databinding.ViewSearchWordBinding
import com.zkxy.shop.entity.search.SearchWordHistoryEntity
import com.zkxy.shop.room.database.ShopDatabase
import com.zkxy.shop.ui.goods.GoodsDetailsActivity
import com.zkxy.shop.ui.home.adapter.GoodsAdapter
import com.zkxy.shop.ui.home.decoration.GoodsItemAverageMarginDecoration
import com.zkxy.shop.ui.search.adapter.SearchHistoryWordAdapter
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showSoftInput

/**
 * @author zhangyuxiang
 * @date 2024/9/20
 */
class SearchActivity : BaseViewBindActivity<SearchViewModel, ActivitySearchBinding>() {


    private lateinit var mLoadService: LoadService<Any>
    private lateinit var searchHistoryWordAdapter: SearchHistoryWordAdapter
    private lateinit var goodsAdapter: GoodsAdapter

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, SearchActivity::class.java))
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        mViewBind.apply {
            mLoadService = getLoadSir().register(refreshLayout) {
                mViewModel.fetchSearchData(
                    isFirst = true, start = 0, searchWord = shopSearchView.getSearchContent()
                )
            }
            searchHistoryWordAdapter = SearchHistoryWordAdapter().apply {
                onWorkClickListener = {
                    shopSearchView.setSearchContent(it.searchWord)
                    mViewModel.fetchSearchData(
                        isFirst = true, start = 0, searchWord = it.searchWord
                    )
                }
            }
            goodsAdapter = GoodsAdapter().apply {
                onGoodsItemClickListener = {
                    GoodsDetailsActivity.startActivity(
                        context = this@SearchActivity,
                        goodsId = it.goodsId
                    )
                }
                rvGoods.addItemDecoration(GoodsItemAverageMarginDecoration())
                rvGoods.adapter = this
            }
            refreshLayout.setOnLoadMoreListener {
                mViewModel.fetchSearchData(
                    isFirst = false,
                    start = rvGoods.adapter?.itemCount ?: 0,
                    searchWord = shopSearchView.getSearchContent()
                )
            }
            shopSearchView.apply {
                requestFocus()
                postDelayed({
                    showSoftInput()
                }, 500)
                onSearchClickListener = {
                    mViewModel.fetchSearchData(
                        isFirst = true, start = 0, searchWord = shopSearchView.getSearchContent()
                    )
                }
                onValueChangeListener = {
                    mViewBind.clSearchHistory.isVisible = true
                    mViewBind.clSearchResult.isVisible = false
                }
            }
            ivDelete.onContinuousClick {
                //删除搜索词历史记录
                ShopCommonDialog(
                    context = this@SearchActivity, message = "确认删除全部搜索历史"
                ).apply {
                    onConfirmClickListener = {
                        ShopDatabase.getInstance().searchHistoryDao().deleteAllSearchWords()
                    }
                }.show()
            }
        }
    }

    /**
     * 生成搜索词标签View
     */
    private fun generateSearchWordView(searchWordHistoryEntity: SearchWordHistoryEntity): View {
        return ViewSearchWordBinding.inflate(layoutInflater, mViewBind.flSearchHistory, false)
            .apply {
                tvWord.text = searchWordHistoryEntity.searchWord
            }.root.apply {
                onContinuousClick {
                    mViewBind.shopSearchView.setSearchContent(searchWordHistoryEntity.searchWord)
                    mViewModel.fetchSearchData(
                        isFirst = true, start = 0, searchWord = searchWordHistoryEntity.searchWord
                    )
                }
            }
    }

    @SuppressLint("SetTextI18n")
    override fun createObserver() {
        super.createObserver()
        mViewModel.apply {
            searchWords.observe(this@SearchActivity) { searchWordList ->
                arrayOf(
                    mViewBind.tvSearchHistoryDesc, mViewBind.ivDelete, mViewBind.scrollSearchHistory
                ).forEach {
                    it.isVisible = searchWordList.isNotEmpty()
                }
                mViewBind.flSearchHistory.removeAllViews()
                repeat(searchWordList.size) {
                    mViewBind.flSearchHistory.addView(generateSearchWordView(searchWordList[it]))
                }
            }
            loadContentStatus.observe(this@SearchActivity) {
                mLoadService.setLoadContentStatus(it)
                mViewBind.clSearchHistory.isVisible = false
                mViewBind.clSearchResult.isVisible = true
                mViewBind.tvResultCount.isVisible = it == LoadContentStatus.SUCCESS
            }
            isRefreshing.observe(this@SearchActivity) {
                if (!it) {
                    mViewBind.refreshLayout.finishRefresh()
                }
            }
            isLoading.observe(this@SearchActivity) {
                if (!it) {
                    mViewBind.refreshLayout.finishLoadMore()
                }
            }
            firstGoodsDatas.observe(this@SearchActivity) {
                goodsAdapter.setNewInstance(it)
            }
            moreGoodsDatas.observe(this@SearchActivity) {
                goodsAdapter.addData(it)
            }
            dataHasMore.observe(this@SearchActivity) {
                mViewBind.refreshLayout.setNoMoreData(!it)
            }
            resultCount.observe(this@SearchActivity) {
                mViewBind.tvResultCount.text = "共 $it 个结果"
            }
        }
    }
}