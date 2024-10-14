package com.zyxcoder.mvvmroot.network.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.zyxcoder.mvvmroot.network.NetworkUtil

/**
 * Create by zyx_coder on 2022/11/17
 * 网络变化广播接收器
 */
class NetworkStateReceive : BroadcastReceiver() {
    var isInit = true

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION && !isInit) {
            when {
                !NetworkUtil.isNetworkAvailable(context) -> {
                    //收到没有网络时判断之前的值是不是有网络，如果有网络才提示通知 ，防止重复通知
                    NetworkStateManager.instance.netWorkStateCallback.value?.let {
                        if (it.isSuccess) {
                            //没网
                            NetworkStateManager.instance.netWorkStateCallback.value =
                                NetState(isSuccess = false)
                        }
                        return
                    }
                    NetworkStateManager.instance.netWorkStateCallback.value =
                        NetState(isSuccess = false)
                }
                else -> {
                    //收到有网络时判断之前的值是不是没有网络，如果没有网络才提示通知 ，防止重复通知
                    NetworkStateManager.instance.netWorkStateCallback.value?.let {
                        if (!it.isSuccess) {
                            //有网络了
                            NetworkStateManager.instance.netWorkStateCallback.value =
                                NetState(isSuccess = true)
                        }
                        return
                    }
                    NetworkStateManager.instance.netWorkStateCallback.value =
                        NetState(isSuccess = true)
                }
            }
            isInit = false
        }
    }
}