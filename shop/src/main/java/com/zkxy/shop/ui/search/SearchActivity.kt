package com.zkxy.shop.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.gxy.common.base.BaseViewBindActivity
import com.zkxy.shop.databinding.ActivitySearchBinding
import com.zkxy.shop.entity.search.SearchWordHistoryEntity
import com.zkxy.shop.room.database.ShopDatabase
import com.zkxy.shop.ui.search.adapter.SearchHistoryWordAdapter

/**
 * @author zhangyuxiang
 * @date 2024/9/20
 */
class SearchActivity : BaseViewBindActivity<SearchViewModel, ActivitySearchBinding>() {


    private lateinit var searchHistoryWordAdapter: SearchHistoryWordAdapter

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, SearchActivity::class.java))
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        mViewBind.apply {
            searchHistoryWordAdapter = SearchHistoryWordAdapter().apply {
                rvSearchHistory.adapter = this
            }
            rvSearchHistory.layoutManager = FlexboxLayoutManager(this@SearchActivity).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            shopSearchView.onSearchClickListener = {
//                ShopDatabase.getInstance().searchHistoryDao().insertSearchWord(
//                    SearchWordHistoryEntity(
//                        searchWord = it,
//                        searchTime = System.currentTimeMillis()
//                    )
//                )
            }
        }
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.apply {
            searchWords.observe(this@SearchActivity) {
                searchHistoryWordAdapter.setNewInstance(it.toMutableList())
            }
        }
    }
}