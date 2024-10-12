package com.gxy.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.gxy.common.R
import com.gxy.common.databinding.ViewSelectTimeBinding
import com.gxy.common.ext.DateFormatType
import com.gxy.common.ext.dateToString
import com.gxy.common.ext.showDefaultTimePicker
import com.zyxcoder.mvvmroot.ext.showToast

/**
 * @author zhangyuxiang
 * @date 2024/4/15
 */
@SuppressLint("ResourceAsColor", "ResourceType")
class SelectTimeLayout(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var mBinding: ViewSelectTimeBinding

    var onSelectStartTimeClickListener: ((date: String?) -> Unit)? = null
    var onSelectEndTimeClickListener: ((date: String?) -> Unit)? = null

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.SelectTimeLayout)
        mBinding = ViewSelectTimeBinding.inflate(LayoutInflater.from(context), this, true)
        mBinding.apply {
            tvTitle.text = attr.getString(R.styleable.SelectTimeLayout_selectTimeLayoutTitle)
            viewLine.isVisible =
                attr.getBoolean(R.styleable.SelectTimeLayout_selectTimeLayoutNeedViewLine, false)
            tvStar.isVisible =
                attr.getBoolean(R.styleable.SelectTimeLayout_selectTimeLayoutIsRequireds, true)
            tvStartTime.setOnClickListener {
                context.showDefaultTimePicker {
                    val startDate = it.dateToString(
                        DateFormatType.TYPE_YMD
                    )
                    tvStartTime.text = startDate
                    onSelectStartTimeClickListener?.invoke(startDate)
                }
            }
            tvEndTime.setOnClickListener {
                context.showDefaultTimePicker {
                    val endDate = it.dateToString(
                        DateFormatType.TYPE_YMD
                    )
                    tvEndTime.text = endDate
                    onSelectEndTimeClickListener?.invoke(endDate)
                }
            }
            arrayListOf(tvStartTime, tvEndTime).forEach {
                it.setTextColor(
                    ContextCompat.getColor(
                        context,
                        attr.getColor(
                            R.styleable.SelectTimeLayout_selectTimeLayoutSelectColor,
                            R.color.color_333333
                        )
                    )
                )
            }
        }
        attr.recycle()
    }

    /**
     * 初始化SelectTimeLayout
     */
    fun initSelectTimeLayout(
        title: String?,
        isNeedViewLine: Boolean?,
        isRequireds: Boolean?,
        startTime: String?,
        endTime: String?,
        @ColorInt selectColor: Int = R.color.color_333333
    ) {
        mBinding.apply {
            tvTitle.text = title
            viewLine.isVisible = isNeedViewLine ?: false
            tvStar.isVisible = isRequireds ?: true
            tvStartTime.text = startTime
            tvEndTime.text = endTime
            arrayListOf(tvStartTime, tvEndTime).forEach {
                it.setTextColor(ContextCompat.getColor(context, selectColor))
            }
        }
    }

    fun getStartTime(): String {
        return mBinding.tvStartTime.text.toString()
    }

    fun getEndTime(): String {
        return mBinding.tvEndTime.text.toString()
    }

    /**
     * 判断是否为空，并且为空弹toast
     */
    fun contentIsEmptyAndShowToast(): Boolean {
        return mBinding.run {
            if (getStartTime().isEmpty()) {
                context.showToast(R.string.please_select_start_time)
            }
            if (getEndTime().isEmpty()) {
                context.showToast(R.string.please_select_end_time)
            }
            getStartTime().isEmpty() || getEndTime().isEmpty()
        }
    }
}