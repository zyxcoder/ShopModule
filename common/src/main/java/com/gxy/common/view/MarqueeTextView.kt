package com.gxy.common.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.graphics.toColorInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.gxy.common.R

/**
 * @author zhangyuxiang
 * @date 2024/3/14
 */
class MarqueeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        private const val GRAVITY_LEFT = 0
        private const val GRAVITY_RIGHT = 1
        private const val MARQUEE_SPEED = 3
    }

    private var textColor: Int
    private var textSize: Float
    private var gravity: Int

    //一开始滚动时是否完全展示
    private val marqueeStartTextCompleteDisplay: Boolean
    private var textPaint: TextPaint
    private var marqueeTextWidth = -1
    private var marqueeTextCurrentX = 0
    private var marqueeText: String = ""
    private var needMarquee = false
    private var marqueeAnim: ValueAnimator? = null
    private var textBaseLine = 0f
    private val textBetweenDistance by lazy {
        //当字符串滚动时,两个字符串之间的间距
        width * 3 / 4
    }

    init {
        val attr =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.MarqueeTextView,
                defStyleAttr,
                0
            )
        textColor =
            attr.getColor(
                R.styleable.MarqueeTextView_marqueeTextColor,
                "#333333".toColorInt()
            )
        textSize = attr.getDimension(R.styleable.MarqueeTextView_marqueeTextSize, 14f)
        gravity = attr.getInt(R.styleable.MarqueeTextView_marqueeGravity, GRAVITY_LEFT)
        marqueeStartTextCompleteDisplay = attr.getBoolean(
            R.styleable.MarqueeTextView_marqueeStartTextCompleteDisplay,
            false
        )
        val text = attr.getString(R.styleable.MarqueeTextView_marqueeText) ?: ""
        attr.recycle()
        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
            textSize = this@MarqueeTextView.textSize
            color = this@MarqueeTextView.textColor
        }
        (context as? LifecycleOwner)?.lifecycle?.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                marqueeAnim?.resume()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                marqueeAnim?.pause()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                marqueeAnim?.cancel()
                marqueeAnim = null
            }
        })
        setMarqueeText(text)
    }

    /**
     * 设置跑马灯颜色
     */
    fun setMarqueeTextColor(textColor: Int) {
        textPaint.apply {
            color = textColor
        }
    }

    /**
     * 设置跑马灯文字大小
     */
    fun setMarqueeTextSize(size: Float) {
        textPaint.apply {
            textSize = size
        }
    }

    /**
     * 设置跑马灯文字
     * 这个方法本该在post{}里执行的,在页面没渲染完成某些值没有初始化,会导致位置不对。但放在post{}里面就不能在XML里预览,若有显示问题,在外部anyView.post{setMarqueeText(text)}这样调用
     * @param text 跑马灯文字
     */
    fun setMarqueeText(text: String) {
        if (text.isEmpty() || text == marqueeText) {
            return
        }
        requestLayout()
        marqueeText = text
        marqueeTextWidth = textPaint.measureText(text).toInt()
        needMarquee = if (width == 0) {
            false
        } else {
            marqueeTextWidth > width
        }
        marqueeTextWidth += textBetweenDistance
        textBaseLine = textPaint.fontMetrics.let {
            // 文字基线=内容高度/2 + (字体bottom - top) / 2 - 字体bottom
            (if (height == 0) {
                //这里是为了能预览,正常情况不会走到这里来
                it.bottom - it.top
            } else {
                height.toFloat()
            } - paddingTop - paddingBottom) / 2 + (it.bottom - it.top) / 2 - it.bottom
        }
        marqueeTextCurrentX = 0
        if (needMarquee) {
            //展示不下,需要滚动
            startMarquee()
        } else {
            //直接展示下了,不用滚动
            invalidate()
        }
    }

    private fun startMarquee() {
        marqueeTextCurrentX = if (marqueeStartTextCompleteDisplay) {
            0
        } else {
            width + MARQUEE_SPEED
        }
        marqueeAnim = ValueAnimator.ofInt(0, marqueeTextWidth).apply {
            duration = marqueeTextWidth.toLong()
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            addUpdateListener {
                invalidate()
            }
            startDelay = 1500
            start()
        }
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            (textPaint.fontMetrics.bottom - textPaint.fontMetrics.top).toInt()
        )
    }

    override fun onDraw(canvas: Canvas) {
        marqueeTextCurrentX = if (marqueeTextCurrentX <= -marqueeTextWidth) {
            0
        } else {
            if (needMarquee) {
                marqueeTextCurrentX - MARQUEE_SPEED
            } else {
                when (gravity) {
                    GRAVITY_LEFT -> 0
                    GRAVITY_RIGHT -> width - marqueeTextWidth + textBetweenDistance
                    else -> throw IllegalArgumentException("no such gravity")
                }
            }
        }
        canvas.drawText(
            marqueeText,
            marqueeTextCurrentX.toFloat(),
            textBaseLine,
            textPaint
        )
        if (needMarquee) {
            canvas.drawText(
                marqueeText,
                (marqueeTextCurrentX + marqueeTextWidth).toFloat(),
                textBaseLine,
                textPaint
            )
        }
    }
}
