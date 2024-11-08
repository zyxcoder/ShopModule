package com.zkxy.shop.common.dialog

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import com.gxy.common.common.dialog.CenterDialog
import com.zkxy.shop.databinding.DialogCashQrcodeBinding
import com.zkxy.shop.utils.CodeUtil
import com.zyxcoder.mvvmroot.ext.onContinuousClick

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
            tvSaveImg.onContinuousClick { onSaveImgClickListener?.invoke(qrCode) }
        }
    }
}