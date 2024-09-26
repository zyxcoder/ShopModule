package com.zkxy.shop.ui.goods

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.gxy.common.base.BaseViewBindActivity
import com.zkxy.shop.R
import com.zkxy.shop.common.dialog.AddressBookBottomDialog
import com.zkxy.shop.common.dialog.SpecificationBottomDialog
import com.zkxy.shop.databinding.ActivityPlaceOrderBinding
import com.zkxy.shop.databinding.LayoutShopReceiveKdBinding
import com.zkxy.shop.databinding.LayoutShopReceiveZtBinding
import com.zkxy.shop.ui.goods.adapter.ZtPointAdapter
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.utils.ImageOptions
import com.zyxcoder.mvvmroot.utils.dpToPx
import com.zyxcoder.mvvmroot.utils.loadImage

class PlaceOrderActivity : BaseViewBindActivity<PlaceOrderViewModel, ActivityPlaceOrderBinding>() {
    private var pvOptions: OptionsPickerView<Any>? = null
    private val ztPointAdapter by lazy { ZtPointAdapter() }
    private val specificationBottomDialog by lazy { SpecificationBottomDialog() }
    private val addressBookBottomDialog by lazy { AddressBookBottomDialog() }

    companion object {
        const val IS_PICK = "isPick"
        fun startActivity(context: Context, isKd: Boolean = false) {
            context.startActivity(Intent(context, PlaceOrderActivity::class.java).apply {
                putExtra(IS_PICK, isKd)
            })
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        mViewBind.apply {
            inputSpecification.onContinuousClick {
                specificationBottomDialog.show(supportFragmentManager)
            }

            ivGoods.loadImage(
                "https://gd-hbimg.huaban.com/4bd2502a1859e4bcc9d0afeda5b8851d98a267dd18c54-81OUAo_fw1200webp",
                imageOptions = ImageOptions().apply { cornersRadius = dpToPx(4f).toInt() }
            )
            if (intent.getBooleanExtra(IS_PICK, false)) {
                LayoutShopReceiveKdBinding.bind(vsKd.inflate()).apply {
                    llAddressBook.onContinuousClick {
                        addressBookBottomDialog.show(supportFragmentManager)
                    }

                    inputSelectAddress.onContinuousClick {
                        mViewModel.initJsonData(this@PlaceOrderActivity)
                    }
                }
                ivReceiveType.setBackgroundResource(R.drawable.ic_kd)
            } else {
                LayoutShopReceiveZtBinding.bind(vsZt.inflate()).apply {
                    rlvZt.adapter = ztPointAdapter
                }
                ztPointAdapter.setList(mutableListOf("", "", "", "", "", "", "", "", "", "", ""))
                ivReceiveType.setBackgroundResource(R.drawable.ic_zt)
            }
        }

        pvOptions = OptionsPickerBuilder(
            this
        ) { options1, options2, options3, _ ->

        }.setTitleText("城市选择")
            .setDividerColor(Color.BLACK)
            .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
            .setContentTextSize(20)
            .build()

    }

    override fun createObserver() {
        mViewModel.pickerData.observe(this) {
            pvOptions?.setPicker(
                it.options1Items, it.options2Items, it.options3Items as List<MutableList<MutableList<Any>>>?
            )
            pvOptions?.show()
        }

    }
}