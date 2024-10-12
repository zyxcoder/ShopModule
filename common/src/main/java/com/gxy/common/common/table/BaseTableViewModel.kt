package com.gxy.common.common.table

import androidx.lifecycle.MutableLiveData
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job

/**
 * @author zhangyuxiang
 * @date 2024/4/11
 */
abstract class BaseTableViewModel : BaseViewModel() {

    val updateSuccess = MutableLiveData<Boolean>()
    val insertSuccess = MutableLiveData<Boolean>()

    /**
     * 其他操作
     */
    protected abstract suspend fun otherOperate(vararg params: Any?)


    protected abstract suspend fun updateTableData(mapParams: Map<String?, @JvmSuppressWildcards Any?>?)

    protected abstract suspend fun insertTableData(mapParams: Map<String?, @JvmSuppressWildcards Any?>?)

    fun fetchTableData(vararg params: Any?) {
        request<Job>(block = {
            loadingChange.showDialog.value = "加载中..."
            otherOperate(*params)
            loadingChange.dismissDialog.value = true
        }, error = {
            loadingChange.dismissDialog.value = true
        })
    }


    fun updateUserData(
        mapParams: Map<String?, @JvmSuppressWildcards Any?>?
    ) {
        request<Job>(block = {
            loadingChange.showDialog.value = "提交中..."
            val map = mutableMapOf<String?, Any?>()
            mapParams?.forEach {
                map[it.key] = it.value ?: ""
            }
            updateTableData(map)
            updateSuccess.value = true
            loadingChange.dismissDialog.value = true
        }, error = {
            updateSuccess.value = false
            loadingChange.dismissDialog.value = true
        })
    }

    fun insertUserData(mapParams: Map<String?, @JvmSuppressWildcards Any?>?) {
        request<Job>(block = {
            loadingChange.showDialog.value = "提交中..."
            val map = mutableMapOf<String?, Any?>()
            mapParams?.forEach {
                map[it.key] = it.value ?: ""
            }
            insertTableData(map)
            insertSuccess.value = true
            loadingChange.dismissDialog.value = true
        }, error = {
            insertSuccess.value = false
            loadingChange.dismissDialog.value = true
        })
    }
}