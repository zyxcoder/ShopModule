package com.zkxy.shop.ui.order

import androidx.lifecycle.MutableLiveData
import com.zkxy.shop.entity.goods.PlaceOrderEntity
import com.zkxy.shop.entity.order.ConfirmAddressEntity
import com.zkxy.shop.entity.order.OrderDetailsEntity
import com.zkxy.shop.network.request.apiService
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job

class OrderDetailsViewModel : BaseViewModel() {
    val payOrderSuccess = MutableLiveData<Boolean>()
    val confirmAddressSuccess = MutableLiveData<Boolean>()
    val orderDetailsEntity = MutableLiveData<OrderDetailsEntity>()
    val placeOrderEntity = MutableLiveData<PlaceOrderEntity>()
    val confirmAddressEntity = MutableLiveData<MutableList<ConfirmAddressEntity>?>()
    fun orderDetails(orderId: Int?, saleId: Int?) {
        request<Job>(block = {
            val apiData = if (saleId == -9999) {
                apiService.orderDetails(orderId = orderId).apiData()
            } else {
                apiService.orderDetails(orderId = orderId, saleId = saleId).apiData()
            }
            apiData.apply {
                orderDetailsEntity.value = this
                if (statusId == 2 && !deliveryCode.isNullOrEmpty()) {
                    confirmAddressEntity.value =
                        apiService.getAPPAddress(goodsId = goodsId).apiNoData()
                }
            }
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

    fun shipmentsApp(
        orderId: Int?,
        deliveryCode: String?,
        shipmentsAddress: String?,
        shipmentsAddressId: Int?
    ) {
        request<Job>(block = {
            loadingChange.showDialog.value = ""
            apiService.shipmentsApp(
                orderId = orderId,
                deliveryCode = deliveryCode,
                shipmentsAddress = shipmentsAddress,
                shipmentsAddressId = shipmentsAddressId
            ).apiNoData()
            confirmAddressSuccess.value = true
            loadingChange.dismissDialog.value = true
        }, error = {
            loadingChange.dismissDialog.value = true
        })
    }


}