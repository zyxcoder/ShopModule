package com.zk.common.application

import com.facebook.drawee.backends.pipeline.Fresco
import com.zk.common.common.loadsir.DefaultZkLoadingCallback
import com.zk.common.common.loadsir.EmptyZkCallback
import com.zk.common.common.loadsir.ErrorZkCallback
import com.kingja.loadsir.core.LoadSir


/**
 * @author zhangyuxiang
 * @date 2024/2/22
 */
open class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        LoadSir.beginBuilder()
            .addCallback(DefaultZkLoadingCallback())
            .addCallback(EmptyZkCallback())
            .addCallback(ErrorZkCallback())
            .commit()
    }
}