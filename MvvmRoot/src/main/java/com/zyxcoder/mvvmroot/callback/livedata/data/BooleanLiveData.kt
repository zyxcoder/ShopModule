package com.zyxcoder.mvvmroot.callback.livedata.data

import androidx.lifecycle.MutableLiveData

/**
 * Create by zyx_coder on 2022/11/17
 */
class BooleanLiveData : MutableLiveData<Boolean>() {

    override fun getValue(): Boolean {
        return super.getValue() ?: false
    }
}
