package com.gxy.common.ext

import java.text.SimpleDateFormat

/**
 * @author zhangyuxiang
 * @date 2024/3/19
 */

fun String?.safeToFloat(): Float {
    return this?.toFloatOrNull() ?: 0.0f
}

fun String?.getFromIdForServerEntity(): Int {
    return this?.substringBefore("-")?.toIntOrNull() ?: 0
}
/**
 * 日期转换
 */
fun String.toFormattedDate(inputFormat: String, outputFormat: String): String {
    try {
        val inputDateFormat = SimpleDateFormat(inputFormat)
        val outputDateFormat = SimpleDateFormat(outputFormat)

        val date = inputDateFormat.parse(this)
        return outputDateFormat.format(date)
    } catch (e: Exception) {
        println("日期格式转换出现异常: ${e.message}")
        return this // 返回原始日期字符串
    }
}

fun String?.safeToDouble(): Double {
    return this?.toDoubleOrNull() ?: 0.0
}

/**
 * 判断手机号是否合法,这里还是不要完全按照正规正则写死，因为手机号更新速度较快，写死了到时候改动也比较频繁
 */
fun String?.isLegalPhoneNumber(): Boolean {
    val telecomOperatorPattern = "^(1[3-9])\\d{9}$".toRegex()
    return telecomOperatorPattern.matches(this ?: "")
}