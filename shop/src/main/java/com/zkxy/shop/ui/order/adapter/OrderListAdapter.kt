package com.zkxy.shop.ui.order.adapter

import android.graphics.Color
import android.os.CountDownTimer
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
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
 * statusId 订单状态：0待支付 1待发货; 2待提货; 3已发货; 4已提货; 5已取消
 */
class OrderListAdapter : BaseViewBindingAdapter<OrderListEntity, ItemOrderListBinding>(
    ItemOrderListBinding::inflate,
    R.layout.item_order_list
) {
    private val timeMap = HashMap<Int?, MyCountDownTimer?>()
    private var colorFB7E2B = Color.parseColor("#FB7E2B")
    private var color00B578 = Color.parseColor("#00B578")
    private var color999999 = Color.parseColor("#999999")
    private var colorFA5151 = Color.parseColor("#FA5151")
    private val df: SimpleDateFormat
    private val time: Long

    init {
        addChildClickViewIds(R.id.tvCancel, R.id.tvGoPay)
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
            llRemainder.visibility = View.GONE
            var timer = timeMap[item.orderId]
            timer?.cancel()
            timer = null
            when (item.statusId) {
                //下单时间 + 30 - 当前时间
                0, 6 -> {
                    tvCreateTime.text = "下单时间 ${item.createTime}"
                    tvStatus.setBackgroundResource(R.drawable.shape_ffe8e8_2)
                    tvStatus.setTextColor(colorFA5151)
                    tvGoPay.visibility = View.VISIBLE
                    llRemainder.visibility = View.VISIBLE
                    val remainder = try {
                        val remainderTime = (df.parse(item.createTime ?: "0")?.time
                            ?: 0) + time - System.currentTimeMillis()
                        if (remainderTime > time) time else remainderTime
                    } catch (e: Exception) {
                        0
                    }
                    if (remainder > 0) {
                        llRemainder.visibility = View.VISIBLE
                        val cdu = MyCountDownTimer(
                            remainder,
                            1000,
                            tvRemainder,
                            llRemainder,
                            item,
                            holder.layoutPosition
                        )
                        cdu.start()
                        timeMap[item.orderId] = cdu
                    } else {
                        llRemainder.visibility = View.GONE
                    }
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
                    tvCancel.visibility = View.INVISIBLE
                    if (item.deliveryType == 1) {//快递
                        tvReceiveTitle.text = "快递信息："
                        tvKdName.text = "${item.logisticsCompany} ${item.expressNumber}"
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
        var llRemainder: LinearLayout,
        var item: OrderListEntity,
        var position: Int
    ) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            //设置时间格式
            val m = millisUntilFinished / countDownInterval
            val minute = (m / 60) % 60
            val s = m % 60
            tv.text = "${minute}分${s}秒"
        }

        override fun onFinish() {
            llRemainder.visibility = View.GONE
            item.statusId = 5
            notifyItemChanged(position)
        }
    }

}