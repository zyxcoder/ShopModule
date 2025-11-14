package com.zk.common.common.loadsir

import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir


/**
 * @author zhangyuxiang
 * @date 2024/1/26
 */

fun getLoadSir(): LoadSir {
    return LoadSir.getDefault()
}

fun LoadService<Any>.setLoadContentStatus(loadZkContentStatus: LoadZkContentStatus) {
    when (loadZkContentStatus) {
        LoadZkContentStatus.DEFAULT_LOADING -> {
            showCallback(DefaultZkLoadingCallback::class.java)
        }

        LoadZkContentStatus.SUCCESS -> {
            showSuccess()
        }

        LoadZkContentStatus.DEFAULT_ERROR -> {
            showCallback(ErrorZkCallback::class.java)
        }

        LoadZkContentStatus.DEFAULT_EMPTY -> {
            showCallback(EmptyZkCallback::class.java)
        }
    }
}