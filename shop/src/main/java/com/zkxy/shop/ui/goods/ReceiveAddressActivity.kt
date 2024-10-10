package com.zkxy.shop.ui.goods

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.gxy.common.base.BaseViewBindActivity
import com.zkxy.shop.databinding.ActivityReceiveAddressBinding
import com.zkxy.shop.entity.goods.AddressBookEntity
import com.zkxy.shop.utils.SelectAddressUtil
import com.zyxcoder.mvvmroot.ext.onContinuousClick

class ReceiveAddressActivity :
    BaseViewBindActivity<ReceiveAddressViewModel, ActivityReceiveAddressBinding>() {
    private var selectAddressUtil: SelectAddressUtil? = null

    companion object {
        const val IS_EDIT = "is_edit"
        const val BOOK_ENTITY = "book_entity"
        fun startActivityForResult(
            context: Context?,
            launcher: ActivityResultLauncher<Intent>,
            addressBookEntity: AddressBookEntity?,
            isEdit: Boolean = false
        ) {
            launcher.launch(Intent(context, ReceiveAddressActivity::class.java).apply {
                putExtra(IS_EDIT, isEdit)
                putExtra(BOOK_ENTITY, addressBookEntity)
            })
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        val isEdit = intent.getBooleanExtra(IS_EDIT, false)
        val addressBookEntity = intent.getSerializableExtra(BOOK_ENTITY) as? AddressBookEntity
        if (isEdit) {
            mViewBind.toobarLayout.setTitleContent("修改地址")
            addressBookEntity?.apply {
                mViewBind.inputSelectAddress.setSelectText(administrativeRegion)
                mViewBind.inputAddress.setSelectText(address)
            }
        }

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
                if (isEdit) {
                    if (!inputSelectAddress.contentIsEmptyAndShowToast() && !inputAddress.contentIsEmptyAndShowToast()) {
                        mViewModel.updateUserAddress(
                            address = inputAddress.getContent(),
                            administrativeRegion = inputSelectAddress.getContent(),
                            addressId = addressBookEntity?.addressId
                        )
                    }
                } else {
                    if (!inputSelectAddress.contentIsEmptyAndShowToast() && !inputAddress.contentIsEmptyAndShowToast()) {
                        mViewModel.addUserAddress(
                            address = inputAddress.getContent(),
                            administrativeRegion = inputSelectAddress.getContent()
                        )
                    }
                }
            }
        }
    }

    override fun createObserver() {
        mViewModel.apply {
            pickerData.observe(this@ReceiveAddressActivity) {
                selectAddressUtil?.setPicker(it)
            }

            addAddressSuccess.observe(this@ReceiveAddressActivity) {
                if (it) {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }
}