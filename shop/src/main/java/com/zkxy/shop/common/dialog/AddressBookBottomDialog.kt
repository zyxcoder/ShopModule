package com.zkxy.shop.common.dialog

import androidx.fragment.app.FragmentManager
import com.gxy.common.base.BaseBottomSheetDialogFragment
import com.zkxy.shop.R
import com.zkxy.shop.databinding.DialogShopAddressListBinding
import com.zkxy.shop.entity.goods.AddressBookEntity
import com.zkxy.shop.ui.goods.adapter.SelectAddressAdapter
import com.zyxcoder.mvvmroot.ext.onContinuousClick

class AddressBookBottomDialog : BaseBottomSheetDialogFragment<DialogShopAddressListBinding>() {

    private val selectAddressAdapter by lazy { SelectAddressAdapter() }
    var addressListener: ((isEdit: Boolean, address: AddressBookEntity?) -> Unit)? = null
    var addressItemClickListener: ((address: AddressBookEntity?) -> Unit)? = null
    var addressAcquiesceListener: ((addressId: Int?, acquiesce: Int) -> Unit)? = null
    var addressDeleteListener: ((addressId: Int?) -> Unit)? = null

    override fun initView() {
        mViewBind.apply {
            rlv.adapter = selectAddressAdapter
            tvAddAddress.onContinuousClick {
                addressListener?.invoke(false, null)
                dismiss()
            }
        }
        selectAddressAdapter.setOnItemClickListener { _, _, position ->
            addressItemClickListener?.invoke(selectAddressAdapter.data[position])
            dismiss()
        }

        selectAddressAdapter.setOnItemChildClickListener { _, view, position ->

            when (view.id) {
                R.id.tvNormal -> {
                    selectAddressAdapter.apply {
                        data[position].apply {
                            if (acquiesce == 1) {
                                acquiesce = 0
                                addressAcquiesceListener?.invoke(
                                    addressId,
                                    0
                                )
                                notifyItemChanged(position, "check")
                            } else {
                                data.forEachIndexed { index, addressBookEntity ->
                                    addressBookEntity.acquiesce = if (index == position) {
                                        addressAcquiesceListener?.invoke(
                                            addressBookEntity.addressId,
                                            1
                                        )
                                        1
                                    } else {
                                        0
                                    }
                                    notifyDataSetChanged()
                                }
                            }
                        }
                    }
                    dismiss()
                }

                R.id.tvEditAddress -> {
                    addressListener?.invoke(true, selectAddressAdapter.data[position])
                    dismiss()
                }

                R.id.tvDeleteAddress -> {
                    addressDeleteListener?.invoke(selectAddressAdapter.data[position].addressId)
                    dismiss()
                }
            }
        }
    }

    override fun resetDialogHeightPercent(): Float = 0.5f

    fun setData(deliveryAddressEntities: MutableList<AddressBookEntity>) {
        selectAddressAdapter.setNewInstance(deliveryAddressEntities)
    }

    fun show(manager: FragmentManager) {
        super.show(manager, "AddressBookBottomDialog")
    }
}