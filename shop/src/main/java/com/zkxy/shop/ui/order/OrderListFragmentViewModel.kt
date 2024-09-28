package com.zkxy.shop.ui.order

import com.gxy.common.common.activitylist.BaseCommonListFragmentViewModel
import com.gxy.common.network.api.ApiResult
import com.zkxy.shop.entity.order.OrderListEntity

class OrderListFragmentViewModel : BaseCommonListFragmentViewModel<OrderListEntity>() {
    override suspend fun getCommonList(
        start: Int,
        searchKey: String?,
        vararg params: Any?
    ): ApiResult<MutableList<OrderListEntity>> {
        val mutableListOf = mutableListOf(OrderListEntity(""))
        return ApiResult(
            statusCode = "1",
            statusDesc = "",
            hasMore = false,
            data = mutableListOf,
            listCount = 1
        )
    }
}