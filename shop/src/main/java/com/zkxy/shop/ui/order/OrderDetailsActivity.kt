package com.zkxy.shop.ui.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gxy.common.base.BaseViewBindActivity
import com.zkxy.shop.databinding.ActivityOrderDetailsBinding

class OrderDetailsActivity :
    BaseViewBindActivity<OrderDetailsViewModel, ActivityOrderDetailsBinding>() {

    companion object {
        fun start(context: Context?) {
            context?.startActivity(Intent(context, OrderDetailsActivity::class.java))
        }
    }

    override fun init(savedInstanceState: Bundle?) {
    }
}