package com.gxy.common.view.refresh

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.gxy.common.R
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author zhangyuxiang
 * @date 2024/2/27
 * 带有下拉刷新头部和加载更多的下拉刷新控件
 */
@SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
class HFSmartRefreshLayout(
    context: Context, attrs: AttributeSet
) : SmartRefreshLayout(context, attrs) {


    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.HFSmartRefreshLayout)
        //设置不满一屏不可以上拉加载更多
        setEnableLoadMoreWhenContentNotFull(false)
        //设置刷新头和刷新底部view
        setRefreshHeader(RefreshHeader(context, attrs).apply {
            setDrawableMarginRight(2f)
            val backgroundColor = attr.getColor(
                R.styleable.HFSmartRefreshLayout_backgroundColor,
                ContextCompat.getColor(context, R.color.white)
            )
            setArrowDrawable(resources.getDrawable(R.drawable.ic_refresh_arrow).apply {
                setTint(
                    backgroundColor
                )
            })
            setProgressDrawable(resources.getDrawable(R.drawable.ic_refresh_progress).apply {
                setTint(
                    backgroundColor
                )
            })
            setEnableLastTime(true)
            setAccentColor(backgroundColor)
        })
        setRefreshFooter(RefreshFooter(context, attrs).apply {
            setDrawableMarginRight(2f)
        })
        attr.recycle()
    }
}