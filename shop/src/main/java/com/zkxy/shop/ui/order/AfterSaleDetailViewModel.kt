package com.zkxy.shop.ui.order

import androidx.lifecycle.MutableLiveData
import com.gxy.common.common.loadsir.LoadContentStatus
import com.zkxy.shop.entity.goods.PlaceOrderEntity
import com.zkxy.shop.entity.order.AfterSaleDetailEntity
import com.zkxy.shop.network.request.apiService
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job

/**
 * @author zhangyuxiang
 * @date 2025/6/25
 */
class AfterSaleDetailViewModel : BaseViewModel() {

    val loadContentStatus = MutableLiveData<LoadContentStatus>()
    val afterSaleDetailData = MutableLiveData<AfterSaleDetailEntity>()
    val placeOrderEntity = MutableLiveData<PlaceOrderEntity>()

    fun fetchOrderAfterSalesDetails(orderId: Int) {
        request<Job>(block = {
            loadContentStatus.value = LoadContentStatus.DEFAULT_LOADING
            afterSaleDetailData.value =
                apiService.orderAfterSalesDetails(orderId = orderId).apiData()
            placeOrderEntity.value = apiService.goodsStockAddressSearch(
                goodsId = afterSaleDetailData.value?.goodsId, deliveryMode = 2
            ).apiData()
            loadContentStatus.value = LoadContentStatus.SUCCESS
        }, error = {
            loadContentStatus.value = LoadContentStatus.DEFAULT_ERROR
        })
    }
}