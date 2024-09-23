package com.zkxy.shop.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.zkxy.shop.R
import com.zkxy.shop.databinding.ViewShopSearchBinding
import com.zyxcoder.mvvmroot.ext.hideSoftInput
import com.zyxcoder.mvvmroot.ext.onContinuousClick

/**
 * @author zhangyuxiang
 * @date 2024/9/20
 */
class ShopSearchView(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    var onSearchClickListener: ((searchContent: String) -> Unit)? = null
    var onValueChangeListener: ((searchContent: String) -> Unit)? = null
    private var mBinding: ViewShopSearchBinding

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.ShopSearchView)
        mBinding = ViewShopSearchBinding.inflate(LayoutInflater.from(context), this, true)
        mBinding.apply {
            etSearch.hint = attr.getString(R.styleable.ShopSearchView_search_hint)
            etSearch.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearchClickListener?.invoke(v.text.toString())
                    hideSoftInput()
                    clearFocus()
                    return@setOnEditorActionListener true
                }
                false
            }
            tvSearch.onContinuousClick {
                onSearchClickListener?.invoke(etSearch.text.toString())
                hideSoftInput()
                clearFocus()
            }
            etSearch.doAfterTextChanged {
                ivSearchDelete.isVisible = !it.isNullOrEmpty()
                onValueChangeListener?.invoke(it.toString())
            }
            ivSearchDelete.onContinuousClick {
                clearSearchContent()
            }
        }
        attr.recycle()
    }

    fun getSearchContent(): String {
        return mBinding.etSearch.text.toString()
    }

    fun setSearchContent(content: String?) {
        mBinding.etSearch.setText(content)
    }

    fun setSearchHintContent(hintContent: String?) {
        mBinding.etSearch.hint = hintContent
    }

    fun clearSearchContent() {
        mBinding.etSearch.text.clear()
    }

}