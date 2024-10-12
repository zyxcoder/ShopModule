package com.gxy.common.common.providers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.chad.library.adapter.base.binder.QuickViewBindingItemBinder
import com.gxy.common.databinding.ViewTypeTitleBinding
import com.gxy.common.entity.common.TitleItemEntity

/**
 * @author zhangyuxiang
 * @date 2024/4/11
 */
class TitleBinder : QuickViewBindingItemBinder<TitleItemEntity, ViewTypeTitleBinding>() {
    override fun convert(holder: BinderVBHolder<ViewTypeTitleBinding>, data: TitleItemEntity) {
        holder.viewBinding.apply {
            tvLableName.text = data.lableName
            tvLableValue.text = data.lableValue
            viewLineGray.isVisible = data.isShowBottomLine ?: false
        }
    }

    override fun onCreateViewBinding(
        layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int
    ) = ViewTypeTitleBinding.inflate(layoutInflater, parent, false)
}