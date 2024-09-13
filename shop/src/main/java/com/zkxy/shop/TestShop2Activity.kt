package com.zkxy.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gxy.common.base.BaseViewBindActivity
import com.zkxy.shop.databinding.ActivityShopTest2Binding
import com.zyxcoder.mvvmroot.ext.onContinuousClick

class TestShop2Activity : BaseViewBindActivity<TestShopActivityViewModel, ActivityShopTest2Binding>() {


    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, TestShop2Activity::class.java))
        }
    }

    override fun init(savedInstanceState: Bundle?) {
    }

    override fun createObserver() {
    }

    override fun dismissLoading() {
    }

    override fun initData() {
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.apply {
            tvUrl.text = "模块地址:" + mode_base_url
            btJump.onContinuousClick {
                finish()
            }
        }
    }

    override fun showLoading(message: String) {
    }

}