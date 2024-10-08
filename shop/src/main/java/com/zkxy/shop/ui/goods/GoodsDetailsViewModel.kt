package com.zkxy.shop.ui.goods

import androidx.lifecycle.MutableLiveData
import com.zkxy.shop.entity.goods.GoodsDetailsEntity
import com.zkxy.shop.network.request.apiService
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job

class GoodsDetailsViewModel : BaseViewModel() {

    val goodsDetailsEntity = MutableLiveData<GoodsDetailsEntity>()

    fun goodsDetail(goodsId: Int) {
        request<Job>(block = {
            loadingChange.showDialog.value = ""
            goodsDetailsEntity.value = apiService.goodsDetail(goodsId = goodsId).apiData()
            loadingChange.dismissDialog.value = true
        }, error = {
            loadingChange.dismissDialog.value = true
        })
    }
}