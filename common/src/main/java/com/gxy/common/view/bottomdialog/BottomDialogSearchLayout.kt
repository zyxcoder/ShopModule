package com.gxy.common.view.bottomdialog

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import com.gxy.common.databinding.ViewBottomDialogSearchBinding

/**
 * @author zhangyuxiang
 * @date 2024/3/11
 */
class BottomDialogSearchLayout constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var mBinding: ViewBottomDialogSearchBinding

    var onSearchClickListener: ((searchContent: String) -> Unit)? = null

    init {
        mBinding = ViewBottomDialogSearchBinding.inflate(LayoutInflater.from(context), this, true)
        mBinding.apply {
            etSearch.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearchClickListener?.invoke(v.text.toString())
                    return@setOnEditorActionListener true
                }
                false
            }
            etSearch.doAfterTextChanged {
//                ivDelete.isVisible = it.toString().trim().isNotEmpty()
                onSearchClickListener?.invoke(etSearch.text.toString())
            }
            ivDelete.setOnClickListener {
                etSearch.setText("")
            }
            tvSearch.setOnClickListener {
                onSearchClickListener?.invoke(etSearch.text.toString())
            }
        }
    }
}