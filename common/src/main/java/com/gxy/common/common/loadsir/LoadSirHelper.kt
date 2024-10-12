package com.gxy.common.common.loadsir

import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir


/**
 * @author zhangyuxiang
 * @date 2024/1/26
 */

fun getLoadSir(): LoadSir {
    return LoadSir.getDefault()
}

fun LoadService<Any>.setLoadContentStatus(loadContentStatus: LoadContentStatus) {
    when (loadContentStatus) {
        LoadContentStatus.DEFAULT_LOADING -> {
            showCallback(DefaultLoadingCallback::class.java)
        }

        LoadContentStatus.SUCCESS -> {
            showSuccess()
        }

        LoadContentStatus.DEFAULT_ERROR -> {
            showCallback(ErrorCallback::class.java)
        }

        LoadContentStatus.DEFAULT_EMPTY -> {
            showCallback(EmptyCallback::class.java)
        }
    }
}