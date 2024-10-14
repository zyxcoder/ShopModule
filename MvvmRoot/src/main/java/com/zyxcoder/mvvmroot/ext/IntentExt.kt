package com.zyxcoder.mvvmroot.ext

import android.annotation.SuppressLint
import android.content.Intent
import android.os.BaseBundle
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Create by zyx_coder on 2023/4/23
 */
/**
 * 封装Intent的put方法(封装了常用的，如果缺少要用的，可参考自行封装)
 */
@Suppress("UNCHECKED_CAST")
fun <T> Intent.put(key: String, value: T) {
    when (value) {
        is Int -> putExtra(key, value)
        is Byte -> putExtra(key, value)
        is Char -> putExtra(key, value)
        is Long -> putExtra(key, value)
        is Float -> putExtra(key, value)
        is Short -> putExtra(key, value)
        is Double -> putExtra(key, value)
        is Boolean -> putExtra(key, value)
        is Bundle -> putExtra(key, value)
        is String -> putExtra(key, value)
        is IntArray -> putExtra(key, value)
        is ByteArray -> putExtra(key, value)
        is CharArray -> putExtra(key, value)
        is LongArray -> putExtra(key, value)
        is FloatArray -> putExtra(key, value)
        is ShortArray -> putExtra(key, value)
        is DoubleArray -> putExtra(key, value)
        is BooleanArray -> putExtra(key, value)
        is CharSequence -> putExtra(key, value)
        is Array<*> -> {
            when {
                value.isArrayOf<String>() -> putExtra(key, value as Array<String>)
                value.isArrayOf<Parcelable>() -> putExtra(key, value as Array<Parcelable>)
                value.isArrayOf<CharSequence>() -> putExtra(key, value as Array<CharSequence>)
                else -> putExtra(key, value)
            }
        }
        is Parcelable -> putExtra(key, value)
        is Serializable -> putExtra(key, value)
        else -> throw IllegalStateException("Type of property $key is not supported")
    }
}

fun Intent.putExtras(params: Map<String, Any?>? = null): Intent {
    if (params.isNullOrEmpty()) return this
    params.forEach { (key, value) ->
        if (value != null) {
            put(key, value)
        }
    }
    return this
}

@SuppressLint("DiscouragedPrivateApi")
internal object IntentFieldMethod {
    lateinit var mExtras: Field
    lateinit var mMap: Field
    lateinit var unparcel: Method

    init {
        try {
            mExtras = Intent::class.java.getDeclaredField("mExtras")
            mMap = BaseBundle::class.java.getDeclaredField("mMap")
            unparcel = BaseBundle::class.java.getDeclaredMethod("unparcel")
            mExtras.isAccessible = true
            mMap.isAccessible = true
            unparcel.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

/**
 * [Intent]的扩展方法，此方法可无视类型直接获取到对应值
 * 如getStringExtra()、getIntExtra()、getSerializableExtra()等方法通通不用
 * 可以直接通过此方法来获取到对应的值，例如：
 * <pre>
 *     var mData: List<String>? = null
 *     mData = intent.get("Data")
 * </pre>
 * 而不用显式强制转型
 *
 * @param key 对应的Key
 * @return 对应的Value
 */
@Suppress("UNCHECKED_CAST")
fun <T> Intent.get(key: String, defaultValue: T? = null): T? {
    try {
        val extras = IntentFieldMethod.mExtras.get(this) as? Bundle ?: return defaultValue
        IntentFieldMethod.unparcel.invoke(extras)
        val map = IntentFieldMethod.mMap.get(extras) as? Map<String, Any> ?: return defaultValue
        return map[key] as? T ?: return defaultValue
    } catch (e: Exception) {
        //Ignore
    }
    return defaultValue
}
