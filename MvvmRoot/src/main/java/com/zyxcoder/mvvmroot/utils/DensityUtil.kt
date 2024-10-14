package com.zyxcoder.mvvmroot.utils

import android.content.res.Resources

/**
 * Create by zyx_coder on 2022/12/8
 */
fun dpToPx(dp: Float): Float {
    return dp * Resources.getSystem().displayMetrics.density
}

fun pxToDp(px: Float): Float {
    return px / Resources.getSystem().displayMetrics.density
}

fun spToPx(sp: Float): Float {
    return sp * Resources.getSystem().displayMetrics.scaledDensity
}

fun pxToSp(px: Float): Float {
    return px / Resources.getSystem().displayMetrics.scaledDensity
}

fun getDensity(): Float = Resources.getSystem().displayMetrics.density