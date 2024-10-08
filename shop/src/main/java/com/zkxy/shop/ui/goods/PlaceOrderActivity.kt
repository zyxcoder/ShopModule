package com.zkxy.shop.ui.goods

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.gxy.common.base.BaseViewBindActivity
import com.zkxy.shop.R
import com.zkxy.shop.common.dialog.AddressBookBottomDialog
import com.zkxy.shop.common.dialog.SpecificationBottomDialog
import com.zkxy.shop.databinding.ActivityPlaceOrderBinding
import com.zkxy.shop.databinding.LayoutShopReceiveKdBinding
import com.zkxy.shop.databinding.LayoutShopReceiveZtBinding
import com.zkxy.shop.entity.goods.GoodsDetailsEntity
import com.zkxy.shop.ext.doubleToTwoDecimalPlaceString
import com.zkxy.shop.ext.multiply
import com.zkxy.shop.ui.goods.adapter.ZtPointAdapter
import com.zkxy.shop.utils.SelectAddressUtil
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.utils.ImageOptions
import com.zyxcoder.mvvmroot.utils.dpToPx
import com.zyxcoder.mvvmroot.utils.loadImage


class PlaceOrderActivity : BaseViewBindActivity<PlaceOrderViewModel, ActivityPlaceOrderBinding>() {
    private var selectAddressUtil: SelectAddressUtil? = null
    private val ztPointAdapter by lazy { ZtPointAdapter() }
    private val specificationBottomDialog by lazy { SpecificationBottomDialog() }
    private val addressBookBottomDialog by lazy { AddressBookBottomDialog() }

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

        mViewBind.apply {
            tvGoodsName.text = goodsDetailsEntity.goodsName
            tvTopPoints.text = goodsDetailsEntity.goodsScorePrice.toString()
            tvTopMoney.text = goodsDetailsEntity.goodsMoneyPrice.doubleToTwoDecimalPlaceString()

            mViewBind.tvTopNum.text =
                if (goodsDetailsEntity.buyEmption == -1) "不限" else "每人限购${goodsDetailsEntity.buyEmption}件"
            inputSpecification.onContinuousClick {
                specificationBottomDialog.show(supportFragmentManager)
            }

            specificationBottomDialog.onItemClickListener = {
                inputSpecification.setSelectText(it.specName)
            }

            ivGoods.loadImage(
                goodsDetailsEntity.bannerPicDtoList?.first()?.picUrl,
                imageOptions = ImageOptions().apply { cornersRadius = dpToPx(4f).toInt() }
            )

            val goodsId = intent.getIntExtra(GOODS_ID, 15)

            if (goodsDetailsEntity.deliveryMode == 1) {//1:快递 2:自提
                mViewModel.initJsonData(this@PlaceOrderActivity)
                selectAddressUtil = SelectAddressUtil(this@PlaceOrderActivity)
                LayoutShopReceiveKdBinding.bind(vsKd.inflate()).apply {
                    selectAddressUtil?.onSelectedAddressListener = {
                        inputSelectAddress.setSelectText(it)
                    }

                    llAddressBook.onContinuousClick {
                        addressBookBottomDialog.show(supportFragmentManager)
                    }

                    inputSelectAddress.onContinuousClick {
                        selectAddressUtil?.show()
                    }
                }
                ivReceiveType.setBackgroundResource(R.drawable.ic_kd)

            } else {
                LayoutShopReceiveZtBinding.bind(vsZt.inflate()).apply {
                    rlvZt.adapter = ztPointAdapter
                }
                ivReceiveType.setBackgroundResource(R.drawable.ic_zt)
            }
            mViewModel.goodsStockAddressSearch(
                goodsId = goodsId,
                deliveryMode = goodsDetailsEntity.deliveryMode ?: 1
            )

            inputNum.onInputChangeListener = {
                gpPrice.visibility = View.VISIBLE
                val num = it?.toIntOrNull() ?: 0
                tvNum.text = num.toString()
                tvPoints.text =
                    (goodsDetailsEntity.goodsScorePrice ?: 0).multiply(num)
                tvMoney.text = (goodsDetailsEntity.goodsMoneyPrice ?: 0.0).multiply(num)
            }
        }

        ztPointAdapter.setOnItemChildClickListener { _, view, position ->
            val address = ztPointAdapter.data[position]
            when (view.id) {
                R.id.tvDialing -> {
                    startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${address.pickAddressTel}")))
                }
                else -> {}
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
        }
    }
}