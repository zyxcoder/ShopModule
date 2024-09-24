package com.zkxy.shop.ui.search

import androidx.lifecycle.MutableLiveData
import com.gxy.common.common.loadsir.LoadContentStatus
import com.gxy.common.network.api.ApiResult
import com.zkxy.shop.entity.home.GoodsEntity
import com.zkxy.shop.entity.search.SearchWordHistoryEntity
import com.zkxy.shop.room.database.ShopDatabase
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

/**
 * @author zhangyuxiang
 * @date 2024/9/20
 */
class SearchViewModel : BaseViewModel() {

    //页面每次请求的数据条数，可修改
    var pageSize = 10

    val isRefreshing = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val firstGoodsDatas = MutableLiveData<MutableList<GoodsEntity>>()
    val moreGoodsDatas = MutableLiveData<MutableList<GoodsEntity>>()
    val loadContentStatus = MutableLiveData<LoadContentStatus>()
    val dataHasMore = MutableLiveData<Boolean>()


    val searchWords = ShopDatabase.getInstance().searchHistoryDao().getAllSearchWords()


    /**
     * 获取列表数据
     * @param isFirst 是否是第一次请求
     * @param isRefresh 是否是下拉刷新
     * @param params 可变参数
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
            //todo 获取列表数据
            delay(1000)
            val goodsList = mutableListOf<GoodsEntity>()
            repeat(10) {
                if (it % 2 == 0) {
                    goodsList.add(
                        GoodsEntity(
                            goodsName = "清风原木纯品金",
                            goodsUrl = "https://gd-hbimg.huaban.com/310d6a3729d1e1199f4aa07e275425f92e694eedf4678-JtZ2Oq_fw1200webp",
                            goodsInventory = 10,
                            goodsId = 1,
                            goodsPoint = 23.9,
                            goodsPrice = 38.9
                        )
                    )
                } else {
                    goodsList.add(
                        GoodsEntity(
                            goodsName = "清风原木纯品金装清风原木纯品金装系列抽取式纸巾150抽x3层清风原木纯品金装系列抽取式纸巾150抽x3层清风原木纯品金装系列抽取式纸巾150抽x3层系列抽取式纸巾150抽x3层 12包",
                            goodsUrl = "https://gd-hbimg.huaban.com/468d5c6d327b411c5041fdd4d9ddb7b6d9015bb2d6d9b-GBjrha_fw1200webp",
                            goodsInventory = 0,
                            goodsId = 1,
                            goodsPoint = null,
                            goodsPrice = 67.9
                        )
                    )
                }
            }
            val apiResult = ApiResult<MutableList<GoodsEntity>>(
                statusDesc = "qwqwwq",
                statusCode = "0",
                listCount = 10,
                hasMore = true,
                data = goodsList
            )
            //todo 获取列表数据


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
    fun insertSearchWordHistory(word: String) {
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