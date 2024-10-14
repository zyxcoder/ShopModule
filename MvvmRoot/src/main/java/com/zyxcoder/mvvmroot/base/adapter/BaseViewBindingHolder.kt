package com.zyxcoder.mvvmroot.base.adapter

import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Create by zyx_coder on 2023/1/9
 */
class BaseViewBindingHolder<VB : ViewBinding>(var viewBind: VB) : BaseViewHolder(viewBind.root)