package com.gxy.common.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.gxy.common.R

/**
 * @author zhangyuxiang
 * @date 2024/2/28
 */

/**
 * 获取手机号码登陆协议style
 *
 * @param context
 * @return
 */
fun getPhoneLoginPrivacyStyle(
    context: Context, onClick: (() -> Unit)? = null
): SpannableStringBuilder {
    return context.run {
        val content: String = resources.getString(R.string.login_privacy)
        SpannableStringBuilder(content).apply {
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this@run, R.color.color_3662EC)),
                content.indexOf("《"),
                content.indexOf("》") + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        onClick?.invoke()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.run {
                            isUnderlineText = false
                            clearShadowLayer()
                        }
                    }
                },
                content.lastIndexOf("《"),
                content.lastIndexOf("》") + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}