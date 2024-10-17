package com.zkxy.shop.ui.order

import androidx.lifecycle.MutableLiveData
import com.gxy.common.common.activitylist.BaseCommonListFragmentViewModel
import com.gxy.common.network.api.ApiResult
import com.zkxy.shop.entity.order.OrderListEntity
import com.zkxy.shop.network.request.apiService
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job

class OrderListFragmentViewModel : BaseCommonListFragmentViewModel<OrderListEntity>() {

    val payOrderSuccess = MutableLiveData<Boolean>()
    val cancelOrderSuccess = MutableLiveData<Boolean>()
    override var pageSize = 10

    //订单状态：1待发货; 2待提货; 3已发货; 4已提货; 5已取消,0 6 待支付
    override suspend fun getCommonList(
        start: Int,
        searchKey: String?,
        vararg params: Any?
    ): ApiResult<MutableList<OrderListEntity>> {
        val statusIds = when (params[0] as? Int) {
            0 -> mutableListOf()//全部
            1 -> mutableListOf(1, 2)//待发货,待提货
            2 -> mutableListOf(3, 4)//已发货,已提货
            3 -> mutableListOf(5)//已取消
            4 -> mutableListOf(0, 6)//待支付
            else -> mutableListOf()
        }

        return apiService.orderListAPP(
            current = start,
            key = searchKey,
            statusIds = mutableMapOf("statusIds" to statusIds),
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

    fun payment(orderCode: String?) {
        request<Job>(block = {
            loadingChange.showDialog.value = ""
            apiService.payment(orderCode = orderCode).apiNoData()
            payOrderSuccess.value = true
            loadingChange.dismissDialog.value = true
        }, error = {
            loadingChange.dismissDialog.value = true
        })
    }


}