package com.zk.common.base

import androidx.lifecycle.MutableLiveData
import com.zk.common.common.loadsir.LoadZkContentStatus
import com.zk.common.entity.common.BottomListEntity
import com.zk.common.entity.common.BottomRequestEntity
import com.zk.common.ext.commonApiService
import com.zk.common.ext.listConvertToBottomListEntity
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job

/**
 * @author zhangyuxiang
 * @date 2024/4/24
 */
class BottomChooseDialogViewModel : BaseViewModel() {

    val loadZkContentStatus = MutableLiveData<LoadZkContentStatus>()
    val isRefreshing = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val dataHasMore = MutableLiveData<Boolean>()
    val bottomCommonOptionData = MutableLiveData<List<BottomListEntity>>()
    val bottomCommonOptionMoreData = MutableLiveData<List<BottomListEntity>>()

    /**
     * 每页默认30条数据
     */
    private val pageSize = 30

    fun fetchChooseList(
        isFirst: Boolean = true,
        isRefresh: Boolean,
        nameId: String?,
        checkId: Int?,
        bottomRequestEntity: BottomRequestEntity?
    ) {
        bottomRequestEntity?.searchVoList?.forEach {
            it.length = pageSize
        }
        request<Job>(block = {
            if (isFirst) {
                loadZkContentStatus.value = LoadZkContentStatus.DEFAULT_LOADING
            }
            if (isRefresh || isFirst) {
                isRefreshing.value = true
            } else {
                isLoading.value = true
            }
            val apiResult = commonApiService.getChooseList(bottomRequestEntity)
            val dataList =
                apiResult.apiData().getValueByKey(key = nameId)?.listConvertToBottomListEntity()

            dataHasMore.value = (dataList?.size ?: 0) >= pageSize
            //选中
            dataList?.findLast {
                it.id == checkId && checkId != null
            }?.isCheck = true
            if (isFirst) {
                if (dataList.isNullOrEmpty()) {
                    loadZkContentStatus.value = LoadZkContentStatus.DEFAULT_EMPTY
                } else {
                    loadZkContentStatus.value = LoadZkContentStatus.SUCCESS
                }
            }
            if (isRefresh || isFirst) {
                dataList?.let {
                    bottomCommonOptionData.value = it
                }
                isRefreshing.value = false
            } else {
                dataList?.let {
                    bottomCommonOptionMoreData.value = it
                }
                isLoading.value = false
            }
        }, error = {
            if (isFirst) {
                loadZkContentStatus.value = LoadZkContentStatus.DEFAULT_ERROR
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