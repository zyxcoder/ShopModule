package com.gxy.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.gxy.common.R
import com.gxy.common.databinding.ViewInputPhoneBinding
import com.gxy.common.ext.isLegalPhoneNumber
import com.zyxcoder.mvvmroot.ext.showToast

class InputPhoneLayout(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var mBinding: ViewInputPhoneBinding
    private var hintContent: String? = null

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.InputPhoneLayout)
        mBinding = ViewInputPhoneBinding.inflate(LayoutInflater.from(context), this, true)
        mBinding.apply {
            tvStar.isVisible = attr.getBoolean(R.styleable.InputPhoneLayout_required, false)
            etPhone.isEnabled = attr.getBoolean(R.styleable.InputPhoneLayout_is_edit, true)
            hintContent = attr.getString(R.styleable.InputPhoneLayout_input_phone_hint)
            tvTitle.text =
                attr.getString(R.styleable.InputPhoneLayout_input_phone_title) ?: context.getString(
                    R.string.phone_num
                )
            etPhone.hint = hintContent
        }
    }

    fun getPhone(): String {
        return mBinding.etPhone.phoneText
    }

    fun setPhone(phone: String?) {
        mBinding.etPhone.setText(phone)
    }

    /**
     * 判断是否为空，并且为空弹toast
     */
    fun contentIsEmptyAndShowToast(): Boolean {
        val phoneText = mBinding.etPhone.phoneText
        if (phoneText.isEmpty()) {
            hintContent?.let { context.showToast(it) }
            return true
        }
        if (!phoneText.isLegalPhoneNumber()) {
            context.showToast(R.string.not_legal_phonenumber)
            return true
        }
        return false
    }
}