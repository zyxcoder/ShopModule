package com.gxy.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.gxy.common.R
import com.gxy.common.databinding.ViewOneRowTwoColumnBinding

/**
 * @author zhangyuxiang
 * @date 2024/3/14
 */
class OneRowTwoColumnLayout(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var mBinding: ViewOneRowTwoColumnBinding

    init {

        val attr = context.obtainStyledAttributes(attrs, R.styleable.OneRowTwoColumnLayout)
        mBinding = ViewOneRowTwoColumnBinding.inflate(LayoutInflater.from(context), this, true)
        mBinding.apply {
            tvTitle.apply {
                text = attr.getString(R.styleable.OneRowTwoColumnLayout_oneRowTwoColumnTitle)
                setTextColor(
                    attr.getColor(
                        R.styleable.OneRowTwoColumnLayout_oneRowTwoColumnTitleColor,
                        ContextCompat.getColor(context, R.color.color_666666)
                    )
                )
                textSize = attr.getDimension(
                    R.styleable.OneRowTwoColumnLayout_oneRowTwoColumnTitleSize, 12f
                )
            }
            tvContent.apply {
                text = attr.getString(R.styleable.OneRowTwoColumnLayout_oneRowTwoColumnContent)
                setTextColor(
                    attr.getColor(
                        R.styleable.OneRowTwoColumnLayout_oneRowTwoColumnContentColor,
                        ContextCompat.getColor(context, R.color.color_333333)
                    )
                )
                textSize = attr.getDimension(
                    R.styleable.OneRowTwoColumnLayout_oneRowTwoColumnContentSize, 12f
                )
            }
            tvSubTitle.apply {
                text = attr.getString(R.styleable.OneRowTwoColumnLayout_oneRowTwoColumnSubTitle)
                setTextColor(
                    attr.getColor(
                        R.styleable.OneRowTwoColumnLayout_oneRowTwoColumnSubTitleColor,
                        ContextCompat.getColor(context, R.color.color_666666)
                    )
                )
                textSize = attr.getDimension(
                    R.styleable.OneRowTwoColumnLayout_oneRowTwoColumnSubTitleSize, 12f
                )
                isVisible =
                    !attr.getString(R.styleable.OneRowTwoColumnLayout_oneRowTwoColumnSubTitle)
                        .isNullOrEmpty()
            }
            tvSubContent.apply {
                text = attr.getString(R.styleable.OneRowTwoColumnLayout_oneRowTwoColumnSubContent)
                setTextColor(
                    attr.getColor(
                        R.styleable.OneRowTwoColumnLayout_oneRowTwoColumnSubContentColor,
                        ContextCompat.getColor(context, R.color.color_333333)
                    )
                )
                textSize = attr.getDimension(
                    R.styleable.OneRowTwoColumnLayout_oneRowTwoColumnSubContentSize, 12f
                )
                isVisible =
                    !attr.getString(R.styleable.OneRowTwoColumnLayout_oneRowTwoColumnSubContent)
                        .isNullOrEmpty() || tvSubTitle.isVisible
            }
        }
        attr.recycle()
    }

    fun setTitle(title: String?) {
        mBinding.apply {
            tvTitle.apply {
                text = title
            }
        }
    }

    fun setContent(content: String?) {
        mBinding.apply {
            tvContent.apply {
                text = content
            }
        }
    }

    fun setSubTitle(subTitle: String?) {
        mBinding.apply {
            tvSubTitle.apply {
                text = subTitle
                isVisible = !subTitle.isNullOrEmpty()
            }
        }
    }

    fun setSubContent(subContent: String?) {
        mBinding.apply {
            tvSubContent.apply {
                text = subContent
                isVisible = !subContent.isNullOrEmpty() || tvSubTitle.isVisible
            }
        }
    }
}