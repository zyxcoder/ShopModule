package com.zkxy.shop.ui.order

import com.gxy.common.common.activitylist.BaseCommonListFragment
import com.gxy.common.databinding.FragmentBaseCommonListBinding
import com.zkxy.shop.R
import com.zkxy.shop.common.dialog.CancelDialog
import com.zkxy.shop.databinding.ItemOrderListBinding
import com.zkxy.shop.entity.order.OrderListEntity
import com.zkxy.shop.ui.order.adapter.OrderListAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.ext.showToast

class AfterSalesListFragment(title: String) :
    BaseCommonListFragment<AfterSalesFragmentViewModel, FragmentBaseCommonListBinding, ItemOrderListBinding, OrderListEntity>(
        title
    ) {

    private val orderListAdapter by lazy { OrderListAdapter(isAfterSales = true) }

    override fun provideAdapter(): BaseViewBindingAdapter<OrderListEntity, ItemOrderListBinding> {
        return orderListAdapter.apply {

            setOnItemChildClickListener { _, view, position ->
                val orderListEntity = data[position]
                activity?.apply {
                    when (view.id) {
                        R.id.tvCancelAfterSales -> {
                            CancelDialog(this, onConfirmClickListener = {
                                mViewModel.orderAfterSalesRevoke(
                                    orderListEntity.orderId,
                                    orderListEntity.saleId
                                )
                            }, message = "确定撤销售后申请？").show()
                        }
                    }
                }
            }

            setOnItemClickListener { _, _, position ->
                val orderListEntity = data[position]
                OrderDetailsActivity.startActivity(
                    activity,
                    orderId = orderListEntity.orderId,
                    saleId = orderListEntity.saleId
                )
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