package com.zkxy.shopmodule

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.zkxy.shop.test.TestShopActivity
import com.zkxy.shop.mode_base_url
import com.zkxy.shop.ui.home.ShopHomeActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mode_base_url = "www.baidu.com"
        findViewById<Button>(R.id.btJump).setOnClickListener {
//            TestShopActivity.startActivity(context = this)
            ShopHomeActivity.startActivity(this)
        }
    }
}