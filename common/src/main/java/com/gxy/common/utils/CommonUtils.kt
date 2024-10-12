package com.gxy.common.utils

import android.content.Context
import android.util.DisplayMetrics

/**
 * @author zhangyuxiang
 * @date 2024/3/11
 */

/**
 * 获取屏幕高度
 */
fun Context.getScreenHeight(): Int {
    return getDisplayMetrics(this).heightPixels
}
/**
 * 获取屏幕宽度
 */
fun Context.getScreenWidth(): Int {
    return getDisplayMetrics(this).widthPixels
}

fun getDisplayMetrics(context: Context): DisplayMetrics {
    return context.resources.displayMetrics
}