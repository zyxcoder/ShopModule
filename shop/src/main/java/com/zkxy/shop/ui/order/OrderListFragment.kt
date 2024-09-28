package com.zkxy.shop.ui.order

import com.gxy.common.common.activitylist.BaseCommonListFragment
import com.gxy.common.databinding.FragmentBaseCommonListBinding
import com.zkxy.shop.databinding.ItemOrderListBinding
import com.zkxy.shop.entity.order.OrderListEntity
import com.zkxy.shop.ui.order.adapter.OrderListAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter

class OrderListFragment(title: String) :
    BaseCommonListFragment<OrderListFragmentViewModel, FragmentBaseCommonListBinding, ItemOrderListBinding, OrderListEntity>(title) {
    override fun provideAdapter(): BaseViewBindingAdapter<OrderListEntity, ItemOrderListBinding> {
        return OrderListAdapter()
    }

    override fun provideRequestParams(): Array<out Any> {
        return arrayOf()
    }
}