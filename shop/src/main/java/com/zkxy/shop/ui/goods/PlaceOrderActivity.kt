package com.zkxy.shop.ui.goods

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import com.gxy.common.base.BaseViewBindActivity
import com.zkxy.shop.R
import com.zkxy.shop.appUserName
import com.zkxy.shop.appUserTel
import com.zkxy.shop.common.dialog.AddressBookBottomDialog
import com.zkxy.shop.common.dialog.SelectNavigationDialog
import com.zkxy.shop.common.dialog.SpecificationBottomDialog
import com.zkxy.shop.databinding.ActivityPlaceOrderBinding
import com.zkxy.shop.databinding.LayoutShopReceiveKdBinding
import com.zkxy.shop.databinding.LayoutShopReceiveZtBinding
import com.zkxy.shop.entity.goods.Address
import com.zkxy.shop.entity.goods.GoodsDetailsEntity
import com.zkxy.shop.ext.doubleToTwoDecimalPlaceString
import com.zkxy.shop.ext.multiply
import com.zkxy.shop.ui.goods.adapter.ZtPointAdapter
import com.zkxy.shop.ui.order.OrderDetailsActivity
import com.zkxy.shop.utils.SelectAddressUtil
import com.zyxcoder.mvvmroot.callback.lifecycle.ActivityManger
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast
import com.zyxcoder.mvvmroot.utils.ImageOptions
import com.zyxcoder.mvvmroot.utils.dpToPx
import com.zyxcoder.mvvmroot.utils.loadImage

class PlaceOrderActivity : BaseViewBindActivity<PlaceOrderViewModel, ActivityPlaceOrderBinding>() {
    private var selectAddressUtil: SelectAddressUtil? = null
    private val ztPointAdapter by lazy { ZtPointAdapter() }
    private val specificationBottomDialog by lazy { SpecificationBottomDialog(this) }
    private val addressBookBottomDialog by lazy { AddressBookBottomDialog(this) }
    private val selectNavigationDialog by lazy { SelectNavigationDialog(this) }
    private val layoutShopReceiveKdBinding by lazy { LayoutShopReceiveKdBinding.bind(mViewBind.vsKd.inflate()) }
    private var guideAddress: Address? = null

    companion object {
        const val GOODS_ID = "goodsId"
        const val GOODS_ENTITY = "goods_entity"
        fun startActivity(
            context: Context,
            goodsId: Int,
            goodsDetailsEntity: GoodsDetailsEntity
        ) {
            context.startActivity(Intent(context, PlaceOrderActivity::class.java).apply {
                putExtra(GOODS_ID, goodsId)
                putExtra(GOODS_ENTITY, goodsDetailsEntity)
            })
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        val goodsDetailsEntity =
            intent.getSerializableExtra(GOODS_ENTITY) as? GoodsDetailsEntity ?: return
        mViewModel.goodsPayType()
        when (goodsDetailsEntity.priceType) {
            2, 3 -> {
                mViewBind.llWxPay.visibility = View.VISIBLE
            }
        }

        mViewBind.apply {
            tvGoodsName.text = goodsDetailsEntity.goodsName

            inputPerson.setSelectText(appUserName)
            inputTel.setPhone(appUserTel)

            (goodsDetailsEntity.goodsScorePrice ?: 0).apply {
                if (this <= 0) {
                    tvTopPoint.visibility = View.GONE
                } else {
                    tvTopPoints.text = this.toString()
                }
            }

            if (goodsDetailsEntity.goodsMoneyPrice == null || goodsDetailsEntity.goodsMoneyPrice <= 0.0) {
                tvTopPoint.text = "积分"
                tvTopUnit.visibility = View.GONE
            } else {
                tvTopUnit.visibility = View.VISIBLE
                tvTopPoint.text = "积分+"
                tvTopMoney.text = goodsDetailsEntity.goodsMoneyPrice.doubleToTwoDecimalPlaceString()
            }



            mViewBind.tvTopNum.text =
                if (goodsDetailsEntity.buyEmption == -1) "不限" else "每人限购${goodsDetailsEntity.buyEmption}件"
            inputSpecification.onContinuousClick {
                specificationBottomDialog.show()
            }

            specificationBottomDialog.onItemClickListener = {
                inputSpecification.setSelectText(it.specName, it.goodsSpecId)
            }

            ivGoods.loadImage(
                goodsDetailsEntity.bannerPicDtoList?.firstOrNull()?.picUrl,
                imageOptions = ImageOptions().apply { cornersRadius = dpToPx(4f).toInt() }
            )

            val goodsId = intent.getIntExtra(GOODS_ID, 15)

            if (goodsDetailsEntity.deliveryMode == 1) {//1:快递 2:自提
                mViewModel.getUserAddress()
                mViewModel.initJsonData(this@PlaceOrderActivity)
                selectAddressUtil = SelectAddressUtil(this@PlaceOrderActivity)
                layoutShopReceiveKdBinding.apply {
                    selectAddressUtil?.onSelectedAddressListener = {
                        inputSelectAddress.setSelectText(it)
                    }

                    llAddressBook.onContinuousClick {
                        addressBookBottomDialog.show()
                    }

                    inputSelectAddress.onContinuousClick {
                        selectAddressUtil?.show()
                    }

                    addressBookBottomDialog.addressItemClickListener = {
                        inputSelectAddress.setSelectText(it?.administrativeRegion)
                        tvAddress.setSelectText(it?.address)
                    }
                }
                ivReceiveType.setBackgroundResource(R.drawable.ic_kd)

                addressBookBottomDialog.addressListener = { isEdit, addressBookEntity ->
                    ReceiveAddressActivity.startActivityForResult(
                        this@PlaceOrderActivity,
                        isEdit = isEdit,
                        launcher = resultRefreshLauncher,
                        addressBookEntity = addressBookEntity
                    )
                }

                addressBookBottomDialog.addressAcquiesceListener = { it, acquiesce ->
                    mViewModel.acquiesceAddress(it, acquiesce)
                }

                addressBookBottomDialog.addressDeleteListener = {
                    mViewModel.deleteAddress(it)
                }
            } else {
                LayoutShopReceiveZtBinding.bind(vsZt.inflate()).apply {
                    rlvZt.adapter = ztPointAdapter
                }
                ivReceiveType.setBackgroundResource(R.drawable.ic_zt)

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
            mViewModel.goodsStockAddressSearch(
                goodsId = goodsId,
                deliveryMode = goodsDetailsEntity.deliveryMode ?: 1
            )

            etNum.doAfterTextChanged {
                val num = it?.toString()?.toIntOrNull() ?: 0
                tvNum.text = num.toString()

                (goodsDetailsEntity.goodsScorePrice ?: 0).apply {
                    if (this <= 0) {
                        tvBottomPoint.visibility = View.GONE
                    } else {
                        tvBottomPoints.text = this.multiply(num)
                    }
                }

                if (goodsDetailsEntity.goodsMoneyPrice == null || goodsDetailsEntity.goodsMoneyPrice <= 0.0) {
                    tvBottomPoint.text = "积分"
                    tvBottomUnit.visibility = View.GONE
                } else {
                    tvBottomUnit.visibility = View.VISIBLE
                    tvBottomPoint.text = "积分+"
                    tvBottomMoney.text = (goodsDetailsEntity.goodsMoneyPrice).multiply(num)
                }
            }

            //下单
            tvTakeOrder.onContinuousClick {
                var check = true
                mutableListOf(inputSpecification, inputPerson).forEach {
                    if (it.contentIsEmptyAndShowToast()) {
                        check = false
                        return@onContinuousClick
                    }
                }
                if ((etNum.text.toString().toIntOrNull() ?: 0) < 1) {
                    showToast("请输入数量")
                    check = false
                }

//                if ((inputNum.getContent()?.toIntOrNull()
//                        ?: 0) > (goodsDetailsEntity.buyEmption ?: 1)
//                ) {
//                    showToast("超过最大购买数量")
//                    check = false
//                }

                if (inputTel.contentIsEmptyAndShowToast()) {
                    check = false
                }

                val address = if (goodsDetailsEntity.deliveryMode == 1) {
                    if (layoutShopReceiveKdBinding.inputSelectAddress.contentIsEmptyAndShowToast()) {
                        check = false
                    }
                    if (layoutShopReceiveKdBinding.tvAddress.contentIsEmptyAndShowToast()) {
                        check = false
                    }
                    layoutShopReceiveKdBinding.inputSelectAddress.getContent() + layoutShopReceiveKdBinding.tvAddress.getContent()
                } else null


// [{"label":"积分支付","value":1},{"label":"微信支付","value":2},{"label":"支付宝支付","value":3},{"label":"积分+微信支付","value":4},{"label":"积分+支付宝支付","value":5},{"label":"对公转账","value":6}]
// priceType: 1 积分  2现金+积分 3现金
                val goodsPayType = when (goodsDetailsEntity.priceType) {
                    1 -> 1
                    2 -> 4
                    3 -> 2
                    else -> 1
                }

                if (check) {
                    mViewModel.createOrder(
                        consignee = inputPerson.getContent(),
                        consigneeTel = inputTel.getPhone(),
                        goodsId = goodsId,
                        goodsNum = etNum.text.toString().toIntOrNull() ?: 0,
                        goodsSpecId = inputSpecification.getContentTag(),
                        deliveryType = goodsDetailsEntity.deliveryMode,
                        priceType = goodsDetailsEntity.priceType,
                        goodsPayType = goodsPayType,
                        deliveryAddress = address
                    )
                }
            }
        }
    }

    override fun createObserver() {
        mViewModel.apply {
            pickerData.observe(this@PlaceOrderActivity) {
                selectAddressUtil?.setPicker(it)
            }
            placeOrderEntity.observe(this@PlaceOrderActivity) {
                specificationBottomDialog.setData(it.goodsSpecDtoList)
                ztPointAdapter.setNewInstance(it.addressList)
            }

            deliveryAddressListEntity.observe(this@PlaceOrderActivity) {
                addressBookBottomDialog.setData(it)
            }
            editAddress.observe(this@PlaceOrderActivity) {
                mViewModel.getUserAddress()
            }

            createOrderSuccess.observe(this@PlaceOrderActivity) {
                if (it.orderId != null && it.orderId > 0) {
                    OrderDetailsActivity.startActivity(
                        this@PlaceOrderActivity,
                        orderId = it.orderId
                    )
                    if (!it.desc.isNullOrEmpty()) {
                        ActivityManger.currentActivity?.showToast(it.desc)
                    }
                    finish()
                } else {
                    if (!it.desc.isNullOrEmpty()) {
                        ActivityManger.currentActivity?.showToast(it.desc)
                    }
                }
            }
//            goodsPayTypeListEntity.observe(this@PlaceOrderActivity) {
//            }
        }
    }

    private val resultRefreshLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                showToast("保存成功")
                mViewModel.getUserAddress()
            }
        }
}