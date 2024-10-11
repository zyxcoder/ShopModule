package com.zkxy.shop.ui.order

import android.content.Context
import android.content.Intent
import com.google.android.material.tabs.TabLayout.MODE_AUTO
import com.gxy.common.common.activitylist.BaseCommonListActivity
import com.gxy.common.common.activitylist.BaseCommonListFragment
import com.gxy.common.common.activitylist.BaseCommonListFragmentViewModel
import com.gxy.common.databinding.ActivityBaseCommonListBinding
import com.zkxy.shop.databinding.ItemOrderListBinding
import com.zkxy.shop.entity.order.OrderListEntity

class OrderListActivity :
    BaseCommonListActivity<OrderListViewModel, ActivityBaseCommonListBinding, ItemOrderListBinding, OrderListEntity>() {

    companion object {
        fun startActivity(context: Context?) {
            context?.startActivity(Intent(context, OrderListActivity::class.java))
        }
    }

    override fun provideFragments(): ArrayList<BaseCommonListFragment<out BaseCommonListFragmentViewModel<*>, *, *, *>> {
        return arrayListOf(
            OrderListFragment("全部"),
            OrderListFragment("待发货/待提货"),
            OrderListFragment("已发货/已提货"),
            OrderListFragment("已取消"),
        )
    }

    override fun provideTitleContent(): String {
        return "我的订单"
    }

    override var provideTabMode= MODE_AUTO

    override fun provideSearchHintContent(): String? {
        return "请输入订单号、商品名称"
    }

}