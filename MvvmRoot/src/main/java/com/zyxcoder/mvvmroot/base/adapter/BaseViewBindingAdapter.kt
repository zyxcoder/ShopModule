package com.zyxcoder.mvvmroot.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * Create by zyx_coder on 2023/1/9
 */
abstract class BaseViewBindingAdapter<T, VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB, layoutResId: Int
) : BaseQuickAdapter<T, BaseViewBindingHolder<VB>>(layoutResId = layoutResId) {
    override fun onCreateDefViewHolder(
        parent: ViewGroup, viewType: Int
    ): BaseViewBindingHolder<VB> {
        return BaseViewBindingHolder(inflate(LayoutInflater.from(parent.context), parent, false))
    }
}