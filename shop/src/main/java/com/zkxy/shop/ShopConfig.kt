package com.zkxy.shop

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.gxy.common.common.loadsir.DefaultLoadingCallback
import com.gxy.common.common.loadsir.EmptyCallback
import com.gxy.common.common.loadsir.ErrorCallback
import com.kingja.loadsir.core.LoadSir

/**
 * @author zhangyuxiang
 * @date 2024/9/2
 */
var isInit = false


/**
 * shop插件服务器地址
 */
var modeBaseUrl: String? = ""

/**
 * 使用此插件请调用初始化方法，否则会报异常,在Application中调用
 */
fun shopInit(application: Application, shopHttpUrl: String?) {
    Fresco.initialize(application)
    LoadSir.beginBuilder().addCallback(DefaultLoadingCallback()).addCallback(EmptyCallback())
        .addCallback(ErrorCallback()).commit()
    modeBaseUrl = shopHttpUrl

    isInit = true
}