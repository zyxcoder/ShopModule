package com.zkxy.shop.ui.search

import androidx.lifecycle.MutableLiveData
import com.gxy.common.common.loadsir.LoadContentStatus
import com.zkxy.shop.entity.home.GoodsEntity
import com.zkxy.shop.entity.search.SearchWordHistoryEntity
import com.zkxy.shop.network.request.apiService
import com.zkxy.shop.room.database.ShopDatabase
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job

/**
 * @author zhangyuxiang
 * @date 2024/9/20
 */
class SearchViewModel : BaseViewModel() {

    //页面每次请求的数据条数，可修改
    private var pageSize = 10

    val isRefreshing = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val firstGoodsDatas = MutableLiveData<MutableList<GoodsEntity>>()
    val moreGoodsDatas = MutableLiveData<MutableList<GoodsEntity>>()
    val loadContentStatus = MutableLiveData<LoadContentStatus>()
    val dataHasMore = MutableLiveData<Boolean>()
    val resultCount = MutableLiveData<Int>()


    val searchWords = ShopDatabase.getInstance().searchHistoryDao().getAllSearchWords()


    /**
     * 获取列表数据
     * @param isFirst 是否是第一次请求
     */
    fun fetchSearchData(
        isFirst: Boolean, start: Int, searchWord: String
    ) {
        request<Job>(block = {
            if (isFirst) {
                loadContentStatus.value = LoadContentStatus.DEFAULT_LOADING
            }
            if (isFirst) {
                isRefreshing.value = true
            } else {
                isLoading.value = true
            }

            val apiResult = apiService.searchGoods(
                currentPage = start / pageSize + 1, pageSize = pageSize, goodsName = searchWord
            )
            resultCount.value = apiResult.total ?: 0

            val dataList = apiResult.apiData()
            //当有更多数据时，后端返回的data的大小是大于等于pageSize
            dataHasMore.value = dataList.size >= pageSize
            if (isFirst) {
                if (dataList.isEmpty()) {
                    loadContentStatus.value = LoadContentStatus.DEFAULT_EMPTY
                } else {
                    loadContentStatus.value = LoadContentStatus.SUCCESS
                }
            }
            if (isFirst) {
                firstGoodsDatas.value = dataList
                isRefreshing.value = false
            } else {
                moreGoodsDatas.value = dataList
                isLoading.value = false
            }
            //更新搜索词记录
            insertSearchWordHistory(word = searchWord)
        }, error = {
            if (isFirst) {
                loadContentStatus.value = LoadContentStatus.DEFAULT_ERROR
            }
            if (isFirst) {
                isRefreshing.value = false
            } else {
                isLoading.value = false
            }
            dataHasMore.value = false
            //更新搜索词记录
            insertSearchWordHistory(word = searchWord)
        })
    }


    /**
     * 插入搜索历史
     * @param word 搜索词
     */
    private fun insertSearchWordHistory(word: String) {
        request<Job>(block = {
            ShopDatabase.getInstance().searchHistoryDao().let {
                val searchWordHistoryEntity = it.getSearchWordHistory(word = word)
                if (searchWordHistoryEntity != null) {
                    //表中已有这条数据,那么更新搜索时间
                    searchWordHistoryEntity.searchTime = System.currentTimeMillis()
                    it.updateSearchWordHistory(searchWordHistoryEntity)
                } else {
                    it.insertSearchWord(
                        SearchWordHistoryEntity(
                            searchWord = word, searchTime = System.currentTimeMillis()
                        )
                    )
                }
            }
        }, showErrorToast = false)
    }
}