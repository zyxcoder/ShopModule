package com.zyxcoder.mvvmroot.network

import com.zyxcoder.mvvmroot.callback.lifecycle.ActivityManger
import com.zyxcoder.mvvmroot.ext.showToast
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Create by zyx_coder on 2022/11/18
 */
object ExceptionHandle {
    fun handleException(e: Exception) {
        if (ActivityManger.currentActivity == null) {
            return
        }
        when (e) {
            is ApiException -> {
                ActivityManger.currentActivity.showToast(e.message)
            }
            is ConnectException, is UnknownHostException -> {
                ActivityManger.currentActivity.showToast("网络走丢了,请查看手机网络状态")
            }
            is SocketTimeoutException -> {
                ActivityManger.currentActivity.showToast("网络拥堵,稍后请重试")
            }
            else -> {
                ActivityManger.currentActivity.showToast("未知错误")
            }
        }
    }
}