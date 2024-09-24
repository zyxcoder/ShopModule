package com.zkxy.shop.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zkxy.shop.R
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import kotlin.math.max


class FlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private var maxLine = Int.MAX_VALUE
    private var expanded: Boolean = false

    private var mToggleView: View? = null
    private val mAllViews = mutableListOf<View>()
    private val mAllLineViews = mutableListOf<MutableList<View>>()
    private val mLineHeight = mutableListOf<Int>()

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout)
        maxLine = typedArray.getInteger(R.styleable.FlowLayout_maxLine, Int.MAX_VALUE)
        mToggleView =
            LayoutInflater.from(context).inflate(R.layout.view_search_toggle, this, false).apply {
                onContinuousClick {
                    expanded = !expanded
                    requestLayout()
                }
            }
        typedArray.recycle()
    }


    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)

        var width = 0
        var height = 0
        var lineWidth = 0
        var lineHeight = 0
        var lines = 0
        val items = getShowShowView()

        for (child in items) {
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val lp = child.layoutParams as MarginLayoutParams
            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

            if (lineWidth + childWidth > sizeWidth - paddingLeft - paddingRight) {
                if (lines >= maxLine && !expanded) break

                width = max(width, lineWidth)
                lineWidth = childWidth
                height += lineHeight
                lineHeight = childHeight
                lines++
            } else {
                lineWidth += childWidth
                lineHeight = max(lineHeight, childHeight)
            }
            if (child == items.last() && (lines < maxLine || expanded)) {
                width = max(lineWidth, width)
                height += lineHeight
            }
        }

        mToggleView?.let { measureChild(it, widthMeasureSpec, heightMeasureSpec) }

        val w =
            if (modeWidth == MeasureSpec.EXACTLY) sizeWidth else width + paddingLeft + paddingRight
        val h =
            if (modeHeight == MeasureSpec.EXACTLY) sizeHeight else height + paddingTop + paddingBottom
        setMeasuredDimension(w, h)

    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        removeAllViewsInLayout()
        mAllLineViews.clear()
        mLineHeight.clear()

        var lineWidth = 0
        var lineHeight = 0
        var lineViews = mutableListOf<View>()
        val items = getShowShowView()

        for (child in items) {
            val lp = child.layoutParams as MarginLayoutParams

            val childWidth = child.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = child.measuredHeight + lp.topMargin + lp.bottomMargin

            if (lineWidth + childWidth > width - paddingLeft - paddingRight) {
                if (mAllLineViews.size >= maxLine && !expanded) break

                mLineHeight.add(lineHeight)
                mAllLineViews.add(lineViews)
                lineWidth = 0
                lineHeight = childHeight
                lineViews = mutableListOf()
            }
            lineWidth += childWidth
            lineHeight = max(lineHeight, childHeight)
            lineViews.add(child)

        }
        if (mAllLineViews.size < maxLine || expanded) {
            mLineHeight.add(lineHeight)
            mAllLineViews.add(lineViews)
        }

        mToggleView
            ?.takeIf { mAllLineViews.sumOf { it.size } < mAllViews.size }
            ?.let { adjustLineViews(it) }

        var left = paddingLeft
        var top = paddingTop
        var index = 0

        mAllLineViews.forEachIndexed { lineIndex, list ->
            list.forEach {
                val lp = it.layoutParams as MarginLayoutParams
                val lc = left + lp.leftMargin
                val tc = if (it != mToggleView) {
                    top + lp.topMargin
                } else {
                    top - lp.topMargin + (mLineHeight[lineIndex] / 2 - it.measuredHeight / 2)
                }
                val rc = lc + it.measuredWidth
                val bc = tc + it.measuredHeight
                addViewInLayout(it, index, lp)
                it.layout(lc, tc, rc, bc)

                left += it.measuredWidth + lp.leftMargin + lp.rightMargin
                index++
            }
            left = paddingLeft
            top += mLineHeight[lineIndex]
        }
    }

    private fun adjustLineViews(toggle: View) {
        val toggleLp = toggle.layoutParams as MarginLayoutParams
        val toggleWidth = toggle.measuredWidth + toggleLp.leftMargin + toggleLp.rightMargin

        val lastLine = mAllLineViews[mAllLineViews.size - 1]
        val lineWidth = lastLine.sumOf {
            val lp = it.layoutParams as MarginLayoutParams
            it.measuredWidth + lp.leftMargin + lp.rightMargin
        }
        val leftW = width - paddingLeft - paddingRight - lineWidth
        if (leftW < toggleWidth) {
            lastLine.removeAt(lastLine.size - 1)
            adjustLineViews(toggle)
        } else {
            lastLine.add(toggle)
        }
    }

    private fun getShowShowView(): MutableList<View> {
        return if (expanded) {
            mutableListOf<View>().apply {
                addAll(mAllViews)
            }
        } else mAllViews
    }

    override fun addView(child: View?, index: Int, params: LayoutParams?) {
        child?.let { mAllViews.add(child) }
        super.addView(child, index, params)
    }

    override fun removeView(view: View?) {
        mAllViews.remove(view)
        super.removeView(view)
    }

    override fun removeAllViews() {
        mAllViews.clear()
        expanded = false
        super.removeAllViews()
    }

    override fun removeViewAt(index: Int) {
        mAllViews.removeAt(index)
        super.removeViewAt(index)
    }

    override fun removeViews(start: Int, count: Int) {
        mAllViews.removeAll(mAllViews.subList(start, start + count))
        super.removeViews(start, count)
    }
}