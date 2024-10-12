package com.gxy.common.view.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

/**
 * Created by zyx on 2019/7/11 0011.
 * 为RecyclerView设置上下左右间距
 */

public class GridSpacesItemDecoration
        extends RecyclerView.ItemDecoration {
    private HashMap<String, Integer> mSpaceValueMap;

    public static final String TOP_DECORATION = "top_decoration";
    public static final String BOTTOM_DECORATION = "bottom_decoration";
    public static final String LEFT_DECORATION = "left_decoration";
    public static final String RIGHT_DECORATION = "right_decoration";
    private int spanCount = 1;

    public GridSpacesItemDecoration(HashMap<String, Integer> mSpaceValueMap, int spanCount) {
        this.mSpaceValueMap = mSpaceValueMap;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mSpaceValueMap.get(TOP_DECORATION) != null) {
            if (parent.getChildAdapterPosition(view) >= spanCount) {
                outRect.top = mSpaceValueMap.get(TOP_DECORATION);
            } else {
                outRect.top = 0;
            }
        }
        if (mSpaceValueMap.get(LEFT_DECORATION) != null) {
            if (parent.getChildAdapterPosition(view) % spanCount == 0) {
                outRect.left = 0;
            } else {
                outRect.left = mSpaceValueMap.get(LEFT_DECORATION);
            }
        }
        if (mSpaceValueMap.get(RIGHT_DECORATION) != null) {
            outRect.right = mSpaceValueMap.get(RIGHT_DECORATION);
        }
        if (mSpaceValueMap.get(BOTTOM_DECORATION) != null) {
            outRect.bottom = mSpaceValueMap.get(BOTTOM_DECORATION);
        }
    }
}
