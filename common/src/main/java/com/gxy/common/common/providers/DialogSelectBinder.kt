package com.gxy.common.common.providers

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.binder.QuickViewBindingItemBinder
import com.gxy.common.R
import com.gxy.common.common.adapter.GroupTableAdapter
import com.gxy.common.databinding.ViewTypeDialogSelectBinding
import com.gxy.common.entity.common.DialogSelectItemEntity
import com.gxy.common.view.bottomdialog.BottomListDialogFragment
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.utils.dpToPx
import com.zyxcoder.mvvmroot.utils.loadImage

/**
 * @author zhangyuxiang
 * @date 2024/4/11
 */
class DialogSelectBinder :
    QuickViewBindingItemBinder<DialogSelectItemEntity, ViewTypeDialogSelectBinding>() {
    override fun convert(
        holder: BinderVBHolder<ViewTypeDialogSelectBinding>, data: DialogSelectItemEntity
    ) {
        holder.viewBinding.apply {
            ivTag.loadImage(data.drawableRes)
            tvContent.text = data.selectContent
            tvContent.hint = context.getString(R.string.please_select) + "${data.lableName}"
            ivEnter.isVisible = data.showEnter ?: true
            clRoot.onContinuousClick {
                (context as? FragmentActivity)?.supportFragmentManager?.let { fragmentManager ->
                    BottomListDialogFragment.newInstance(
                        dialogTitle = context.getString(R.string.please_select) + "${data.lableName}",
                        dialogListInfo = data.dialogListInfo
                    ).apply {
                        onChooseClickListener = { bottomListEntity ->
                            tvContent.text = bottomListEntity.name
                            ((adapter.recyclerView.parent.parent as? RecyclerView)?.adapter as? GroupTableAdapter)?.data?.firstOrNull {
                                it.tables.contains(data)
                            }?.tables?.filterIsInstance<DialogSelectItemEntity>()
                                ?.findLast {
                                    it == data
                                }?.apply {
                                    selectId = bottomListEntity.id
                                    selectContent = bottomListEntity.name
                                }
                        }
                    }.show(fragmentManager)
                }
            }
            clRoot.updateLayoutParams<MarginLayoutParams> {
                topMargin = dpToPx(data.marginTop?.toFloat() ?: 0f).toInt()
                bottomMargin = dpToPx(data.marginBottom?.toFloat() ?: 0f).toInt()
            }
        }
    }

    override fun onCreateViewBinding(
        layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int
    ): ViewTypeDialogSelectBinding =
        ViewTypeDialogSelectBinding.inflate(layoutInflater, parent, false)
}