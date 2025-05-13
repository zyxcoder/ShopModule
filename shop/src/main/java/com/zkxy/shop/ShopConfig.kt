package com.zkxy.shop

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.gxy.common.common.loadsir.DefaultLoadingCallback
import com.gxy.common.common.loadsir.EmptyCallback
import com.gxy.common.common.loadsir.ErrorCallback
import com.kingja.loadsir.core.LoadSir
import com.tencent.mm.opensdk.openapi.IWXAPI

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
var appLoadLon: String? = ""
var appLoadLat: String? = ""

//用户手机号(必传)
var appUserTel: String? = ""

//用户登录名
var appUserName: String? = ""

var wxApi: IWXAPI? = null

/**
 * 使用此插件请调用初始化方法，否则会报异常,在Application中调用
 * @param application Application
 * @param shopHttpUrl shop服务器地址
 * @param formId 平台id
 */
fun shopInit(
    application: Application,
    shopHttpUrl: String?,
    formId: Int? = 2,
    api: IWXAPI? = null
) {
    Fresco.initialize(application)
    LoadSir.beginBuilder().addCallback(DefaultLoadingCallback()).addCallback(EmptyCallback())
        .addCallback(ErrorCallback()).commit()
    modeBaseUrl = shopHttpUrl
    appPlatformId = formId ?: 2
    isInit = true
    wxApi = api
}

/**
 * 登录后传递必要参数
 * @param userTel 平台用户电话
 * @param loadLon 经度
 * @param loadLat 纬度
 */
fun shopInitArgument(
    userTel: String? = null,
    loadLon: String? = null,
    loadLat: String? = null,
    userName: String? = null
) {
    appUserTel = userTel
    appLoadLon = loadLon
    appLoadLat = loadLat
    appUserName = userName
}