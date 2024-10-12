package com.gxy.common.common.providers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.binder.QuickViewBindingItemBinder
import com.gxy.common.common.adapter.GroupTableAdapter
import com.gxy.common.databinding.ViewTypeSelectTimeBinding
import com.gxy.common.entity.common.SelectTimeItemEntity

/**
 * @author zhangyuxiang
 * @date 2024/4/15
 */
class SelectTimeBinder :
    QuickViewBindingItemBinder<SelectTimeItemEntity, ViewTypeSelectTimeBinding>() {
    override fun convert(
        holder: BinderVBHolder<ViewTypeSelectTimeBinding>, data: SelectTimeItemEntity
    ) {
        holder.viewBinding.selectTime.apply {
            initSelectTimeLayout(
                title = data.lableName,
                isRequireds = data.isRequireds ?: false,
                isNeedViewLine = data.isShowBottomLine ?: false,
                startTime = data.startTime,
                endTime = data.endTime,
                selectColor = data.selectColor
            )
            onSelectStartTimeClickListener = {
                ((adapter.recyclerView.parent.parent as? RecyclerView)?.adapter as? GroupTableAdapter)?.data?.firstOrNull {
                    it.tables.contains(data)
                }?.tables?.filterIsInstance<SelectTimeItemEntity>()?.findLast {
                    it == data
                }?.startTime = it
            }
            onSelectEndTimeClickListener = {
                ((adapter.recyclerView.parent.parent as? RecyclerView)?.adapter as? GroupTableAdapter)?.data?.firstOrNull {
                    it.tables.contains(data)
                }?.tables?.filterIsInstance<SelectTimeItemEntity>()?.findLast {
                    it == data
                }?.endTime = it
            }
        }
    }

    override fun onCreateViewBinding(
        layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int
    ) = ViewTypeSelectTimeBinding.inflate(layoutInflater, parent, false)
}