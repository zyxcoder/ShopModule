package com.gxy.common.common.providers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.chad.library.adapter.base.binder.QuickViewBindingItemBinder
import com.gxy.common.R
import com.gxy.common.databinding.ViewTypeIconTitleBinding
import com.gxy.common.entity.common.IconTitleItemEntity

class IconTittleBinder :
    QuickViewBindingItemBinder<IconTitleItemEntity, ViewTypeIconTitleBinding>() {
    @SuppressLint("SetTextI18n")
    override fun convert(
        holder: BinderVBHolder<ViewTypeIconTitleBinding>,
        data: IconTitleItemEntity
    ) {
        holder.viewBinding.apply {
            tvTitle.text = data.lableName + "：" + data.lableValue
            tvTitle.tag = data.lableValue
            ivIcon.setImageResource(data.iconRes ?: R.drawable.ic_contract_num)
            line.isVisible = data.isShowBottomLine
        }
    }

    override fun onCreateViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ) = ViewTypeIconTitleBinding.inflate(layoutInflater, parent, false)
}