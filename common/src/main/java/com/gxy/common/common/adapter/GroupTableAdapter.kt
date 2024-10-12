package com.gxy.common.common.adapter

import android.widget.ImageView
import com.gxy.common.R
import com.gxy.common.databinding.ItemTableBinding
import com.gxy.common.entity.common.CardIdItemEntity
import com.gxy.common.entity.common.GroupTableEntity
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder
import com.zyxcoder.mvvmroot.utils.dpToPx

/**
 * @author zhangyuxiang
 * @date 2024/4/11
 */
class GroupTableAdapter : BaseViewBindingAdapter<GroupTableEntity, ItemTableBinding>(
    ItemTableBinding::inflate, R.layout.item_table
) {
    var onUploadFileClickListener: ((ivHodler: ImageView) -> Unit)? = null
    var onUpLoadRightSidePic: ((data: CardIdItemEntity, ivHodler: ImageView) -> Unit)? = null
    var onUpLoadReverseSidePic: ((data: CardIdItemEntity, ivHodler: ImageView) -> Unit)? = null
    override fun convert(holder: BaseViewBindingHolder<ItemTableBinding>, item: GroupTableEntity) {
        holder.viewBind.apply {
            TableAdapter(
                onUploadFileClickListener, onUpLoadRightSidePic, onUpLoadReverseSidePic
            ).apply {
                rvTable.adapter = this
                setNewInstance(item.tables.toMutableList())
            }
            clRoot.setPadding(0, 0, 0, dpToPx((item.paddingBottom ?: 20).toFloat()).toInt())
        }
    }
}