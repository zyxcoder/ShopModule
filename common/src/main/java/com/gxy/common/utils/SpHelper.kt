package com.gxy.common.utils

import android.content.Context
import com.zyxcoder.mvvmroot.base.appContext

/**
 * Create by zyx_coder on 2023/7/27
 */
private const val ZKXH_SP = "zkxh_sp"
private const val SLEC_SP = "slec_sp"
//测试模式配置的api环境地址
const val TEST_MODEL_API_URL = "test_model_api_url"

//用户token
const val USER_TOKEN = "user_token"

fun <T> getSpValue(
    key: String, defaultVal: T
): T {
    val sp = appContext.getSharedPreferences(ZKXH_SP, Context.MODE_PRIVATE)
    return when (defaultVal) {
        is Boolean -> sp.getBoolean(key, defaultVal) as T
        is String -> sp.getString(key, defaultVal) as T
        is Int -> sp.getInt(key, defaultVal) as T
        is Long -> sp.getLong(key, defaultVal) as T
        is Float -> sp.getFloat(key, defaultVal) as T
        is Set<*> -> sp.getStringSet(key, defaultVal as Set<String>) as T
        else -> throw IllegalArgumentException("Unrecognized default value $defaultVal")
    }
}

fun <T> putSpValue(
    key: String, value: T
) {
    val editor = appContext.getSharedPreferences(ZKXH_SP, Context.MODE_PRIVATE).edit()
    when (value) {
        is Boolean -> editor.putBoolean(key, value)
        is String -> editor.putString(key, value)
        is Int -> editor.putInt(key, value)
        is Long -> editor.putLong(key, value)
        is Float -> editor.putFloat(key, value)
        is Set<*> -> editor.putStringSet(key, value as Set<String>)
        else -> throw UnsupportedOperationException("Unrecognized value $value")
    }
    editor.apply()
}

fun removeSpValue(key: String) {
    appContext.getSharedPreferences(ZKXH_SP, Context.MODE_PRIVATE).edit().remove(key).apply()
}

fun clearSpValue() {
    val editor = appContext.getSharedPreferences(ZKXH_SP, Context.MODE_PRIVATE).edit()
    editor.clear()
    editor.apply()
}

/**
 * 清空登录相关sp信息
 */
fun clearLoginSpValue() {
    val sp = appContext.getSharedPreferences(SLEC_SP, Context.MODE_PRIVATE)
    val editor = sp.edit()
    sp.all.filterNot {
        it.key == TEST_MODEL_API_URL
    }.forEach {
        editor.remove(it.key)
    }
    editor.apply()
}