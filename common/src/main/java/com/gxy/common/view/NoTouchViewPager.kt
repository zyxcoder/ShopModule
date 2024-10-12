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
class NoTouchViewPager constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?) = false

    override fun onInterceptTouchEvent(ev: MotionEvent?) = false
}