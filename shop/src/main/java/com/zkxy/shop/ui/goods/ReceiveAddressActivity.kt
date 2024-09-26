package com.zkxy.shop.ui.goods

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.gxy.common.base.BaseViewBindActivity
import com.zkxy.shop.databinding.ActivityReceiveAddressBinding
import com.zyxcoder.mvvmroot.ext.onContinuousClick

class ReceiveAddressActivity :
    BaseViewBindActivity<ReceiveAddressViewModel, ActivityReceiveAddressBinding>() {

    private var pvOptions: OptionsPickerView<Any>? = null

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, ReceiveAddressActivity::class.java))
        }
    }

    override fun init(savedInstanceState: Bundle?) {

        pvOptions = OptionsPickerBuilder(
            this
        ) { options1, options2, options3, _ ->

        }.setTitleText("城市选择")
            .setDividerColor(Color.BLACK)
            .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
            .setContentTextSize(20)
            .build()

        mViewBind.inputSelectAddress.onContinuousClick {
            mViewModel.initJsonData(this)
        }
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