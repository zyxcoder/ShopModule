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
 * 平台id
 */
var appPlatformId = 2

//当前经纬度
var loadLon: String = ""
var loadLat: String = ""

/**
 * 使用此插件请调用初始化方法，否则会报异常,在Application中调用
 * @param application Application
 * @param shopHttpUrl shop服务器地址
 * @param formId 平台id
 */
fun shopInit(application: Application, shopHttpUrl: String?, formId: Int? = 2) {
    Fresco.initialize(application)
    LoadSir.beginBuilder().addCallback(DefaultLoadingCallback()).addCallback(EmptyCallback())
        .addCallback(ErrorCallback()).commit()
    modeBaseUrl = shopHttpUrl
    appPlatformId = formId ?: 2
    isInit = true
}