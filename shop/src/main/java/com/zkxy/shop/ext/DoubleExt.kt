package com.zkxy.shop.ext

import java.text.DecimalFormat

/**
 * @author zhangyuxiang
 * @date 2024/5/8
 */

/**
 * 把doule转为普通计数法
 */
fun Double?.normalFormat(): String {
    return DecimalFormat("#.###############").format(this ?: 0.0f)
}

/**
 *公里数转换
 */
fun Double?.formatRouteMiles(): String {
    return DecimalFormat("#.##").format(this ?: 0.0f)
}

/**
 * 两个double相减，保留两位小数
 */
fun Double.subtractAndRound(value: Double): Double {
    val result = this - value
    return "%.2f".format(result).toDouble()
}
/**
 * 两个double相加，保留两位小数
 */
fun Double.addAndRound(value: Double): Double {
    val result = this + value
    return "%.2f".format(result).toDouble()
}

/**
 * double相乘，精确计算
 */
fun Double.multiply(b: Double): Double {
    return toBigDecimal().multiply(b.toBigDecimal()).toDouble()
}

/**
 * double相乘，精确计算,保留两位
 */
fun Double.multiply(b: Int): String {
    return toBigDecimal().multiply(b.toBigDecimal()).toDouble().doubleToTwoDecimalPlace().toString()
}

fun Double.multiplyFormat(b: Int): String {
    return toBigDecimal().multiply(b.toBigDecimal()).toDouble().doubleToTwoDecimalPlace().normalFormat()
}

/**
 * int，精确计算
 */
fun Int.multiply(b: Int): String {
    return toBigDecimal().multiply(b.toBigDecimal()).toString()
}


/**
 * 两个double相除，保留两位小数
 */
fun Double.divideWithPrecision(divisor: Double): Double {
    if (divisor == 0.0) {
        return 0.0
    }
    val result = this / divisor
    return "%.2f".format(result).toDouble()
}

/**
 * 两个double相除，保留一位小数
 */
fun Double?.divideAndRoundToOneDecimalPlace(divisor: Double?): Double {
    if (divisor == 0.0 || this == 0.0) {
        return 0.0
    }
    val result = (this ?: 0.0) / (divisor ?: 0.0)
    return "%.1f".format(result).toDouble()
}

/**
 * 两个double相除
 */
fun Double.divideWithNoPrecision(divisor: Double): Double {
    if (divisor == 0.0) {
        return 0.0
    }
    val result = this / divisor
    return result
}

/**
 * 保留一位小数
 */
fun Double?.doubleToOneDecimalPlace(): Double {
    return "%.1f".format(this).toDouble()
}
/**
 * 保留两位小数
 */
fun Double?.doubleToTwoDecimalPlace(): Double {
    return "%.2f".format(this).toDouble()
}

/**
 * 保留两位小数
 */
fun Double?.doubleToTwoDecimalPlaceString(): String {
    return "%.2f".format(this)
}

fun Double?.formatAmount(): String {
    return this?.let {
        "%,.2f".format(it)
    } ?: "" // 处理无效的字符串
}