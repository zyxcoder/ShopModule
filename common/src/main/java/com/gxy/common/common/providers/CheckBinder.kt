package com.gxy.common.common.providers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.binder.QuickViewBindingItemBinder
import com.gxy.common.common.adapter.GroupTableAdapter
import com.gxy.common.databinding.ViewTypeCheckBinding
import com.gxy.common.entity.common.CheckItemEntity

/**
 * @author zhangyuxiang
 * @date 2024/4/11
 * checkBox模版
 */
class CheckBinder : QuickViewBindingItemBinder<CheckItemEntity, ViewTypeCheckBinding>() {
    override fun convert(holder: BinderVBHolder<ViewTypeCheckBinding>, data: CheckItemEntity) {
        holder.viewBinding.checkLayout.apply {
            initCheckLayout(
                title = data.lableName,
                checkYes = data.isCheck ?: false,
                isShowRequireds = data.isRequireds ?: false
            )
            onCheckedChangeListener = { check ->
                ((adapter.recyclerView.parent.parent as? RecyclerView)?.adapter as? GroupTableAdapter)?.data?.firstOrNull {
                    it.tables.contains(data)
                }?.tables?.filterIsInstance<CheckItemEntity>()
                    ?.findLast {
                        it == data
                    }?.isCheck = check
            }
        }
    }

    override fun onCreateViewBinding(
        layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int
    ) = ViewTypeCheckBinding.inflate(layoutInflater, parent, false)
}