package com.zkxy.shop.ui.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gxy.common.base.BaseViewBindActivity
import com.zkxy.shop.databinding.ActivityAfterSaleDetailBinding

/**
 * @author zhangyuxiang
 * @date 2025/6/25
 */
class AfterSaleDetailActivity :
    BaseViewBindActivity<AfterSaleDetailViewModel, ActivityAfterSaleDetailBinding>() {

    companion object {
        fun startActivity(context: Context?) {
            context?.startActivity(Intent(context, AfterSaleDetailActivity::class.java))
        }
    }

    override fun init(savedInstanceState: Bundle?) {

    }
}