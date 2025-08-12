package com.zkxy.shop.ui.order

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.gxy.common.base.BaseViewBindActivity
import com.gxy.common.common.loadsir.getLoadSir
import com.gxy.common.common.loadsir.setLoadContentStatus
import com.kingja.loadsir.core.LoadService
import com.zkxy.shop.R
import com.zkxy.shop.common.dialog.SelectNavigationDialog
import com.zkxy.shop.databinding.ActivityAfterSaleDetailBinding
import com.zkxy.shop.entity.goods.Address
import com.zkxy.shop.ext.copyText
import com.zkxy.shop.ui.goods.adapter.ZtPointAdapter
import com.zkxy.shop.utils.formatProductInfo
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast
import com.zyxcoder.mvvmroot.utils.ImageOptions
import com.zyxcoder.mvvmroot.utils.loadImage

/**
 * @author zhangyuxiang
 * @date 2025/6/25
 */
class AfterSaleDetailActivity :
    BaseViewBindActivity<AfterSaleDetailViewModel, ActivityAfterSaleDetailBinding>() {
    private lateinit var mPageLoadService: LoadService<Any>

    private var guideAddress: Address? = null
    private val selectNavigationDialog by lazy { SelectNavigationDialog(this) }

    companion object {
        private const val ORDER_ID = "order_id"
        fun startActivity(context: Context?, orderId: Int) {
            context?.startActivity(Intent(context, AfterSaleDetailActivity::class.java).apply {
                putExtra(ORDER_ID, orderId)
            })
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        mViewBind.apply {
            mPageLoadService = getLoadSir().register(consecutiveScrollerLayout) {
                mViewModel.fetchOrderAfterSalesDetails(
                    orderId = intent.getIntExtra(ORDER_ID, 0),
                )
            }
            viewAfterSaleGoods.apply {
                ivCopyOrderCode.onContinuousClick {
                    copyText(mViewModel.afterSaleDetailData.value?.orderCode ?: "")
                    showToast("复制订单编号成功")
                }
                ivCopyAfterSaleCode.onContinuousClick {
                    copyText(mViewModel.afterSaleDetailData.value?.saleCode ?: "")
                    showToast("复制售后编号成功")
                }
            }
            viewRefundAddress.apply {
                ivCopyName.onContinuousClick {
                    copyText(mViewModel.afterSaleDetailData.value?.saleAddress?.saleAddress ?: "")
                    showToast("复制地址成功")
                }
            }
            viewShopZt.apply {
                layoutZtList.updateLayoutParams<MarginLayoutParams> {
                    leftMargin = 0
                    rightMargin = 0
                }
            }
        }

        mViewModel.fetchOrderAfterSalesDetails(
            orderId = intent.getIntExtra(ORDER_ID, 0)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun createObserver() {
        super.createObserver()
        mViewModel.apply {
            loadContentStatus.observe(this@AfterSaleDetailActivity) {
                mPageLoadService.setLoadContentStatus(it)
            }
            afterSaleDetailData.observe(this@AfterSaleDetailActivity) {
                mViewBind.apply {
                    viewAfterSaleStatus.apply {
//                        平台售后状态：1待平台处理; 2已拒绝; 3申请撤销; 4退货/退款中; 5退款失败; 6退款完成
                        when (it.tmsAfterSaleState) {
                            1 -> {
                                //待平台处理
                                tvHandleTitle.text = "【平台处理】"
                                tvHandleDesc.text = "预计1-3个工作日内完成审核"
                            }

                            2 -> {
                                //已拒绝
                                clAfterSaleStatus.isVisible = false
                            }

                            3 -> {
                                //申请撤销
                                clAfterSaleStatus.isVisible = false
                            }

                            4, 5 -> {
                                //退货/退款中
                                tvHandleTitle.text =
                                    if (it.afterSaleType == 1) "【退货中】" else "【退款中】"
                                tvHandleDesc.text =
                                    if (it.afterSaleType == 1) "请在7日内退回商品并确保商品不影响二次销售（质量问题除外），商家收货后1-3个工作日完成退款" else "正在为您加速退款中"
                            }

                            6 -> {
                                //退款完成
                                tvHandleTitle.text = "【退款完成】"
                                tvHandleDesc.text = "平台已为您退款成功"
                            }
                        }
                        ivSubmit.setImageResource(R.drawable.ic_after_sale_handle)
                        tvSubmit.setTextColor(
                            ContextCompat.getColor(
                                this@AfterSaleDetailActivity, R.color.color_04091A
                            )
                        )
                        viewLineOne.setBackgroundResource(R.color.color_3B82F6)
                        ivHandle.setImageResource(R.drawable.ic_after_sale_handle)
                        tvHandle.setTextColor(
                            ContextCompat.getColor(
                                this@AfterSaleDetailActivity, R.color.color_04091A
                            )
                        )
                        ivRefund.setImageResource(if (it.tmsAfterSaleState == 4 || it.tmsAfterSaleState == 5 || it.tmsAfterSaleState == 6) R.drawable.ic_after_sale_handle else R.drawable.ic_after_sale_no_complete)
                        tvRefund.setTextColor(
                            ContextCompat.getColor(
                                this@AfterSaleDetailActivity,
                                if (it.tmsAfterSaleState == 4 || it.tmsAfterSaleState == 5 || it.tmsAfterSaleState == 6) R.color.color_04091A else R.color.color_565D73
                            )
                        )
                        viewLineTwo.setBackgroundResource(if (it.tmsAfterSaleState == 4 || it.tmsAfterSaleState == 5 || it.tmsAfterSaleState == 6) R.color.color_3B82F6 else R.color.color_B8BDCC)
                        ivComplete.setImageResource(if (it.tmsAfterSaleState == 6) R.drawable.ic_after_sale_handle else R.drawable.ic_after_sale_no_complete)
                        tvComplete.setTextColor(
                            ContextCompat.getColor(
                                this@AfterSaleDetailActivity,
                                if (it.tmsAfterSaleState == 6) R.color.color_04091A else R.color.color_565D73
                            )
                        )
                        viewLineThree.setBackgroundResource(if (it.tmsAfterSaleState == 6) R.color.color_3B82F6 else R.color.color_B8BDCC)
                    }
                    viewRefundColse.apply {
                        when (it.tmsAfterSaleState) {

                            2 -> {
                                //已拒绝
                                tvRefundClose.text =
                                    if (it.afterSaleType == 1) "退货申请已关闭" else "退款申请已关闭"
                                tvRefundCloseDesc.text =
                                    "平台已拒绝您的售后，售后保障期内，您可以重新发起售后申请"
                                tvRejectReason.text = "拒绝原因：${it.afterSaleDesc ?: ""}"
                            }

                            3 -> {
                                //申请撤销
                                tvRefundClose.text =
                                    if (it.afterSaleType == 1) "退货申请已关闭" else "退款申请已关闭"
                                tvRefundCloseDesc.text =
                                    "您已主动取消售后，售后保障期内，您可以重新发起售后申请"
                                clRejectReason.isVisible = false
                            }

                            else -> {
                                clRefundClose.isVisible = false
                            }

                        }
                    }

                    viewAfterSaleGoods.apply {
                        ivGoods.loadImage(url = it.goodsImg, imageOptions = ImageOptions().apply {
                            error = com.gxy.common.R.drawable.ps_image_placeholder
                            placeholder = com.gxy.common.R.drawable.ps_image_placeholder
                            fallback = com.gxy.common.R.drawable.ps_image_placeholder
                        })
                        tvGoodsName.text = it.goodsName
                        tvPriceAndPoint.text = formatProductInfo(
                            points = it.platformPoints,
                            price = it.platformMoney,
                            mainTextSize = 14,
                            minorTextSize = 14,
                            isNeedSpace = false,
                            chineseTextColor = ContextCompat.getColor(
                                this@AfterSaleDetailActivity,
                                R.color.color_04091A
                            )
                        )
                        tvCount.text = "申请数量：${it.goodsNum ?: 0}"
                        tvOrderCode.text = "订单编号：${it.orderCode}"
                        tvAfterSaleCode.text = "售后编号：${it.saleCode}"
                        tvRefundMoney.text = "退款金额：" + formatProductInfo(
                            points = it.payPoints,
                            price = it.payment,
                            mainTextSize = 12,
                            minorTextSize = 12,
                            isNeedSpace = false
                        )
                        tvRefundType.text =
                            "售后类型：${if (it.afterSaleType == 1) "退货" else "退款"}"
                        tvCancelReason.text = "取消原因：${it.cancelReason ?: ""}"
                        tvApplyTime.text = "申请时间：${it.afterSaleApplyTime}"
                    }

                    viewRefundAddress.apply {
                        clAddress.isVisible = it.saleAddress != null
                        spaceRefundAddress.isVisible = it.saleAddress != null
                        tvName.text =
                            "${it.saleAddress?.saleConsignee}${it.saleAddress?.saleConsigneeTel}"
                        tvAddress.text = it.saleAddress?.saleAddress
                    }
                }
            }
            placeOrderEntity.observe(this@AfterSaleDetailActivity) {
                mViewBind.apply {
                    viewShopZt.apply {
                        layoutZtList.isVisible = !it.addressList.isNullOrEmpty()
                        ZtPointAdapter().apply {
                            rlvZt.adapter = this
                            setNewInstance(it.addressList)
                            setOnItemChildClickListener { _, view, position ->
                                val address = data[position]
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
                                        guideAddress = data[position]
                                        selectNavigationDialog.show()
                                    }
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
                }
            }
        }
    }
}