package com.zkxy.shop.ui.category

import androidx.lifecycle.MutableLiveData
import com.zk.common.common.loadsir.LoadZkContentStatus
import com.zkxy.shop.entity.category.GoodsCategoryEntity
import com.zkxy.shop.network.request.apiService
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job

/**
 * @author zhangyuxiang
 * @date 2024/9/24
 */
class CategoryViewModel : BaseViewModel() {

    val loadZkContentStatus = MutableLiveData<LoadZkContentStatus>()

    val categoryDataList = MutableLiveData<MutableList<GoodsCategoryEntity>>()


    /**
     * 重置分类选择
     */
    fun resetCategorySelect(selectCategoryEntity: GoodsCategoryEntity) {
        categoryDataList.value = categoryDataList.value?.onEach {
            it.isSelect = it == selectCategoryEntity
        }
    }

    fun fetchCategory() {
        request<Job>(block = {
            loadZkContentStatus.value = LoadZkContentStatus.DEFAULT_LOADING
            val apiResult = apiService.getGoodsCategory().apiData().filter {
                it.children?.isNotEmpty() == true
            }.toMutableList()
            //默认选中第一个
            apiResult.firstOrNull()?.isSelect = true
            categoryDataList.value = apiResult
            if (apiResult.isNotEmpty()) {
                loadZkContentStatus.value = LoadZkContentStatus.SUCCESS
            } else {
                loadZkContentStatus.value = LoadZkContentStatus.DEFAULT_EMPTY
            }
        }, error = {
            loadZkContentStatus.value = LoadZkContentStatus.DEFAULT_ERROR
        })
    }

}