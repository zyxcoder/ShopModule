package com.zkxy.shop.ui.order.adapter

import android.graphics.Color
import android.view.View
import com.gxy.common.ext.copyText
import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemOrderListBinding
import com.zkxy.shop.entity.order.OrderListEntity
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast
import com.zyxcoder.mvvmroot.utils.loadImage

/**
 * statusId 订单状态：0待支付 1待发货; 2待提货; 3已发货; 4已提货; 5已取消
 */
class OrderListAdapter : BaseViewBindingAdapter<OrderListEntity, ItemOrderListBinding>(
    ItemOrderListBinding::inflate,
    R.layout.item_order_list
) {

    private var colorFB7E2B = Color.parseColor("#FB7E2B")
    private var color00B578 = Color.parseColor("#00B578")
    private var color999999 = Color.parseColor("#999999")
    private var colorFA5151 = Color.parseColor("#FA5151")

    init {
        addChildClickViewIds(R.id.tvCancel, R.id.tvGoPay)
    }

    override fun convert(
        holder: BaseViewBindingHolder<ItemOrderListBinding>,
        item: OrderListEntity
    ) {
        holder.viewBind.apply {
            ivGoodsImg.loadImage(item.goodsImg)
            tvOrderCode.onContinuousClick {
                context.copyText(item.orderCode ?: "")
                context.showToast("复制成功")
            }

            tvOrderCode.text = "订单编号：${item.orderCode}"
            tvGoodsName.text = item.goodsName
            tvNum.text = item.goodsNum.toString()
            tvAmount.text = item.paymentAmount
            tvBuyTime.text = "下单时间 ${item.createTime}"
            tvStatus.text = item.statusName
            tvCancel.visibility = View.VISIBLE

            tvDeliverTime.visibility = View.INVISIBLE
            tvReceiveTitle.visibility = View.INVISIBLE
            tvKdName.visibility = View.INVISIBLE
            tvZtTip.visibility = View.INVISIBLE
            tvGoPay.visibility = View.GONE

            when (item.statusId) {
                0 -> {
                    tvStatus.setBackgroundResource(R.drawable.shape_ffe8e8_2)
                    tvStatus.setTextColor(colorFA5151)
                    tvGoPay.visibility = View.VISIBLE
                }

                1, 2 -> {
                    tvStatus.setBackgroundResource(R.drawable.shape_ffe9db_2)
                    tvStatus.setTextColor(colorFB7E2B)
                    if (item.deliveryType == 1) {//快递

                    } else {//自提
                        tvReceiveTitle.text = "提货码："
                        tvKdName.text = item.deliveryCode
                        tvReceiveTitle.visibility = View.VISIBLE
                        tvKdName.visibility = View.VISIBLE
                        tvZtTip.visibility = View.VISIBLE
                    }
                }

                3, 4 -> {
                    tvStatus.setBackgroundResource(R.drawable.shape_e7f4f0_2)
                    tvStatus.setTextColor(color00B578)
                    tvReceiveTitle.visibility = View.VISIBLE
                    tvKdName.visibility = View.VISIBLE
                    tvDeliverTime.visibility = View.VISIBLE
                    if (item.deliveryType == 1) {//快递
                        tvReceiveTitle.text = "快递信息："
                        tvKdName.text = item.logisticsCompany
                        tvDeliverTime.text = "发货时间：${item.shippingTime ?: ""}"
                    } else {//自提
                        tvReceiveTitle.text = "自提点："
                        tvKdName.text = item.dAddress
                        tvDeliverTime.text = "提货时间：${item.dTime ?: ""}"
                    }
                }

                5 -> {
                    tvStatus.setBackgroundResource(R.drawable.shape_ededed_2)
                    tvStatus.setTextColor(color999999)
                    tvCancel.visibility = View.INVISIBLE
                    tvDeliverTime.visibility = View.VISIBLE
                    tvReceiveTitle.visibility = View.VISIBLE
                    tvKdName.visibility = View.VISIBLE
                    tvKdName.text = item.orderDesc
                    tvReceiveTitle.text = "取消原因："
                    tvDeliverTime.text = "取消时间：${item.cancelTime ?: ""}"
                }

                else -> {
                    tvStatus.setBackgroundResource(R.drawable.shape_ffe9db_2)
                    tvStatus.setTextColor(colorFB7E2B)
                }
            }
        }
    }
}