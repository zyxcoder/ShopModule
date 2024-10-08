package com.zkxy.shopmodule

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.zkxy.shop.loadLat
import com.zkxy.shop.loadLon
import com.zkxy.shop.ui.goods.GoodsDetailsActivity
import com.zkxy.shop.ui.home.ShopHomeActivity
import com.zkxy.shop.ui.order.OrderListActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btJump).setOnClickListener {
            ShopHomeActivity.startActivity(this)
        }
        findViewById<Button>(R.id.btGoodsDetail).setOnClickListener {
            GoodsDetailsActivity.startActivity(this)
        }
        findViewById<Button>(R.id.btOrderList).setOnClickListener {
            OrderListActivity.startActivity(this)
        }
//30.481401271903536, 103.55262001052857
        loadLon = "30.481401271903536"
        loadLat = "103.55262001052857"
    }
}