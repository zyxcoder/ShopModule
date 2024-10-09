package com.zkxy.shop.ui.category

import androidx.lifecycle.MutableLiveData
import com.gxy.common.common.loadsir.LoadContentStatus
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

    val loadContentStatus = MutableLiveData<LoadContentStatus>()

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
            loadContentStatus.value = LoadContentStatus.DEFAULT_LOADING
            val apiResult = apiService.getGoodsCategory().apiData().filter {
                it.children?.isNotEmpty() == true
            }.toMutableList()
            //默认选中第一个
            apiResult.firstOrNull()?.isSelect = true
            categoryDataList.value = apiResult
            if (apiResult.isNotEmpty()) {
                loadContentStatus.value = LoadContentStatus.SUCCESS
            } else {
                loadContentStatus.value = LoadContentStatus.DEFAULT_EMPTY
            }
        }, error = {
            loadContentStatus.value = LoadContentStatus.DEFAULT_ERROR
        })
    }

}