package com.zkxy.shop.ui.order

import com.gxy.common.common.activitylist.BaseCommonListFragment
import com.gxy.common.databinding.FragmentBaseCommonListBinding
import com.zkxy.shop.R
import com.zkxy.shop.common.dialog.CancelOrderDialog
import com.zkxy.shop.databinding.ItemOrderListBinding
import com.zkxy.shop.entity.order.OrderListEntity
import com.zkxy.shop.ui.order.adapter.OrderListAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.ext.showToast

class OrderListFragment(title: String, private val status: Int) :
    BaseCommonListFragment<OrderListFragmentViewModel, FragmentBaseCommonListBinding, ItemOrderListBinding, OrderListEntity>(
        title
    ) {
    override fun provideAdapter(): BaseViewBindingAdapter<OrderListEntity, ItemOrderListBinding> {
        return OrderListAdapter().apply {

            setOnItemChildClickListener { _, view, position ->
                val orderListEntity = data[position]
                activity?.apply {
                    if (view.id == R.id.tvCancel) {
                        CancelOrderDialog(this, onConfirmClickListener = {
                            mViewModel.orderCancel(orderListEntity.orderId)
                        }).show()
                    } else {
                        //重新支付
                        CancelOrderDialog(this, onConfirmClickListener = {
                            mViewModel.payment(orderListEntity.orderCode)
                        }, isPay = true).show()

                    }
                }
            }

            setOnItemClickListener { _, _, position ->
                val orderListEntity = data[position]
                OrderDetailsActivity.startActivity(activity, orderId = orderListEntity.orderId)
            }
        }
    }

    override fun provideRequestParams(): Array<out Any> {
        return arrayOf(status)
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.cancelOrderSuccess.observe(this) {
            if (it) {
                activity?.showToast("取消成功")
                startSearch()
            }
        }

        mViewModel.payOrderSuccess.observe(this) {
            if (it) {
                activity?.showToast("支付成功")
                startSearch()
            }
        }
    }
}