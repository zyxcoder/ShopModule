package com.zkxy.shopmodule

import android.app.Application
import com.zkxy.shop.shopInit

/**
 * @author zhangyuxiang
 * @date 2024/9/20
 */
class ShopApplication :Application() {


    override fun onCreate() {
        super.onCreate()
        shopInit(this,"http://192.168.1.34:7777/")
    }
}