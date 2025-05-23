package com.gxy.common.common.activitylist

import androidx.lifecycle.MutableLiveData
import com.gxy.common.common.loadsir.LoadContentStatus
import com.gxy.common.network.api.ApiResult
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job

/**
 * @author zhangyuxiang
 * @date 2024/3/21
 */
abstract class BaseMultipleListFragmentViewModel<ItemDataEntity> : BaseViewModel() {

    //页面每次请求的数据条数，可修改
    protected open var pageSize = 10

    val isRefreshing = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val firstDatas = MutableLiveData<MutableList<ItemDataEntity>>()
    val moreDatas = MutableLiveData<MutableList<ItemDataEntity>>()
    val loadContentStatus = MutableLiveData<LoadContentStatus>()
    val dataHasMore = MutableLiveData<Boolean>()
    val listCount = MutableLiveData<Int>()


    protected abstract suspend fun getCommonList(
        start: Int, length: Int, searchKey: String?, vararg params: Any?
    ): ApiResult<MutableList<ItemDataEntity>>


    /**
     * 获取列表数据
     * @param isFirst 是否是第一次请求
     * @param isRefresh 是否是下拉刷新
     * @param params 可变参数
     */
    fun getList(
        isFirst: Boolean,
        isRefresh: Boolean,
        start: Int,
        searchKey: String? = null,
        vararg params: Any?
    ) {
        request<Job>(block = {
            if (isFirst) {
                loadContentStatus.value = LoadContentStatus.DEFAULT_LOADING
            }
            if (isRefresh || isFirst) {
                isRefreshing.value = true
            } else {
                isLoading.value = true
            }
            val apiResult = getCommonList(
                start = start, length = pageSize, searchKey = searchKey, params = params
            )
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
            if (isRefresh || isFirst) {
                listCount.value = apiResult.recordsFiltered ?: 0
                firstDatas.value = dataList
                isRefreshing.value = false
            } else {
                moreDatas.value = dataList
                isLoading.value = false
            }
        }, error = {
            if (isFirst) {
                loadContentStatus.value = LoadContentStatus.DEFAULT_ERROR
            }
            if (isRefresh || isFirst) {
                listCount.value = 0
                isRefreshing.value = false
            } else {
                isLoading.value = false
            }
            dataHasMore.value = false
        })
    }
}