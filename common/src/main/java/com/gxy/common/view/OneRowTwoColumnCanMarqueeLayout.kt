package com.gxy.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.gxy.common.R
import com.gxy.common.databinding.ViewOneRowTwoCanMarqueeColumnBinding
import com.zyxcoder.mvvmroot.utils.spToPx

/**
 * @author zhangyuxiang
 * @date 2024/3/14
 */
class OneRowTwoColumnCanMarqueeLayout(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var mBinding: ViewOneRowTwoCanMarqueeColumnBinding

    init {

        val attr =
            context.obtainStyledAttributes(attrs, R.styleable.OneRowTwoColumnCanMarqueeLayout)
        mBinding =
            ViewOneRowTwoCanMarqueeColumnBinding.inflate(LayoutInflater.from(context), this, true)
        mBinding.apply {
            tvTitle.apply {
                text =
                    attr.getString(R.styleable.OneRowTwoColumnCanMarqueeLayout_oneRowTwoColumnCanMarqueeTitle)
                setTextColor(
                    attr.getColor(
                        R.styleable.OneRowTwoColumnCanMarqueeLayout_oneRowTwoColumnCanMarqueeTitleColor,
                        ContextCompat.getColor(context, R.color.color_666666)
                    )
                )
                textSize = attr.getDimension(
                    R.styleable.OneRowTwoColumnCanMarqueeLayout_oneRowTwoColumnCanMarqueeTitleSize,
                    12f
                )
            }
            tvContent.apply {
                setMarqueeTextSize(
                    attr.getDimension(
                        R.styleable.OneRowTwoColumnCanMarqueeLayout_oneRowTwoColumnCanMarqueeContentSize,
                        spToPx(12f)
                    )
                )
                setMarqueeTextColor(
                    attr.getColor(
                        R.styleable.OneRowTwoColumnCanMarqueeLayout_oneRowTwoColumnCanMarqueeContentColor,
                        ContextCompat.getColor(context, R.color.color_333333)
                    )
                )
                val content =
                    attr.getString(R.styleable.OneRowTwoColumnCanMarqueeLayout_oneRowTwoColumnCanMarqueeContent)
                post {
                    setMarqueeText(content ?: "")
                }
            }
            tvSubTitle.apply {
                text =
                    attr.getString(R.styleable.OneRowTwoColumnCanMarqueeLayout_oneRowTwoColumnCanMarqueeSubTitle)
                setTextColor(
                    attr.getColor(
                        R.styleable.OneRowTwoColumnCanMarqueeLayout_oneRowTwoColumnCanMarqueeSubTitleColor,
                        ContextCompat.getColor(context, R.color.color_666666)
                    )
                )
                textSize = attr.getDimension(
                    R.styleable.OneRowTwoColumnCanMarqueeLayout_oneRowTwoColumnCanMarqueeSubTitleSize,
                    12f
                )
                isVisible =
                    !attr.getString(R.styleable.OneRowTwoColumnCanMarqueeLayout_oneRowTwoColumnCanMarqueeSubTitle)
                        .isNullOrEmpty()

            }
            tvSubContent.apply {
                setMarqueeTextSize(
                    attr.getDimension(
                        R.styleable.OneRowTwoColumnCanMarqueeLayout_oneRowTwoColumnCanMarqueeSubContentSize,
                        spToPx(12f)
                    )
                )
                setMarqueeTextColor(
                    attr.getColor(
                        R.styleable.OneRowTwoColumnCanMarqueeLayout_oneRowTwoColumnCanMarqueeSubContentColor,
                        ContextCompat.getColor(context, R.color.color_333333)
                    )
                )
                val content =
                    attr.getString(R.styleable.OneRowTwoColumnCanMarqueeLayout_oneRowTwoColumnCanMarqueeSubContent)
                isVisible = !content.isNullOrEmpty() || tvSubTitle.isVisible
                if (isVisible) {
                    post {
                        setMarqueeText(content ?: "")
                    }
                }
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
                post {
                    setMarqueeText(content ?: "")
                }
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
                isVisible = !subContent.isNullOrEmpty() || tvSubTitle.isVisible
                if (isVisible) {
                    post {
                        setMarqueeText(subContent ?: "")
                    }
                }
            }
        }
    }
}