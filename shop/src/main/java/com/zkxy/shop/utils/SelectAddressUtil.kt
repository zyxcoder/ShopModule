package com.zkxy.shop.utils

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.zkxy.shop.R
import com.zkxy.shop.entity.goods.JsonBean
import com.zkxy.shop.entity.goods.PickerEntity
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast

class SelectAddressUtil(val context: Context?) {
    private var optionsPickerBuilder: OptionsPickerView<Any>? = null
    var onSelectedAddressListener: ((address: String) -> Unit)? = null

    private var pickerEntity: PickerEntity? = null

    init {
        optionsPickerBuilder = OptionsPickerBuilder(context)
        { options1, options2, options3, v ->
            pickerEntity?.apply {
                val pickerViewText = (options1Items?.get(options1) as? JsonBean)?.pickerViewText
                val get = options2Items?.get(options1)?.get(options2)
                val get1 = options3Items?.get(options1)?.get(options2)?.get(options3)
                onSelectedAddressListener?.invoke("$pickerViewText$get$get1")
            }

        }.setLayoutRes(R.layout.pickerview_custom_options) {

            it.findViewById<TextView>(R.id.tvConfirm).onContinuousClick {
                optionsPickerBuilder?.returnData()
                optionsPickerBuilder?.dismiss()
            }

        }
            .setItemVisibleCount(5)
            .setLineSpacingMultiplier(3f)
            .setContentTextSize(14)
            .setSelectOptions(0, 0, 0)
            .setDividerColor(Color.TRANSPARENT)
            .setBgColor(Color.TRANSPARENT)
            .setOutSideCancelable(false).build()

//        optionsPickerBuilder?.setPicker(
//            pickerEntity.options1Items,
//            pickerEntity.options2Items,
//            pickerEntity.options3Items as List<MutableList<MutableList<Any>>>?
//        )
    }

    fun setPicker(pickerEntity: PickerEntity) {
        this.pickerEntity = pickerEntity
        optionsPickerBuilder?.setPicker(
            pickerEntity.options1Items,
            pickerEntity.options2Items,
            pickerEntity.options3Items as List<MutableList<MutableList<Any>>>?
        )
    }

    fun show() {
        if (pickerEntity == null) {
            context.showToast("数据错误")
            return
        }
        optionsPickerBuilder?.show()
    }
}