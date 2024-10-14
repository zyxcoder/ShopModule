package com.zyxcoder.mvvmroot.callback.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.zyxcoder.mvvmroot.callback.livedata.data.BooleanLiveData

/**
 * Create by zyx_coder on 2022/11/17
 */
object AppLifeObserver : DefaultLifecycleObserver {
    var isForeground = BooleanLiveData()

    //app在前台
    override fun onStart(owner: LifecycleOwner) {
        isForeground.value = true
    }

    //app在后台
    override fun onStop(owner: LifecycleOwner) {
        isForeground.value = false
    }
}