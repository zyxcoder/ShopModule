package com.zkxy.shop.common.dialog

import androidx.fragment.app.FragmentManager
import com.gxy.common.base.BaseBottomSheetDialogFragment
import com.zkxy.shop.R
import com.zkxy.shop.databinding.DialogShopAddressListBinding
import com.zkxy.shop.entity.goods.AddressBookEntity
import com.zkxy.shop.ui.goods.ReceiveAddressActivity
import com.zkxy.shop.ui.goods.adapter.SelectAddressAdapter
import com.zyxcoder.mvvmroot.ext.onContinuousClick

class AddressBookBottomDialog : BaseBottomSheetDialogFragment<DialogShopAddressListBinding>() {

    private val selectAddressAdapter by lazy { SelectAddressAdapter() }

    private val addressList = mutableListOf(
        AddressBookEntity(isCheck = true),
        AddressBookEntity(isCheck = false),
        AddressBookEntity(isCheck = false),
        AddressBookEntity(isCheck = false),
        AddressBookEntity(isCheck = false),
        AddressBookEntity(isCheck = false),
        AddressBookEntity(isCheck = false),
        AddressBookEntity(isCheck = false),
        AddressBookEntity(isCheck = false)
    )

    override fun initView() {
        mViewBind.apply {
            rlv.adapter = selectAddressAdapter
            tvAddAddress.onContinuousClick {
                ReceiveAddressActivity.startActivity(context)
                dismiss()
            }
        }
        selectAddressAdapter.setList(addressList)
        selectAddressAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.tvNormal -> {
                    selectAddressAdapter.apply {
                        data[position].apply {
                            if (isCheck) {
                                isCheck = false
                                notifyItemChanged(position, "check")
                            } else {
                                data.forEachIndexed { index, addressBookEntity ->
                                    addressBookEntity.isCheck = index == position
                                    notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }

                R.id.tvEditAddress -> {
                    ReceiveAddressActivity.startActivity(context)
                    dismiss()
                }
            }
        }
    }

    override fun resetDialogHeightPercent(): Float = 0.5f

    fun show(manager: FragmentManager) {
        super.show(manager, "AddressBookBottomDialog")
    }
}