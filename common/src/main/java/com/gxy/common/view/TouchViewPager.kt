package com.gxy.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * @author zhangyuxiang
 * @date 2024/2/23
 */
class TouchViewPager constructor(
    context: Context, attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?) =
        if (canScroll == true) super.onTouchEvent(ev) else false

    override fun onInterceptTouchEvent(ev: MotionEvent?) =
        if (canScroll == true) super.onInterceptTouchEvent(ev) else false


    private var canScroll: Boolean? = true

    fun setCanScroll(canScroll: Boolean) {
        this.canScroll = canScroll
    }

    /**
     * 避免跨页面切换闪烁
     */
    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item,false)
    }
}