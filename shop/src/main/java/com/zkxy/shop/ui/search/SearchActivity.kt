package com.zkxy.shop.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.gxy.common.base.BaseViewBindActivity
import com.gxy.common.common.dialog.CommonDialog
import com.gxy.common.common.loadsir.LoadContentStatus
import com.gxy.common.common.loadsir.getLoadSir
import com.gxy.common.common.loadsir.setLoadContentStatus
import com.kingja.loadsir.core.LoadService
import com.zkxy.shop.databinding.ActivitySearchBinding
import com.zkxy.shop.room.database.ShopDatabase
import com.zkxy.shop.ui.home.adapter.GoodsAdapter
import com.zkxy.shop.ui.search.adapter.SearchHistoryWordAdapter
import com.zyxcoder.mvvmroot.ext.onContinuousClick

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
                rvSearchHistory.adapter = this
            }
            goodsAdapter = GoodsAdapter().apply {
                onGoodsItemClickListener = {

                }
                rvGoods.adapter = this
            }
            rvSearchHistory.layoutManager = FlexboxLayoutManager(this@SearchActivity).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            refreshLayout.setOnLoadMoreListener {
                mViewModel.fetchSearchData(
                    isFirst = false,
                    start = rvGoods.adapter?.itemCount ?: 0,
                    searchWord = shopSearchView.getSearchContent()
                )
            }
            shopSearchView.requestFocus()
            shopSearchView.apply {
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
                CommonDialog(
                    context = this@SearchActivity, message = "确认删除全部搜索历史"
                ).apply {
                    onConfirmClickListener = {
                        ShopDatabase.getInstance().searchHistoryDao().deleteAllSearchWords()
                    }
                }.show()
            }
        }
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.apply {
            searchWords.observe(this@SearchActivity) { searchWordList ->
                arrayOf(mViewBind.tvSearchHistoryDesc, mViewBind.ivDelete).forEach {
                    it.isVisible = searchWordList.isNotEmpty()
                }
                searchHistoryWordAdapter.setNewInstance(searchWordList.toMutableList())
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
        }
    }
}