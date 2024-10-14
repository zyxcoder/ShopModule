package com.zyxcoder.mvvmroot.network.manager

import com.zyxcoder.mvvmroot.callback.livedata.event.EventLiveData

/**
 * Create by zyx_coder on 2022/11/17
 * 网络状态变化管理类
 */
class NetworkStateManager private constructor() {
    val netWorkStateCallback = EventLiveData<NetState>()

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }
}