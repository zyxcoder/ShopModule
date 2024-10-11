package com.zkxy.shop.ui.order

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.gxy.common.base.BaseViewBindActivity
import com.zkxy.shop.R
import com.zkxy.shop.databinding.ActivityOrderDetailsBinding
import com.zyxcoder.mvvmroot.utils.loadImage

/**
 * statusId 订单状态：：1待发货; 2待提货; 3已发货; 4已提货; 5已取消
 */
class OrderDetailsActivity :
    BaseViewBindActivity<OrderDetailsViewModel, ActivityOrderDetailsBinding>() {

    private var colorFB7E2B = Color.parseColor("#FB7E2B")
    private var color00B578 = Color.parseColor("#00B578")
    private var color999999 = Color.parseColor("#999999")

    companion object {
        const val ORDER_ID = "order_ID"
        fun startActivity(context: Context?, orderId: Int?) {
            context?.startActivity(Intent(context, OrderDetailsActivity::class.java).apply {
                putExtra(ORDER_ID, orderId)
            })
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        val orderId = intent.getIntExtra(ORDER_ID, -1)
        mViewModel.orderDetails(orderId)
    }

    override fun createObserver() {
        mViewModel.apply {
            orderDetailsEntity.observe(this@OrderDetailsActivity) {
                mViewBind.apply {
                    ivGoods.loadImage(it.goodsImg)
                    tvOrderCode.text = "订单编号：${it.orderCode}"
                    tvGoodsName.text = it.goodsName
                    tvPoints.text = it.platformPrice
//                    tvPrice.text = it.pri.toString()
                    tvSpecName.setMessageText(it.goodsSpecName)
                    tvNum.setMessageText(it.goodsNum.toString())
                    tvConsignee.setMessageText(it.consignee)
                    tvConsigneeTel.setPhone(it.consigneeTel)
                    tvDeliveryAddress.setMessageText(it.deliveryAddress)
                    tvPaymentAmount.setText(it.paymentAmount)
                    tvStatus.text = it.statusName

                    if (!it.deliveryCode.isNullOrEmpty()) {
                        tvPickupCode.text = it.deliveryCode
                    }
                    when (it.statusId) {
                        1, 2 -> {
                            tvStatus.setBackgroundResource(R.drawable.shape_ffe9db_2)
                            tvStatus.setTextColor(colorFB7E2B)
                        }

                        3, 4 -> {
                            tvStatus.setBackgroundResource(R.drawable.shape_e7f4f0_2)
                            tvStatus.setTextColor(color00B578)
                        }

                        5 -> {
                            tvStatus.setBackgroundResource(R.drawable.shape_ededed_2)
                            tvStatus.setTextColor(color999999)
                        }

                        else -> {
                            tvStatus.setBackgroundResource(R.drawable.shape_ffe9db_2)
                            tvStatus.setTextColor(colorFB7E2B)
                        }
                    }

                }
            }
        }
    }

}