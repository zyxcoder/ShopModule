package com.zkxy.shop.common.dialog

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import com.gxy.common.common.dialog.CenterDialog
import com.gxy.common.ext.copyText
import com.zkxy.shop.databinding.DialogCashQrcodeBinding
import com.zkxy.shop.utils.CodeUtil
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast

class CashQrCodeDialog(context: Context, val wxPayCodeUrl: String, val wxOrderAmount: Double?) :
    CenterDialog(context) {

    var onSaveImgClickListener: ((qrCode: Bitmap) -> Unit)? = null

    private lateinit var viewBind: DialogCashQrcodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBind = DialogCashQrcodeBinding.inflate(layoutInflater)
        setContentView(viewBind.root)
        val qrCode = CodeUtil.createQRCode(wxPayCodeUrl, 165)
        viewBind.apply {
            ivClose.onContinuousClick { dismiss() }
            ivQrcode.setImageBitmap(qrCode)
            tvAmount.text = "￥$wxOrderAmount"
            tvSaveImg.onContinuousClick {
                context.copyText(wxPayCodeUrl)
                context.showToast("复制成功，去微信支付")
            }
            tvPayUrl.text = wxPayCodeUrl
        }
    }
}