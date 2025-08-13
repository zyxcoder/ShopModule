package com.zkxy.shop.ui.order

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.core.graphics.toColorInt
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
import com.zkxy.shop.entity.order.OrderDetailsEntity
import com.zkxy.shop.ext.pay
import com.zkxy.shop.ui.goods.adapter.ZtPointAdapter
import com.zkxy.shop.wxApi
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast
import com.zyxcoder.mvvmroot.utils.loadImage
import java.text.SimpleDateFormat

/**
 * statusId 订单状态：：1待发货; 2待提货; 3已发货; 4已提货; 5已取消
 */
class OrderDetailsActivity :
    BaseViewBindActivity<OrderDetailsViewModel, ActivityOrderDetailsBinding>() {

    private val time = (30 * 60 * 1000).toLong() //30分钟
    private val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private var colorFB7E2B = "#FB7E2B".toColorInt()
    private var color00B578 = "#00B578".toColorInt()
    private var color999999 = "#999999".toColorInt()
    private var colorFA5151 = "#FA5151".toColorInt()
    private val ztPointAdapter by lazy { ZtPointAdapter() }
    private var guideAddress: Address? = null
    private val selectNavigationDialog by lazy { SelectNavigationDialog(this) }
    private val confirmAddressDialog by lazy { ConfirmAddressDialog(this) }
    private var detailsEntity: OrderDetailsEntity? = null

    companion object {
        const val ORDER_ID = "order_id"
        fun startActivity(context: Context?, orderId: Int?) {
            context?.startActivity(Intent(context, OrderDetailsActivity::class.java).apply {
                putExtra(ORDER_ID, orderId)
            })
        }
    }

    private var isOpenWx = false
    private var orderId: Int? = null

    override fun init(savedInstanceState: Bundle?) {
        orderId = intent.getIntExtra(ORDER_ID, -1)
        mViewModel.orderDetails(orderId)
        mViewBind.apply {
            tvConsigneeTel.setIsInput(false)
            tvGoPay.onContinuousClick {
                detailsEntity?.apply {
                    if (scorePayFlag != 1 && payWay == 1 && prepayParams != null) {
                        wxApi.pay(
                            context = this@OrderDetailsActivity,
                            appId = prepayParams.appId,
                            partnerId = prepayParams.partnerId,
                            prepayId = prepayParams.prepayId,
                            nonceStr = prepayParams.nonceStr,
                            timeStamp = prepayParams.timeStamp,
                            sign = prepayParams.sign,
                        )
                        isOpenWx = true
                    } else {
                        mViewModel.payment(detailsEntity?.orderCode)
                    }
                }
            }
            tvOrderCode.onContinuousClick {
                copyText(detailsEntity?.orderCode ?: "")
                showToast("复制成功")
            }
            tvServe.onContinuousClick {
                AfterSaleDetailActivity.startActivity(
                    context = this@OrderDetailsActivity,
                    orderId = detailsEntity?.saleId ?: -1
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isOpenWx) {
            mViewModel.orderDetails(orderId)
            isOpenWx = false
        }
    }

    private val vsKdInfoLayout by lazy { LayoutFhInfoBinding.bind(mViewBind.vsKdInfo.inflate()) }
    private val vsZtInfoLayout by lazy { LayoutZtInfoBinding.bind(mViewBind.vsZtInfo.inflate()) }
    private val vsCancelInfoLayout by lazy { LayoutCancelInfoBinding.bind(mViewBind.vsCancelInfo.inflate()) }
    private val vsZtLayout by lazy { LayoutShopReceiveZtBinding.bind(mViewBind.vsZt.inflate()) }

    private var timer: MyCountDownTimer? = null

    override fun createObserver() {
        mViewModel.apply {
            orderDetailsEntity.observe(this@OrderDetailsActivity) {
                detailsEntity = it
                mViewBind.apply {
                    tvRemainder.visibility = View.GONE
                    if (it.statusId == 0 || it.statusId == 6) {
                        val remainder = try {
                            val remainderTime = (df.parse(it.createTime ?: "0")?.time
                                ?: 0) + time - System.currentTimeMillis()
                            if (remainderTime > time) time else remainderTime
                        } catch (e: Exception) {
                            0
                        }
                        if (remainder > 0) {
                            timer?.cancel()
                            tvRemainder.visibility = View.VISIBLE
                            timer = MyCountDownTimer(
                                remainder,
                                1000,
                                tvRemainder,
                            )
                            timer?.start()
                        } else {
                            tvRemainder.visibility = View.GONE
                        }
                    }
                    ivGoods.loadImage(it.goodsImg)
                    tvOrderCode.text = "订单编号：${it.orderCode}"
                    tvGoodsName.text = it.goodsName
                    tvPoints.text = it.paymentAmount
                    tvPayType.setMessageText(it.payWayName)
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
//                              afterSaleState  ：1待平台处理; 2已拒绝; 3申请撤销; 4退货/退款中; 5退款失败; 6退款完成
                                tvRefundProgress.text = when (it.afterSaleState) {
                                    1 -> {
                                        color = "#566BEB".toColorInt()
                                        "待平台处理"
                                    }

                                    2 -> {
                                        color = "#FA5151".toColorInt()
                                        "已拒绝"
                                    }

                                    3 -> {
                                        color = "#566BEB".toColorInt()
                                        "申请撤销"
                                    }

                                    4 -> {
                                        color = "#566BEB".toColorInt()
                                        "退货/退款中"
                                    }

                                    5 -> {
                                        color = "#566BEB".toColorInt()
                                        "退款失败"
                                    }

                                    6 -> {
                                        color = "#FA5151".toColorInt()
                                        tvSuccessTime.isVisible = true
                                        tvSuccessTime.setMessageText(it.refundTime)
                                        "退款成功"
                                    }

                                    else -> {
                                        color = "#566BEB".toColorInt()
                                        "退款处理中"
                                    }
                                }

                                tvRefundProgress.setTextColor(color)
                                clDes.isVisible = !it.refundDesc.isNullOrEmpty()
                                tvRefundDesc.text = it.refundDesc
                                tvRefundTime.setMessageText(it.afterSaleApplyTime)
                                tvRefundAmount.setMessageText(it.paymentAmount)
                            }
                        }

                        7 -> {
                            clServe.visibility = View.VISIBLE
                            if (it.afterSaleState == 4 && it.afterSaleType == 1) {
                                tvRemainder.visibility = View.VISIBLE
                                tvRemainder.text = "请在7日内退回商品，退货成功后完成退款"
                            }

                            vsCancelInfoLayout.apply {
                                val color: Int
                                tvRefundProgress.text = when (it.afterSaleState) {
                                    1 -> {
                                        color = "#566BEB".toColorInt()
                                        "待平台处理"
                                    }

                                    2 -> {
                                        color = "#FA5151".toColorInt()
                                        "已拒绝"
                                    }

                                    3 -> {
                                        color = "#566BEB".toColorInt()
                                        "申请撤销"
                                    }

                                    4 -> {
                                        color = "#566BEB".toColorInt()
                                        "退货/退款中"
                                    }

                                    5 -> {
                                        color = "#566BEB".toColorInt()
                                        "退款失败"
                                    }

                                    6 -> {
                                        color = "#00B42A".toColorInt()
                                        "退款成功"
                                    }

                                    else -> {
                                        color = "#566BEB".toColorInt()
                                        "退款处理中"
                                    }
                                }
                                tvRefundProgress.setTextColor(color)
                                clDes.isVisible = !it.refundDesc.isNullOrEmpty()
                                tvRefundDesc.text = it.refundDesc
                                tvRefundTime.setMessageText(it.afterSaleApplyTime)
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
                    mViewModel.orderDetails(orderId)
                }
            }
            confirmAddressSuccess.observe(this@OrderDetailsActivity) {
                if (it) {
                    showToast("提交成功")
                    mViewModel.orderDetails(orderId)
                    vsZtLayout.layoutZtList.isVisible = false
                }
            }
            confirmAddressEntity.observe(this@OrderDetailsActivity) { addressList ->
                val deliveryCode = mViewBind.tvPickupCode.text.toString().trim()
                if (!addressList.isNullOrEmpty()) {
                    confirmAddressDialog.onConfirmAddressClickListener = {
                        mViewModel.shipmentsApp(
                            orderId = detailsEntity?.orderId,
                            deliveryCode = deliveryCode,
                            shipmentsAddress = it.label,
                            shipmentsAddressId = it.value
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

    private inner class MyCountDownTimer(
        millisInFuture: Long,
        var countDownInterval: Long,
        var tv: TextView,
    ) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            //设置时间格式
            val m = millisUntilFinished / countDownInterval
            val minute = (m / 60) % 60
            val s = m % 60
            tv.text = "还剩${minute}:${s}订单自动取消"
        }

        override fun onFinish() {
            tv.visibility = View.GONE
            mViewModel.orderDetails(orderId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

}

