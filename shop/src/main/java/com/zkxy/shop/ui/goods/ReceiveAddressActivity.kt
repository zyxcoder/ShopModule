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
        const val IS_EDIT = "is_edit"
        fun startActivity(context: Context?, isEdit: Boolean = false) {
            context?.startActivity(Intent(context, ReceiveAddressActivity::class.java).apply {
                putExtra(IS_EDIT, isEdit)
            })
        }
    }

    override fun init(savedInstanceState: Bundle?) {

        val isEdit = intent.getBooleanExtra(IS_EDIT, false)

        mViewModel.initJsonData(this)
        selectAddressUtil = SelectAddressUtil(this)
        mViewBind.apply {
            selectAddressUtil?.onSelectedAddressListener = {
                inputSelectAddress.setSelectText(it)
            }
            inputSelectAddress.onContinuousClick {
                selectAddressUtil?.show()
            }

            tvSave.onContinuousClick {
                if (!inputSelectAddress.contentIsEmptyAndShowToast() && !inputAddress.contentIsEmptyAndShowToast()) {
                    mViewModel.addUserAddress(
                        address = inputAddress.getContent(),
                        administrativeRegion = inputSelectAddress.getContent()
                    )
                }

            }
        }


    }

    override fun createObserver() {
        mViewModel.pickerData.observe(this) {
            selectAddressUtil?.setPicker(it)
        }
    }
}