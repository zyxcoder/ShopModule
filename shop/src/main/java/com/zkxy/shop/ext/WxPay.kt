package com.zkxy.shop.ext

import android.content.Context
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.zyxcoder.mvvmroot.ext.showToast

fun IWXAPI?.pay(
    context: Context?,
    appId: String?,
    partnerId: String?,
    prepayId: String?,
    nonceStr: String?,
    timeStamp: String?,
    sign: String?
) {
    this?.apply {
        if (isWXAppInstalled) {
            val request = PayReq()
            request.appId = appId
            request.partnerId = partnerId
            request.prepayId = prepayId
            request.packageValue = "Sign=WXPay"
            request.nonceStr = nonceStr
            request.timeStamp = timeStamp
            request.sign = sign
            //拉起微信支付
            sendReq(request)
        } else {
            context?.showToast("您的设备未安装微信客户端")
        }
    }
}