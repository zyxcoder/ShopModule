package com.zkxy.shop.ui.order

import com.gxy.common.common.activitylist.BaseCommonListFragment
import com.gxy.common.databinding.FragmentBaseCommonListBinding
import com.zkxy.shop.common.dialog.CancelOrderDialog
import com.zkxy.shop.databinding.ItemOrderListBinding
import com.zkxy.shop.entity.order.OrderListEntity
import com.zkxy.shop.ui.order.adapter.OrderListAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.ext.showToast

class OrderListFragment(title: String) :
    BaseCommonListFragment<OrderListFragmentViewModel, FragmentBaseCommonListBinding, ItemOrderListBinding, OrderListEntity>(
        title
    ) {
    override fun provideAdapter(): BaseViewBindingAdapter<OrderListEntity, ItemOrderListBinding> {
        return OrderListAdapter().apply {

            setOnItemChildClickListener { _, _, position ->
                val orderListEntity = data[position]
                activity?.apply {
                    CancelOrderDialog(this, onConfirmClickListener = {
                        mViewModel.orderCancel(orderListEntity.orderId)
                    }).show()
                }
            }

            setOnItemClickListener { _, _, position ->
                val orderListEntity = data[position]
                OrderDetailsActivity.startActivity(activity, orderId = orderListEntity.orderId)
            }
        }
    }

    override fun provideRequestParams(): Array<out Any> {
        return arrayOf()
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.cancelOrderSuccess.observe(this) {
            if (it) {
                activity?.showToast("取消成功")
                startSearch()
            }
        }
    }
}