package com.zkxy.shop.ui.home.decoration

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zyxcoder.mvvmroot.utils.dpToPx

/**
 * @author zhangyuxiang
 * @date 2024/9/19
 */
class CategoryAverageMarginDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
        view.updateLayoutParams<MarginLayoutParams> {
            leftMargin = dpToPx(if (spanIndex % 3 == 0) 0f else 8f).toInt()
        }
    }
}