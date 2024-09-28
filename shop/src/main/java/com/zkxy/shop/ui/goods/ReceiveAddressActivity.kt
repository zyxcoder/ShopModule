package com.zkxy.shop.ui.goods

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gxy.common.base.BaseViewBindActivity
import com.zkxy.shop.databinding.ActivityReceiveAddressBinding
import com.zkxy.shop.utils.SelectAddressUtil
import com.zyxcoder.mvvmroot.ext.onContinuousClick

class ReceiveAddressActivity :
    BaseViewBindActivity<ReceiveAddressViewModel, ActivityReceiveAddressBinding>() {
    private var selectAddressUtil: SelectAddressUtil? = null

    companion object {
        fun startActivity(context: Context?) {
            context?.startActivity(Intent(context, ReceiveAddressActivity::class.java))
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        mViewModel.initJsonData(this)
        selectAddressUtil = SelectAddressUtil(this)
        selectAddressUtil?.onSelectedAddressListener = {
            mViewBind.inputSelectAddress.setSelectText(it)
        }
        mViewBind.inputSelectAddress.onContinuousClick {
            selectAddressUtil?.show()
        }
    }

    override fun createObserver() {
        mViewModel.pickerData.observe(this) {
            selectAddressUtil?.setPicker(it)
        }
    }
}