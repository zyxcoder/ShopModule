package com.zyxcoder.mvvmroot.ext

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

/**
 * Create by zyx_coder on 2023/4/23
 */

/**
 * 封装Bundle的put方法(封装了常用的，如果缺少要用的，可参考自行封装)
 */
@Suppress("UNCHECKED_CAST")
fun <T> Bundle.put(key: String, value: T) {
    when (value) {
        is String -> putString(key, value)
        is Boolean -> putBoolean(key, value)
        is Long -> putLong(key, value)
        is Char -> putChar(key, value)
        is Int -> putInt(key, value)
        is Short -> putShort(key, value)
        is Byte -> putByte(key, value)
        is ByteArray -> putByteArray(key, value)
        is CharArray -> putCharArray(key, value)
        is CharSequence -> putCharSequence(key, value)
        is Float -> putFloat(key, value)
        is Double -> putDouble(key, value)
        is IntArray -> putIntArray(key, value)
        is DoubleArray -> putDoubleArray(key, value)
        is FloatArray -> putFloatArray(key, value)
        is ShortArray -> putShortArray(key, value)
        is LongArray -> putLongArray(key, value)
        is BooleanArray -> putBooleanArray(key, value)
        is Bundle -> putBundle(key, value)
        is Array<*> -> {
            when {
                value.isArrayOf<String>() -> putStringArray(key, value as Array<String>)
                value.isArrayOf<Parcelable>() -> putParcelableArray(key, value as Array<Parcelable>)
                value.isArrayOf<CharSequence>() -> putCharSequenceArray(
                    key,
                    value as Array<CharSequence>
                )
            }
        }
        is Parcelable -> putParcelable(key, value)
        is Serializable -> putSerializable(key, value)
        else -> throw IllegalStateException("Type of property $key is not supported")
    }
}