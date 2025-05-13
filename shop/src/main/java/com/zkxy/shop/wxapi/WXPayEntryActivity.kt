package com.zkxy.shop.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.zkxy.shop.ui.goods.PlaceOrderActivity.Companion.CLOSE_PLACE_ORDER_PAGE
import com.zkxy.shop.wxApi
import com.zyxcoder.mvvmroot.common.bus.Bus

class WXPayEntryActivity : Activity(), IWXAPIEventHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 隐藏状态栏
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        wxApi?.handleIntent(intent, this)
    }

    override fun onReq(p0: BaseReq?) {
    }

    override fun onResp(p0: BaseResp?) {
        Bus.post(CLOSE_PLACE_ORDER_PAGE, "")
        finish()
    }
}