package com.zkxy.shop.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.zkxy.shop.R
import com.zkxy.shop.databinding.DialogConfirmAddressBinding
import com.zkxy.shop.entity.order.ConfirmAddressEntity
import com.zkxy.shop.ui.goods.adapter.ConfirmAddressAdapter
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast
import com.zyxcoder.mvvmroot.utils.dpToPx

class ConfirmAddressDialog(context: Context) : Dialog(context, R.style.MyBottomDialogTheme) {

    private val confirmAddressAdapter by lazy { ConfirmAddressAdapter() }
    private var oldPosition = -1
    var onConfirmAddressClickListener: ((confirmAddressEntity: ConfirmAddressEntity) -> Unit)? =
        null

    private lateinit var mViewBind: DialogConfirmAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBind = DialogConfirmAddressBinding.inflate(layoutInflater)
        setContentView(mViewBind.root)
        window?.apply {
            setGravity(Gravity.BOTTOM)
            setWindowAnimations(R.style.shop_main_menu_animStyle);
            attributes.apply {
                height = dpToPx(416f).toInt()
                width = MATCH_PARENT
                attributes = this
            }
        }

        mViewBind.apply {
            rlv.adapter = confirmAddressAdapter
            tvCancel.onContinuousClick {
                dismiss()
            }
            tvConfirm.onContinuousClick {
                val addressEntities = confirmAddressAdapter.data.find { it.isCheck }
                if (addressEntities == null) {
                    context.showToast("请选择自提点")
                } else {
                    onConfirmAddressClickListener?.invoke(addressEntities)
                    dismiss()
                }
            }
        }
        confirmAddressAdapter.apply {
            setOnItemClickListener { _, _, position ->
                val data = data
                if (oldPosition != position) {
                    data[position].isCheck = true
                    notifyItemChanged(position)
                    if (oldPosition >= 0) {
                        data[oldPosition].isCheck = false
                        notifyItemChanged(oldPosition)
                    }
                }
                oldPosition = position
            }
        }
    }

    fun setData(list: MutableList<ConfirmAddressEntity>, deliveryCode: String) {
        confirmAddressAdapter.setList(list)
        mViewBind.tvDeliveryCode.text = deliveryCode
    }
}