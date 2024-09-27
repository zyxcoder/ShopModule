package com.zkxy.shop.ui.category

import androidx.lifecycle.MutableLiveData
import com.gxy.common.common.loadsir.LoadContentStatus
import com.zkxy.shop.entity.category.CategoryEntity
import com.zkxy.shop.entity.category.CategoryMinorEntity
import com.zkxy.shop.entity.category.CategorySecondaryEntity
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

/**
 * @author zhangyuxiang
 * @date 2024/9/24
 */
class CategoryViewModel : BaseViewModel() {

    val loadContentStatus = MutableLiveData<LoadContentStatus>()

    val categoryDataList = MutableLiveData<MutableList<CategoryEntity>>()


    /**
     * 重置分类选择
     */
    fun resetCategorySelect(selectCategoryEntity: CategoryEntity) {
        categoryDataList.value = categoryDataList.value?.apply {
            forEach {
                it.isSelect = it == selectCategoryEntity
            }
        }
    }

    fun fetchCategory() {
        request<Job>(block = {
            loadContentStatus.value = LoadContentStatus.DEFAULT_LOADING


            //todo 模拟网络请求
            delay(1000)
            val list = arrayListOf<CategoryEntity>()
            val categoryMinorList = arrayListOf<CategoryMinorEntity>()
            val categorySecondaryList = arrayListOf<CategorySecondaryEntity>()
            repeat(10) {
                categoryMinorList.add(
                    CategoryMinorEntity(
                        categoryId = it, categoryName = "进" + it
                    )
                )
            }
            repeat(10) {
                categorySecondaryList.add(
                    CategorySecondaryEntity(
                        categoryId = it,
                        categoryName = "日用" + it,
                        categoryMinorList = categoryMinorList
                    )
                )
            }

            repeat(10) {
                list.add(
                    CategoryEntity(
                        categoryId = it,
                        categoryName = "个护清洁数据" + it,
                        isSelect = it == 0,
                        categorySecondaryList = categorySecondaryList
                    )
                )
            }
            categoryDataList.value = list




            if ((categoryDataList.value?.size ?: 0) > 0) {
                loadContentStatus.value = LoadContentStatus.SUCCESS
            } else {
                loadContentStatus.value = LoadContentStatus.DEFAULT_EMPTY
            }
        }, error = {
            loadContentStatus.value = LoadContentStatus.DEFAULT_ERROR
        })
    }

}