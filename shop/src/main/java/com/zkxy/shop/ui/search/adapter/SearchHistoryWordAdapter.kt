package com.zkxy.shop.ui.search.adapter

import com.zkxy.shop.R
import com.zkxy.shop.databinding.ViewSearchWordBinding
import com.zkxy.shop.entity.search.SearchWordHistoryEntity
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder
import com.zyxcoder.mvvmroot.ext.onContinuousClick

/**
 * @author zhangyuxiang
 * @date 2024/9/20
 */
class SearchHistoryWordAdapter :
    BaseViewBindingAdapter<SearchWordHistoryEntity, ViewSearchWordBinding>(
        ViewSearchWordBinding::inflate, R.layout.view_search_word
    ) {
    var onWorkClickListener: ((searchWordHistoryEntity: SearchWordHistoryEntity) -> Unit)? = null
    override fun convert(
        holder: BaseViewBindingHolder<ViewSearchWordBinding>,
        item: SearchWordHistoryEntity
    ) {
        holder.viewBind.apply {
            root.onContinuousClick {
                onWorkClickListener?.invoke(item)
            }
            tvWord.text = item.searchWord
        }
    }
}