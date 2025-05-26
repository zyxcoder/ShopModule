package com.gxy.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.gxy.common.R
import com.gxy.common.databinding.ViewSearchBinding
import com.zyxcoder.mvvmroot.ext.hideSoftInput
import com.zyxcoder.mvvmroot.ext.onContinuousClick

/**
 * @author zhangyuxiang
 * @date 2024/3/1
 */
class SearchLayout(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    var onSearchClickListener: ((searchContent: String) -> Unit)? = null
    var onValueChangeListener: ((searchContent: String) -> Unit)? = null
    var onFocusChangeListener: ((hasFocus: Boolean) -> Unit)? = null
    var onClearListener: (() -> Unit)? = null
    private var mBinding: ViewSearchBinding

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.SearchLayout)
        mBinding = ViewSearchBinding.inflate(LayoutInflater.from(context), this, true)
        mBinding.apply {
            etSearch.hint = attr.getString(R.styleable.SearchLayout_search_hint)
            etSearch.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearchClickListener?.invoke(v.text.toString())
                    hideSoftInput()
                    clearFocus()
                    return@setOnEditorActionListener true
                }
                false
            }
            etSearch.setOnFocusChangeListener { _, hasFocus ->
                onFocusChangeListener?.invoke(hasFocus)
            }
            ivDelete.onContinuousClick {
                onClearListener?.invoke()
                clearSearchContent()
            }
            val isNeedShowDeleteButton =
                attr.getBoolean(R.styleable.SearchLayout_is_need_show_delete_button, true)
            val isNeedShowSearchButton =
                attr.getBoolean(R.styleable.SearchLayout_is_need_show_search_button, true)
            ivSearch.isVisible = isNeedShowSearchButton
            etSearch.doAfterTextChanged {
                ivDelete.isVisible = isNeedShowDeleteButton && !it.isNullOrEmpty()
                onValueChangeListener?.invoke(it.toString())
            }
            tvSearch.onContinuousClick {
                onSearchClickListener?.invoke(etSearch.text.toString())
                hideSoftInput()
                clearFocus()
            }
        }
        attr.recycle()
    }

    fun getSearchContent(): String {
        return mBinding.etSearch.text.toString()
    }

    fun clearSearchContent() {
        mBinding.etSearch.text.clear()
    }

    fun setSearchHintContent(hintContent: String?) {
        mBinding.etSearch.hint = hintContent
    }

}