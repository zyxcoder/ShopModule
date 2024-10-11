package com.zkxy.shop.ext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context


/**
 * 复制文字到剪切板
 * @param text 复制的文字
 */
fun Context.copyText(text: String) {
    (getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.setPrimaryClip(
        ClipData.newPlainText("text", text)
    )
}