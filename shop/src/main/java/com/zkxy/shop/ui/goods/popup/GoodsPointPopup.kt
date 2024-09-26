package com.zkxy.shop.ui.goods.popup

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import com.zkxy.shop.R
import com.zkxy.shop.databinding.PopGoodsPointBinding
import com.zkxy.shop.entity.category.GoodsPointEntity
import com.zkxy.shop.ui.goods.adapter.GoodsPointPopAdapter
import razerdp.basepopup.BasePopupWindow

/**
 * @author zhangyuxiang
 * @date 2024/9/26
 */
class GoodsPointPopup(context: Context, private val goodsPointEntitys: List<GoodsPointEntity>?) :
    BasePopupWindow(context) {

    var onPointSelectListener: ((item: GoodsPointEntity) -> Unit)? = null
    private lateinit var adapter: GoodsPointPopAdapter

    init {
        setAlignBackground(true)
        setContentView(R.layout.pop_goods_point)
        showAnimation =
            AnimationUtils.loadAnimation(context, com.gxy.common.R.anim.anim_scale_top_in)
        dismissAnimation =
            AnimationUtils.loadAnimation(context, com.gxy.common.R.anim.anim_scale_top_out)
    }

    override fun onViewCreated(contentView: View) {
        PopGoodsPointBinding.bind(contentView).apply {
            adapter = GoodsPointPopAdapter().apply {
                rvPoint.adapter = this
                setNewInstance(goodsPointEntitys?.toMutableList())
                onGoodsPointSelectListener = {
                    onPointSelectListener?.invoke(it)
                    dismiss()
                }
            }
        }
    }
}