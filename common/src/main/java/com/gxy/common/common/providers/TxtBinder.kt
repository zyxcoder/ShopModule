package com.gxy.common.common.providers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chad.library.adapter.base.binder.QuickViewBindingItemBinder
import com.gxy.common.R
import com.gxy.common.databinding.ViewTypeTxtBinding
import com.gxy.common.entity.common.TxtItemEntity

/**
 * @author zhangyuxiang
 * @date 2024/4/11
 */

class TxtBinder : QuickViewBindingItemBinder<TxtItemEntity, ViewTypeTxtBinding>() {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BinderVBHolder<ViewTypeTxtBinding>, data: TxtItemEntity) {
        holder.viewBinding.tvTitle.apply {
            text = data.lableName + ":" + data.lableValue
            tag = data.lableValue
            setBackgroundResource(data.backgroundRes ?: R.drawable.round_solid_6d8df2_top_8)
        }
    }

    override fun onCreateViewBinding(
        layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int
    ): ViewTypeTxtBinding = ViewTypeTxtBinding.inflate(layoutInflater, parent, false)
}