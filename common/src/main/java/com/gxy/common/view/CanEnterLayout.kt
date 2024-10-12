package com.gxy.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.gxy.common.R
import com.gxy.common.databinding.ViewCanEnterBinding
import com.zyxcoder.mvvmroot.ext.onContinuousClick

/**
 * @author zhangyuxiang
 * @date 2024/3/5
 */
class CanEnterLayout constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private var mBinding: ViewCanEnterBinding

    var onClickListener: (() -> Unit)? = null

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.CanEnterLayout)
        mBinding = ViewCanEnterBinding.inflate(LayoutInflater.from(context), this, true)
        mBinding.apply {
            ivTag.setImageDrawable(attr.getDrawable(R.styleable.CanEnterLayout_can_enter_title_drawable))
            tvTag.text = attr.getString(R.styleable.CanEnterLayout_can_enter_title_content)
            viewLineTag.isVisible =
                attr.getBoolean(R.styleable.CanEnterLayout_can_enter_needViewLine, true)
            clRoot.onContinuousClick {
                onClickListener?.invoke()
            }
        }
        attr.recycle()
    }
}