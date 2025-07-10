package com.zkxy.shop.ui.order

import androidx.lifecycle.MutableLiveData
import com.gxy.common.common.activitylist.BaseCommonListFragmentViewModel
import com.gxy.common.network.api.ApiResult
import com.zkxy.shop.entity.order.OrderListEntity
import com.zkxy.shop.network.request.apiService
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job

class AfterSalesFragmentViewModel : BaseCommonListFragmentViewModel<OrderListEntity>() {

    val cancelOrderSuccess = MutableLiveData<Boolean>()
    override var pageSize = 10

    //售后列表
    override suspend fun getCommonList(
        start: Int,
        searchKey: String?,
        vararg params: Any?
    ): ApiResult<MutableList<OrderListEntity>> {
        return apiService.orderAfterSalesList(
            current = start,
            key = searchKey,
            size = pageSize
        )
    }

    fun orderCancel(orderId: Int?) {
        request<Job>(block = {
            loadingChange.showDialog.value = ""
            apiService.orderCancel(orderId = orderId).apiNoData()
            cancelOrderSuccess.value = true
            loadingChange.dismissDialog.value = true
        }, error = {
            loadingChange.dismissDialog.value = true
        })
    }

    fun orderAfterSalesRevoke(orderId: Int?, saleId: Int?) {
        request<Job>(block = {
            loadingChange.showDialog.value = ""
            apiService.orderAfterSalesRevoke(orderId = orderId, saleId = saleId).apiNoData()
            cancelOrderSuccess.value = true
            loadingChange.dismissDialog.value = true
        }, error = {
            loadingChange.dismissDialog.value = true
        })
    }


}