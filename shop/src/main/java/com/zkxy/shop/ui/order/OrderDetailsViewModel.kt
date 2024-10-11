package com.zkxy.shop.ui.order

import androidx.lifecycle.MutableLiveData
import com.zkxy.shop.entity.order.OrderDetailsEntity
import com.zkxy.shop.network.request.apiService
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job

class OrderDetailsViewModel : BaseViewModel() {

    val orderDetailsEntity = MutableLiveData<OrderDetailsEntity>()

    fun orderDetails(orderId: Int?) {
        request<Job>(block = {
            loadingChange.showDialog.value = ""
            orderDetailsEntity.value = apiService.orderDetails(orderId = orderId).apiData()
            loadingChange.dismissDialog.value = true
        }, error = {
            loadingChange.dismissDialog.value = true
        })
    }

}