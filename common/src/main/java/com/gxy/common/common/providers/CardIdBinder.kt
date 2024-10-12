package com.gxy.common.common.providers

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import com.chad.library.adapter.base.binder.QuickViewBindingItemBinder
import com.gxy.common.R
import com.gxy.common.databinding.ViewTpeCardBinding
import com.gxy.common.entity.common.CardIdItemEntity
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.utils.ImageOptions
import com.zyxcoder.mvvmroot.utils.dpToPx
import com.zyxcoder.mvvmroot.utils.loadImage

/**
 * @author zhangyuxiang
 * @date 2024/4/18
 */
class CardIdBinder(
    private val onUpLoadRightSidePic: ((data: CardIdItemEntity, ivHodler: ImageView) -> Unit)? = null,
    private val onUpLoadReverseSidePic: ((data: CardIdItemEntity, ivHodler: ImageView) -> Unit)? = null
) : QuickViewBindingItemBinder<CardIdItemEntity, ViewTpeCardBinding>() {
    override fun convert(holder: BinderVBHolder<ViewTpeCardBinding>, data: CardIdItemEntity) {
        holder.viewBinding.apply {
            tvDesc.text = data.carDesc ?: context.getString(R.string.id_card_desc)
            tvCardRightSideDesc.text =
                data.cardRightSideDesc ?: context.getString(R.string.card_right_side)
            tvPostCardRightSideDesc.text =
                data.postCardRightSideDesc ?: context.getString(R.string.post_card_right_side)
            tvCardReverseSideDesc.text =
                data.cardReverseSideDesc ?: context.getString(R.string.card_reverse_side)
            tvPostCardReverseSideDesc.text =
                data.postCardReverseSideDesc ?: context.getString(R.string.post_card_reverse_side)

            ivRightSide.loadImage(data.rightCradImageUrl, ImageOptions().apply {
                (data.rightSidePlaceholder ?: R.drawable.ic_card_right_side).let {
                    placeholder = it
                    error = it
                    fallback = it
                }
            })

            ivReverseSide.loadImage(data.reverseCradImageUrl, ImageOptions().apply {
                (data.reverseSidePlaceholder ?: R.drawable.ic_card_reverse_side).let {
                    placeholder = it
                    error = it
                    fallback = it
                }
            })

            ivRightSideBoder.onContinuousClick {
                onUpLoadRightSidePic?.invoke(data, ivRightSide)
            }
            ivReverseSideBoder.onContinuousClick {
                onUpLoadReverseSidePic?.invoke(data, ivReverseSide)
            }

            clRoot.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = dpToPx(data.marginTop?.toFloat() ?: 0f).toInt()
                bottomMargin = dpToPx(data.marginBottom?.toFloat() ?: 0f).toInt()
            }
        }
    }

    override fun onCreateViewBinding(
        layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int
    ): ViewTpeCardBinding = ViewTpeCardBinding.inflate(layoutInflater, parent, false)
}