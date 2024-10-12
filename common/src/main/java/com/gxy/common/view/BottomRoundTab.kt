package com.gxy.common.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.gxy.common.R
import me.majiajie.pagerbottomtabstrip.internal.RoundMessageView
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem

/**
 * @author zhangyuxiang
 * @date 2024/2/22
 */
class BottomRoundTab @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseTabItem(
    context!!, attrs, defStyleAttr
) {
    private val mIcon: ImageView
    private val mTitle: TextView
    private val mMessages: RoundMessageView
    private var mDefaultDrawable: Drawable? = null
    private var mCheckedDrawable: Drawable? = null
    private var mDefaultTextColor = 0x56000000
    private var mCheckedTextColor = 0x56000000
    private var mChecked = false

    init {
        LayoutInflater.from(context).inflate(R.layout.bottom_round_tab, this, true)
        mIcon = findViewById(R.id.ivTab)
        mTitle = findViewById(R.id.tvTitle)
        mMessages = findViewById(R.id.roundMessage)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        val view = getChildAt(0)
        view?.setOnClickListener(l)
    }

    /**
     * 方便初始化的方法
     *
     * @param drawableRes        默认状态的图标
     * @param checkedDrawableRes 选中状态的图标
     * @param title              标题
     */
    fun initialize(
        @DrawableRes drawableRes: Int,
        @DrawableRes checkedDrawableRes: Int,
        title: String?
    ) {
        mDefaultDrawable = ContextCompat.getDrawable(context, drawableRes)
        mCheckedDrawable = ContextCompat.getDrawable(context, checkedDrawableRes)
        mTitle.text = title
    }

    override fun setChecked(checked: Boolean) {
        if (checked) {
            mIcon.setImageDrawable(mCheckedDrawable)
            mTitle.setTextColor(mCheckedTextColor)
        } else {
            mIcon.setImageDrawable(mDefaultDrawable)
            mTitle.setTextColor(mDefaultTextColor)
        }
        mChecked = checked
    }

    override fun setMessageNumber(number: Int) {
        mMessages.messageNumber = number
    }

    override fun setHasMessage(hasMessage: Boolean) {
        mMessages.setHasMessage(hasMessage)
    }

    override fun setTitle(title: String) {
        mTitle.text = title
    }

    override fun setDefaultDrawable(drawable: Drawable) {
        mDefaultDrawable = drawable
        if (!mChecked) {
            mIcon.setImageDrawable(drawable)
        }
    }

    override fun setSelectedDrawable(drawable: Drawable) {
        mCheckedDrawable = drawable
        if (mChecked) {
            mIcon.setImageDrawable(drawable)
        }
    }

    override fun getTitle(): String {
        return mTitle.text.toString()
    }

    fun setTextDefaultColor(@ColorInt color: Int) {
        mDefaultTextColor = color
    }

    fun setTextCheckedColor(@ColorInt color: Int) {
        mCheckedTextColor = color
    }
}