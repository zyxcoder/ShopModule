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

    override fun initView() {
        mViewBind.apply {
            rlv.adapter = selectAddressAdapter
            tvAddAddress.onContinuousClick {
                ReceiveAddressActivity.startActivity(context)
                dismiss()
            }
        }
        selectAddressAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.tvNormal -> {
                    selectAddressAdapter.apply {
                        data[position].apply {
                            if (acquiesce == 1) {
                                acquiesce = 0
                                notifyItemChanged(position, "check")
                            } else {
                                data.forEachIndexed { index, addressBookEntity ->
                                    addressBookEntity.acquiesce = if (index == position) 1 else 0
                                    notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }

                R.id.tvEditAddress -> {
                    ReceiveAddressActivity.startActivity(context, isEdit = true)
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