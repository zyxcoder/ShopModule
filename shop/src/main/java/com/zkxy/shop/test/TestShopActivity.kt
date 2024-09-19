package com.zkxy.shop.test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gxy.common.base.BaseViewBindActivity
import com.zkxy.shop.test.adapter.TestListAdapter
import com.zkxy.shop.databinding.ActivityShopTestBinding
import com.zkxy.shop.entity.TestItemData
import com.zkxy.shop.mode_base_url
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast
import com.zyxcoder.mvvmroot.utils.loadImage

class TestShopActivity : BaseViewBindActivity<TestShopActivityViewModel, ActivityShopTestBinding>() {

    private lateinit var adapter: TestListAdapter

    companion object {
        fun startActivity(context: Context) {
            if (mode_base_url.isNullOrEmpty()) {
                context.showToast("请先初始化url")
                return
            }
            context.startActivity(Intent(context, TestShopActivity::class.java))
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        mViewBind.apply {
            tvUrl.text = "模块地址:" + mode_base_url
            btJump.onContinuousClick {
                TestShop2Activity.startActivity(context = this@TestShopActivity)
            }
            adapter = TestListAdapter().apply {
                rvInfo.adapter = this
            }
            ivLogo.loadImage("https://gntbiz.guangxingyun.com/ImageData/id_card/20220630/1_1656556786872.jpg")
        }
    }

    override fun createObserver() {

    }

    override fun dismissLoading() {
    }

    override fun initData() {
        val list = mutableListOf<TestItemData>()
        repeat(20) {
            list.add(TestItemData("scsqwdqdqwdwqdqwdqa" + it))
        }
        adapter.setNewInstance(list)
    }

    override fun showLoading(message: String) {

    }

}