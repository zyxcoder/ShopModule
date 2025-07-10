package com.zkxy.shop.ui.order

import com.gxy.common.common.activitylist.BaseCommonListFragment
import com.gxy.common.databinding.FragmentBaseCommonListBinding
import com.zkxy.shop.R
import com.zkxy.shop.common.dialog.AfterSalesDialog
import com.zkxy.shop.common.dialog.CancelDialog
import com.zkxy.shop.common.dialog.CancelOrderDialog
import com.zkxy.shop.databinding.ItemOrderListBinding
import com.zkxy.shop.entity.order.OrderListEntity
import com.zkxy.shop.ext.pay
import com.zkxy.shop.ui.order.adapter.OrderListAdapter
import com.zkxy.shop.wxApi
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.ext.showToast

class OrderListFragment(title: String, private val status: Int) :
    BaseCommonListFragment<OrderListFragmentViewModel, FragmentBaseCommonListBinding, ItemOrderListBinding, OrderListEntity>(
        title
    ) {

    private val orderListAdapter by lazy { OrderListAdapter() }

    override fun provideAdapter(): BaseViewBindingAdapter<OrderListEntity, ItemOrderListBinding> {
        return orderListAdapter.apply {

            setOnItemChildClickListener { _, view, position ->

                val orderListEntity = data[position]
                activity?.apply {
                    when (view.id) {
                        R.id.tvCancel -> {
                            CancelDialog(this, onConfirmClickListener = {
                                mViewModel.orderCancel(orderListEntity.orderId)
                            }).show()
                        }

                        R.id.tvGoPay -> {
                            //重新支付
                            CancelOrderDialog(this, onConfirmClickListener = {
                                if (orderListEntity.scorePayFlag != 1 && orderListEntity.payWay == 1 && orderListEntity.prepayParams != null) {
                                    wxApi.pay(
                                        context = getContext(),
                                        appId = orderListEntity.prepayParams.appId,
                                        partnerId = orderListEntity.prepayParams.partnerId,
                                        prepayId = orderListEntity.prepayParams.prepayId,
                                        nonceStr = orderListEntity.prepayParams.nonceStr,
                                        timeStamp = orderListEntity.prepayParams.timeStamp,
                                        sign = orderListEntity.prepayParams.sign,
                                    )
                                    isOpenWx = true
                                } else {
                                    mViewModel.payment(orderListEntity.orderCode)
                                }
                            }, isPay = true).show()
                        }

                        R.id.tvAfterSales -> {
                            AfterSalesDialog(this).apply {
                                onConfirmClickListener = {
                                    mViewModel.orderAfterSales(
                                        cancelReason = it,
                                        orderId = orderListEntity.orderId
                                    )
                                }
                            }.show()
                        }
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
        mViewModel.apply {
            cancelOrderSuccess.observe(this@OrderListFragment) {
                if (it) {
                    activity?.showToast("取消成功")
                    startSearch()
                }
            }

            payOrderSuccess.observe(this@OrderListFragment) {
                if (it) {
                    activity?.showToast("支付成功")
                    startSearch()
                }
            }
            orderAfterSalesSuccessSuccess.observe(this@OrderListFragment) {
                if (it) {
                    activity?.showToast("操作成功")
                    startSearch()
                }
            }
        }
    }

    private var isOpenWx = false

    override fun onResume() {
        super.onResume()
        if (isOpenWx) {
            startSearch()
            isOpenWx = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        orderListAdapter.cancelAllTimers()
    }
}