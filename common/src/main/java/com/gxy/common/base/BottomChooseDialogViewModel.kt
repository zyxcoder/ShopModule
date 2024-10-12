package com.gxy.common.base

import androidx.lifecycle.MutableLiveData
import com.gxy.common.common.loadsir.LoadContentStatus
import com.gxy.common.entity.common.BottomListEntity
import com.gxy.common.entity.common.BottomRequestEntity
import com.gxy.common.ext.commonApiService
import com.gxy.common.ext.listConvertToBottomListEntity
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job

/**
 * @author zhangyuxiang
 * @date 2024/4/24
 */
class BottomChooseDialogViewModel : BaseViewModel() {

    val loadContentStatus = MutableLiveData<LoadContentStatus>()
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
                loadContentStatus.value = LoadContentStatus.DEFAULT_LOADING
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
                    loadContentStatus.value = LoadContentStatus.DEFAULT_EMPTY
                } else {
                    loadContentStatus.value = LoadContentStatus.SUCCESS
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