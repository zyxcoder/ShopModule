package com.gxy.common.ext

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @author zhangyuxiang
 * @date 2024/3/12
 */

enum class DateFormatType {
    TYPE_YMD,//年月日格式
    TYPE_YMDHM,//年月日时分格式
    TYPE_YMDHMS//年月日时分秒格式
}

/**
 * 将日期转为对应格式
 */
fun Date?.dateToString(formatType: DateFormatType): String? {
    return this?.run {
        when (formatType) {
            DateFormatType.TYPE_YMD -> {
                SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(this)
            }

            DateFormatType.TYPE_YMDHMS -> {
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(this)
            }

            DateFormatType.TYPE_YMDHM -> {
                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH).format(this)
            }
        } ?: null
    }
}