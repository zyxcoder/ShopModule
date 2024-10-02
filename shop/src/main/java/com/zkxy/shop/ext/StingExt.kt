package com.zkxy.shop.ext

/**
 * @author zhangyuxiang
 * @date 2024/9/28
 */
/**
 * 判断字符串是否为整数
 */
fun String.isInt(): Boolean {
    return this.toIntOrNull() != null
}

/**
 * 判断字符串是否为浮点数
 */
fun String.isDouble(): Boolean {
    return this.toDoubleOrNull() != null
}

/**
 * 判断字符串是否为长整型
 */
fun String.isLong(): Boolean {
    return this.toLongOrNull() != null
}

/**
 * 判断字符串是否为布尔类型
 */
fun String.isBoolean(): Boolean {
    return this.toBooleanStrictOrNull() != null
}
