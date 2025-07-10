package com.zkxy.shop.ui.order.adapter

import android.os.CountDownTimer
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.core.text.parseAsHtml
import com.gxy.common.ext.copyText
import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemOrderListBinding
import com.zkxy.shop.entity.order.OrderListEntity
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast
import com.zyxcoder.mvvmroot.utils.loadImage
import java.text.SimpleDateFormat

/**
 * statusId 订单状态：0待支付 1待发货; 2待提货; 3已发货; 4已提货; 5已取消 7售后中
 */
class OrderListAdapter(private val isAfterSales: Boolean = false) :
    BaseViewBindingAdapter<OrderListEntity, ItemOrderListBinding>(
        ItemOrderListBinding::inflate,
        R.layout.item_order_list
    ) {
    private val timeMap = HashMap<Int?, MyCountDownTimer?>()
    private val colorFB7E2B = "#FB7E2B".toColorInt()
    private val color00B578 = "#00B578".toColorInt()
    private val color999999 = "#999999".toColorInt()
    private val colorFA5151 = "#FA5151".toColorInt()
    private val df: SimpleDateFormat
    private val time: Long

    init {
        addChildClickViewIds(
            R.id.tvCancel,
            R.id.tvGoPay,
            R.id.tvAfterSales,
            R.id.tvCancelAfterSales
        )
        df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        time = (30 * 60 * 1000).toLong() //30分钟
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
            tvGoodsName.text = item.goodsName
            tvNum.text = item.goodsNum.toString()
            tvAmount.text = item.paymentAmount
            tvBuyTime.text = "下单时间 ${item.createTime}"
            tvOrderCode.text = "订单编号：${item.orderCode}"
            tvStatus.text = item.statusName

            tvGoPay.visibility = View.GONE
            tvCancel.visibility = View.GONE
            tvCancelAfterSales.visibility = View.GONE
            tvAfterSales.visibility = View.GONE

            tvDeliverTime.visibility = View.GONE
            tvKdName.visibility = View.INVISIBLE
            tvZtTip.visibility = View.GONE
            tvRemainder.visibility = View.GONE
            llKd.visibility = View.VISIBLE


            var timer = timeMap[item.orderId]
            timer?.cancel()
            timer = null
            when (item.statusId) {
                //下单时间 + 30 - 当前时间
                0, 6 -> {
                    tvCancel.visibility = View.VISIBLE
                    tvStatus.setBackgroundResource(R.drawable.shape_ffe8e8_2)
                    tvStatus.setTextColor(colorFA5151)
                    tvGoPay.visibility = View.VISIBLE
                    tvRemainder.visibility = View.VISIBLE
                    llKd.visibility = View.GONE
                    val remainder = try {
                        val remainderTime = (df.parse(item.createTime ?: "0")?.time
                            ?: 0) + time - System.currentTimeMillis()
                        if (remainderTime > time) time else remainderTime
                    } catch (e: Exception) {
                        0
                    }
                    if (remainder > 0) {
                        tvRemainder.visibility = View.VISIBLE
                        llKd.visibility = View.GONE
                        val cdu = MyCountDownTimer(
                            remainder,
                            1000,
                            tvRemainder,
                            llKd,
                            item,
                            holder.layoutPosition
                        )
                        cdu.start()
                        timeMap[item.orderId] = cdu
                    } else {
                        tvRemainder.visibility = View.GONE
                        llKd.visibility = View.VISIBLE
                    }
                }

                1, 2 -> {
                    tvStatus.setBackgroundResource(R.drawable.shape_ffe9db_2)
                    tvStatus.setTextColor(colorFB7E2B)
                    if (item.deliveryType == 1) {//快递
                        llKd.visibility = View.GONE
                    } else {//自提
                        tvKdName.text = "提货码：<b>${item.deliveryCode}</b>".parseAsHtml()
                        tvKdName.visibility = View.VISIBLE
                        tvZtTip.visibility = View.VISIBLE
                    }
                }

                3, 4 -> {
                    tvStatus.setBackgroundResource(R.drawable.shape_e7f4f0_2)
                    tvStatus.setTextColor(color00B578)
                    tvKdName.visibility = View.VISIBLE
                    tvDeliverTime.visibility = View.VISIBLE
                    tvAfterSales.visibility = View.VISIBLE
                    if (item.deliveryType == 1) {//快递
                        tvKdName.text =
                            "快递信息：${item.logisticsCompany}\n快递单号：${item.expressNumber}"
                        tvDeliverTime.text = "发货时间：${item.shippingTime ?: ""}"
                    } else {//自提
                        tvKdName.text = "自提点：<b>${item.dAddress}</b>".parseAsHtml()
                        tvDeliverTime.text = "提货时间：${item.dTime ?: ""}"
                    }
                }

                5 -> {
                    tvStatus.setBackgroundResource(R.drawable.shape_ededed_2)
                    tvStatus.setTextColor(color999999)
                    tvDeliverTime.visibility = View.VISIBLE
                    tvKdName.visibility = View.VISIBLE
                    tvKdName.text = "取消原因：${item.orderDesc}"
                    tvDeliverTime.text = "取消时间：${item.cancelTime ?: ""}"
                }

                7 -> {
                    tvKdName.visibility = View.VISIBLE
                    tvDeliverTime.visibility = View.VISIBLE
                    if (isAfterSales) {//售后列表
                        //平台售后状态：1待平台处理; 2已拒绝; 3申请撤销; 4退货/退款中; 5退款失败; 6退款完成
                        tvOrderCode.text = "售后编号：${item.saleCode}"
                        tvStatus.text = when (item.afterSaleState) {
                            1 -> {
                                tvCancelAfterSales.visibility = View.VISIBLE
                                "待平台处理"
                            }

                            2 -> {
                                "已拒绝"
                            }

                            4 -> {
                                tvRemainder.visibility = View.VISIBLE
                                tvRemainder.text = "请在7日内退回商品，退货成功后完成退款"
                                "退货/退款中"
                            }

                            5 -> {
                                "退款失败"
                            }

                            6 -> {
                                "退款完成"
                            }

                            else -> {
                                "待平台处理"
                            }
                        }
                        tvKdName.text = "订单编号：${item.orderCode}\n退货类型：${if (item.afterSaleType ==1)"退货" else "退款"}"
                        tvDeliverTime.text = "申请时间：${item.afterSaleApplyTime ?: ""}"
                        //afterSaleType 1退货 2退款  申请时间afterSaleApplyTime
                    } else {
                        tvStatus.setBackgroundResource(R.drawable.shape_ffe9db_2)
                        tvStatus.setTextColor(colorFB7E2B)
                        tvKdName.text = "退款进度：${item.salesProgress}\n取消原因：${item.orderDesc}"
                        tvDeliverTime.text = "申请时间：${item.cancelTime ?: ""}"
                        tvStatus.text = item.statusName
                    }
                }

                else -> {
                    tvStatus.setBackgroundResource(R.drawable.shape_ffe9db_2)
                    tvStatus.setTextColor(colorFB7E2B)
                }
            }
        }

    }

    fun cancelAllTimers() {
        for (mutableEntry in timeMap) {
            mutableEntry.value?.cancel()
        }
        timeMap.clear()
    }

    private inner class MyCountDownTimer(
        millisInFuture: Long,
        var countDownInterval: Long,
        var tv: TextView,
        val llKd: LinearLayout,
        var item: OrderListEntity,
        var position: Int
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
            llKd.visibility = View.VISIBLE
            item.statusId = 5
            item.orderDesc = "订单自动取消"
            notifyItemChanged(position)
        }
    }

}