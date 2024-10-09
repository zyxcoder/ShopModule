package com.zkxy.shop.ui.home

import androidx.lifecycle.MutableLiveData
import com.gxy.common.common.loadsir.LoadContentStatus
import com.zkxy.shop.entity.home.GoodsEntity
import com.zkxy.shop.entity.home.HomeShopBannerEntity
import com.zkxy.shop.network.request.apiService
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job

/**
 * @author zhangyuxiang
 * @date 2024/9/19
 */
class ShopHomeViewModel : BaseViewModel() {

    //页面每次请求的数据条数，可修改
    private var pageSize = 10

    val isRefreshing = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val topBannerDatas = MutableLiveData<MutableList<HomeShopBannerEntity>>()
    val firstGoodsDatas = MutableLiveData<MutableList<GoodsEntity>>()
    val moreGoodsDatas = MutableLiveData<MutableList<GoodsEntity>>()
    val loadContentStatus = MutableLiveData<LoadContentStatus>()
    val dataHasMore = MutableLiveData<Boolean>()

    /**
     * 获取列表数据
     * @param isFirst 是否是第一次请求
     * @param isRefresh 是否是下拉刷新
     */
    fun getData(
        isFirst: Boolean,
        isRefresh: Boolean,
        start: Int
    ) {
        request<Job>(block = {
            if (isFirst) {
                loadContentStatus.value = LoadContentStatus.DEFAULT_LOADING
            }
            if (isRefresh || isFirst) {
                isRefreshing.value = true
                topBannerDatas.value = apiService.getHomeBanner().apiData()
            } else {
                isLoading.value = true
            }

            val apiResult = apiService.searchGoods(
                goodsSuggest = 1,
                currentPage = start / pageSize + 1,
                pageSize = pageSize
            )
            val dataList = apiResult.apiData()
            //当有更多数据时，后端返回的data的大小是大于等于pageSize
            dataHasMore.value = dataList.size >= pageSize
            if (isFirst || isRefresh) {
                if (dataList.isEmpty()) {
                    loadContentStatus.value = LoadContentStatus.DEFAULT_EMPTY
                } else {
                    loadContentStatus.value = LoadContentStatus.SUCCESS
                }
            }
            if (isRefresh || isFirst) {
                firstGoodsDatas.value = dataList
                isRefreshing.value = false
            } else {
                moreGoodsDatas.value = dataList
                isLoading.value = false
            }
        }, error = {
            if (isFirst || isRefresh) {
                loadContentStatus.value = LoadContentStatus.DEFAULT_ERROR
            }
            if (isRefresh || isFirst) {
                isRefreshing.value = false
            } else {
                isLoading.value = false
            }
            dataHasMore.value = false
        })
    }

}