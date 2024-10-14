package com.zyxcoder.mvvmroot.ext

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import com.zyxcoder.mvvmroot.R

/**
 * 弹出软键盘
 */
fun View.showSoftInput() {
    requestFocus()
    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.RESULT_SHOWN)
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

/**
 * 隐藏软键盘
 */
fun View.hideSoftInput() {
    val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * 获取StringRes
 */
fun View.getString(@StringRes resId: Int): String = context.resources.getString(resId)

/**
 * 连续点击第一次生效，间隔超过500ms
 */
fun View.onContinuousClick(callback: (View) -> Unit) {
    setOnClickListener {
        val current = System.currentTimeMillis()
        val last = getTag(R.id.on_continuous_click_view_id) as? Long ?: 0L
        if (current - last > 500L) {
            callback.invoke(this)
        }
        setTag(R.id.on_continuous_click_view_id, current)
    }
}
