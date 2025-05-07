package com.zkxy.shop.ui.goods.popup

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import com.zkxy.shop.R
import com.zkxy.shop.databinding.PopSortRuleBinding
import com.zkxy.shop.entity.goods.GoodsPointEntity
import com.zkxy.shop.ui.goods.adapter.GoodsPointPopAdapter
import razerdp.basepopup.BasePopupWindow

/**
 * @author zhangyuxiang
 * @date 2024/9/26
 */
class SortRulePopup(context: Context, private val sortRuleEntitys: List<GoodsPointEntity>?) :
    BasePopupWindow(context) {

    var onPointSelectListener: ((item: GoodsPointEntity) -> Unit)? = null
    private lateinit var adapter: GoodsPointPopAdapter

    init {
        setAlignBackground(true)
        setContentView(R.layout.pop_sort_rule)
        showAnimation =
            AnimationUtils.loadAnimation(context, com.gxy.common.R.anim.anim_scale_top_in)
        dismissAnimation =
            AnimationUtils.loadAnimation(context, com.gxy.common.R.anim.anim_scale_top_out)
    }

    override fun onViewCreated(contentView: View) {
        PopSortRuleBinding.bind(contentView).apply {
            adapter = GoodsPointPopAdapter().apply {
                rvPoint.adapter = this
                setNewInstance(sortRuleEntitys?.toMutableList())
                onGoodsPointSelectListener = {
                    onPointSelectListener?.invoke(it)
                    dismiss()
                }
            }
        }
    }
}