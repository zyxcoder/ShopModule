package com.zkxy.shop.ui.order

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.gxy.common.base.BaseViewBindActivity
import com.gxy.common.ext.copyText
import com.zkxy.shop.R
import com.zkxy.shop.common.dialog.ConfirmAddressDialog
import com.zkxy.shop.common.dialog.SelectNavigationDialog
import com.zkxy.shop.databinding.ActivityOrderDetailsBinding
import com.zkxy.shop.databinding.LayoutCancelInfoBinding
import com.zkxy.shop.databinding.LayoutFhInfoBinding
import com.zkxy.shop.databinding.LayoutShopReceiveZtBinding
import com.zkxy.shop.databinding.LayoutZtInfoBinding
import com.zkxy.shop.entity.goods.Address
import com.zkxy.shop.ui.goods.adapter.ZtPointAdapter
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast
import com.zyxcoder.mvvmroot.utils.loadImage

/**
 * statusId 订单状态：：1待发货; 2待提货; 3已发货; 4已提货; 5已取消
 */
class OrderDetailsActivity :
    BaseViewBindActivity<OrderDetailsViewModel, ActivityOrderDetailsBinding>() {

    private var colorFB7E2B = Color.parseColor("#FB7E2B")
    private var color00B578 = Color.parseColor("#00B578")
    private var color999999 = Color.parseColor("#999999")
    private var colorFA5151 = Color.parseColor("#FA5151")
    private val ztPointAdapter by lazy { ZtPointAdapter() }
    private var guideAddress: Address? = null
    private val selectNavigationDialog by lazy { SelectNavigationDialog(this) }
    private val confirmAddressDialog by lazy { ConfirmAddressDialog(this) }

    private var orderCode: String? = null
    private var orderId: Int? = null

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
        mViewBind.apply {
            tvConsigneeTel.setIsInput(false)
            tvGoPay.onContinuousClick {
                mViewModel.payment(orderCode)
            }
            tvOrderCode.onContinuousClick {
                copyText(orderCode ?: "")
                showToast("复制成功")
            }
        }
    }

    private val vsKdInfoLayout by lazy { LayoutFhInfoBinding.bind(mViewBind.vsKdInfo.inflate()) }
    private val vsZtInfoLayout by lazy { LayoutZtInfoBinding.bind(mViewBind.vsZtInfo.inflate()) }
    private val vsCancelInfoLayout by lazy { LayoutCancelInfoBinding.bind(mViewBind.vsCancelInfo.inflate()) }
    private val vsZtLayout by lazy { LayoutShopReceiveZtBinding.bind(mViewBind.vsZt.inflate()) }

    override fun createObserver() {
        mViewModel.apply {
            orderDetailsEntity.observe(this@OrderDetailsActivity) {
                orderCode = it.orderCode
                orderId = it.orderId
                mViewBind.apply {
                    ivGoods.loadImage(it.goodsImg)
                    tvOrderCode.text = "订单编号：${it.orderCode}"
                    tvGoodsName.text = it.goodsName
                    tvPoints.text = it.paymentAmount
//                    tvPrice.text = it.pri.toString()
                    tvSpecName.setMessageText(it.goodsSpecName)
                    tvNum.setMessageText(it.goodsNum.toString())
                    tvConsignee.setMessageText(it.consignee)
                    tvConsigneeTel.setPhone(it.consigneeTel)
                    tvDeliveryAddress.setMessageText(it.deliveryAddress)
                    tvPaymentAmount.text = it.paymentAmount
                    tvStatus.text = it.statusName
                    if (it.deliveryType == 1) {//快递
                        if (!it.logisticsCompany.isNullOrEmpty() && !it.expressNumber.isNullOrEmpty()) {
                            vsKdInfoLayout.apply {
                                tvLogisticsCompany.setMessageText(it.logisticsCompany)
                                tvExpressNumber.setMessageText(it.expressNumber)
                                tvShippingTime.setMessageText(it.shippingTime)
                            }
                        } else {
                            spKd.visibility = View.GONE
                        }
                    } else {//自提
                        tvDeliveryAddress.visibility = View.GONE
                        llDeliveryCode.visibility = View.VISIBLE
                        tvPickupCode.text = "--"
                        if (!it.shippingTime.isNullOrEmpty()) {
                            vsZtInfoLayout.apply {
                                tvZtTime.setMessageText(it.shippingTime)
                                tvZtPoint.text = it.shipmentsAddress
                            }
                        } else {
                            mViewModel.goodsStockAddressSearch(goodsId = it.goodsId ?: 0, 2)
                        }
                    }

                    clGoPay.visibility = View.GONE
                    clConfirm.visibility = View.GONE
                    when (it.statusId) {
                        0, 6 -> {
                            tvStatus.setBackgroundResource(R.drawable.shape_ffe8e8_2)
                            tvStatus.setTextColor(colorFA5151)
                            clGoPay.visibility = View.VISIBLE
                        }

                        1, 2 -> {
                            tvStatus.setBackgroundResource(R.drawable.shape_ffe9db_2)
                            tvStatus.setTextColor(colorFB7E2B)
                            if (!it.deliveryCode.isNullOrEmpty()) {
                                clConfirm.visibility = View.VISIBLE
                                tvPickupCode.text = it.deliveryCode
                            }
                        }

                        3, 4 -> {
                            tvStatus.setBackgroundResource(R.drawable.shape_e7f4f0_2)
                            tvStatus.setTextColor(color00B578)
                            if (!it.deliveryCode.isNullOrEmpty()) {
                                tvPickupCode.text = it.deliveryCode
                            }
                        }

                        5 -> {
                            tvStatus.setBackgroundResource(R.drawable.shape_ededed_2)
                            tvStatus.setTextColor(color999999)
                            vsCancelInfoLayout.apply {
                                val color: Int
                                //退款进度：1处理中，2已退款，3已拒绝
                                tvRefundProgress.text = when (it.refundProgress) {
                                    1 -> {
                                        color = Color.parseColor("#566BEB")
                                        "处理中"
                                    }

                                    2 -> {
                                        color = Color.parseColor("#00B578")
                                        "已完成"
                                    }

                                    3 -> {
                                        color = Color.parseColor("#FA5151")
                                        "已拒绝"
                                    }

                                    else -> {
                                        color = Color.parseColor("#566BEB")
                                        "--"
                                    }
                                }
                                tvRefundProgress.setTextColor(color)
                                tvRefundDesc.text = it.refundDesc
                                tvRefundTime.setMessageText(it.refundTime)
                                tvRefundAmount.setMessageText(it.paymentAmount)
                            }
                        }

                        else -> {
                            tvStatus.setBackgroundResource(R.drawable.shape_ffe9db_2)
                            tvStatus.setTextColor(colorFB7E2B)
                        }
                    }

                }
            }

            placeOrderEntity.observe(this@OrderDetailsActivity) {
                vsZtLayout.apply {
                    rlvZt.adapter = ztPointAdapter
                }
                ztPointAdapter.setNewInstance(it.addressList)
                ztPointAdapter.setOnItemChildClickListener { _, view, position ->
                    val address = ztPointAdapter.data[position]
                    when (view.id) {
                        R.id.tvDialing -> {
                            startActivity(
                                Intent(
                                    Intent.ACTION_DIAL,
                                    Uri.parse("tel:${address.pickAddressTel}")
                                )
                            )
                        }

                        R.id.tvNavigation -> {
                            guideAddress = ztPointAdapter.data[position]
                            selectNavigationDialog.show()
                        }
                    }
                }

                selectNavigationDialog.onSelectNavigationListener = {
                    if (guideAddress == null) {
                        showToast("暂无位置信息")
                    } else {
                        val uri = if (it) {//百度
                            Uri.parse("baidumap://map/direction?destination=${guideAddress!!.pickDetailAddress}")
                        } else {//高德
                            Uri.parse("amapuri://route/plan/?&dname=${guideAddress!!.pickDetailAddress}")
                        }
                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }
                }
            }
            payOrderSuccess.observe(this@OrderDetailsActivity) {
                if (it) {
                    showToast("支付成功")
                    mViewModel.orderDetails(intent.getIntExtra(ORDER_ID, -1))
                }
            }
            confirmAddressSuccess.observe(this@OrderDetailsActivity) {
                if (it) {
                    showToast("提交成功")
                    mViewModel.orderDetails(intent.getIntExtra(ORDER_ID, -1))
                    vsZtLayout.layoutZtList.isVisible = false
                }
            }
            confirmAddressEntity.observe(this@OrderDetailsActivity) { addressList ->
                val deliveryCode = mViewBind.tvPickupCode.text.toString().trim()
                if (!addressList.isNullOrEmpty()) {
                    confirmAddressDialog.onConfirmAddressClickListener = {
                        mViewModel.shipmentsApp(
                            orderId = orderId,
                            deliveryCode = deliveryCode,
                            shipmentsAddress = it.label
                        )
                    }
                    mViewBind.tvConfirm.onContinuousClick {
                        confirmAddressDialog.show()
                        confirmAddressDialog.setData(addressList, deliveryCode)
                    }
                }
            }
        }
    }

}