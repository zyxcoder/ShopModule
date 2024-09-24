package com.zkxy.shop.ui.goods

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.gxy.common.base.BaseViewBindActivity
import com.zkxy.shop.R
import com.zkxy.shop.databinding.ActivityPlaceOrderBinding
import com.zkxy.shop.ui.goods.adapter.ZtPointAdapter
import com.zyxcoder.mvvmroot.utils.ImageOptions
import com.zyxcoder.mvvmroot.utils.dpToPx
import com.zyxcoder.mvvmroot.utils.loadImage

class PlaceOrderActivity : BaseViewBindActivity<PlaceOrderViewModel, ActivityPlaceOrderBinding>() {

    private val ztPointAdapter by lazy { ZtPointAdapter() }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, PlaceOrderActivity::class.java))
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        mViewBind.apply {
            ivGoods.loadImage(
                "https://gd-hbimg.huaban.com/4bd2502a1859e4bcc9d0afeda5b8851d98a267dd18c54-81OUAo_fw1200webp",
                imageOptions = ImageOptions().apply { cornersRadius = dpToPx(4f).toInt() }
            )
            vsZt.inflate().findViewById<RecyclerView>(R.id.rlvZt).adapter = ztPointAdapter
        }

        ztPointAdapter.setList(mutableListOf("", "", "", "", "", ""))
    }
}