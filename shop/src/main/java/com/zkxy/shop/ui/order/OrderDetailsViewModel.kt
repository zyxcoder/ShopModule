package com.zkxy.shop.ui.order

import androidx.lifecycle.MutableLiveData
import com.zkxy.shop.entity.goods.PlaceOrderEntity
import com.zkxy.shop.entity.order.OrderDetailsEntity
import com.zkxy.shop.network.request.apiService
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job

class OrderDetailsViewModel : BaseViewModel() {

    val orderDetailsEntity = MutableLiveData<OrderDetailsEntity>()
    val placeOrderEntity = MutableLiveData<PlaceOrderEntity>()
    fun orderDetails(orderId: Int?) {
        request<Job>(block = {
            orderDetailsEntity.value = apiService.orderDetails(orderId = orderId).apiData()
        }, error = {
        })
    }

    fun goodsStockAddressSearch(goodsId: Int, deliveryMode: Int) {
        request<Job>(block = {
            loadingChange.showDialog.value = ""
            placeOrderEntity.value =
                apiService.goodsStockAddressSearch(goodsId = goodsId, deliveryMode = deliveryMode)
                    .apiData()
            loadingChange.dismissDialog.value = true
        }, error = {
            loadingChange.dismissDialog.value = true
        })
    }

}