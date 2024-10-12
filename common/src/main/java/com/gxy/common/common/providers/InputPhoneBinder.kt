package com.gxy.common.common.providers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.chad.library.adapter.base.binder.QuickViewBindingItemBinder
import com.gxy.common.databinding.ViewInputPhoneBinding
import com.gxy.common.entity.common.InputPhoneItemEntity

class InputPhoneBinder : QuickViewBindingItemBinder<InputPhoneItemEntity, ViewInputPhoneBinding>() {
    override fun convert(
        holder: BinderVBHolder<ViewInputPhoneBinding>,
        data: InputPhoneItemEntity
    ) {
        holder.viewBinding.apply {
            tvTitle.text = data.title
            etPhone.hint = data.hintContent
            etPhone.setText(data.phone)
            tvStar.isVisible = data.isRequireds ?: false
            etPhone.doAfterTextChanged {
                data.result = etPhone.phoneText
            }
            etPhone.isEnabled = !(data.isCanEdit ?: false)
        }
    }

    override fun onCreateViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ViewInputPhoneBinding = ViewInputPhoneBinding.inflate(layoutInflater, parent, false)
}