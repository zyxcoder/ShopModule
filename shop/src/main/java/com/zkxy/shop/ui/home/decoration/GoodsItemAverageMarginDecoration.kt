package com.zkxy.shop.ui.home.decoration

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zyxcoder.mvvmroot.utils.dpToPx

/**
 * @author zhangyuxiang
 * @date 2024/9/19
 */
class GoodsItemAverageMarginDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
        view.updateLayoutParams<MarginLayoutParams> {
            leftMargin = dpToPx(if (spanIndex % 2 == 0) 12f else 3.5f).toInt()
            rightMargin = dpToPx(if (spanIndex % 2 == 0) 3.5f else 12f).toInt()
        }
    }
}