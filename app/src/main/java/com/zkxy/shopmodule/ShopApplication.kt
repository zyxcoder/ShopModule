package com.zkxy.shopmodule

import android.app.Application
import com.zkxy.shop.shopInit

/**
 * @author zhangyuxiang
 * @date 2024/9/20
 */
class ShopApplication : Application() {


    override fun onCreate() {
        super.onCreate()
//        shopInit(application = this, shopHttpUrl = "http://192.168.1.54:7777/", formId = 2)
        shopInit(application = this, shopHttpUrl = "http://39.103.61.134:9091/api/", formId = 2)
    }
}