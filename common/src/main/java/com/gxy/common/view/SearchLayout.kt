package com.gxy.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import com.gxy.common.R
import com.gxy.common.databinding.ViewSearchBinding
import com.zyxcoder.mvvmroot.ext.hideSoftInput

/**
 * @author zhangyuxiang
 * @date 2024/3/1
 */
class SearchLayout constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    var onSearchClickListener: ((searchContent: String) -> Unit)? = null
    var onValueChangeListener: ((searchContent: String) -> Unit)? = null
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
            etSearch.doAfterTextChanged {
                onValueChangeListener?.invoke(it.toString())
            }
        }
        attr.recycle()
    }

    fun getSearchContent(): String {
        return mBinding.etSearch.text.toString()
    }

    fun setSearchHintContent(hintContent: String?) {
        mBinding.etSearch.hint = hintContent
    }

}