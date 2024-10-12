package com.gxy.common.utils;

import android.graphics.Color;

/**
 * @author Sxw
 * @date 2020/7/2.
 * description：${xxxxx}
 */

public class AppBarLayoutUtil {

    public static int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }
    // 颜色混合方法
    public static int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        float r = Color.red(from) * inverseRatio + Color.red(to) * ratio;
        float g = Color.green(from) * inverseRatio + Color.green(to) * ratio;
        float b = Color.blue(from) * inverseRatio + Color.blue(to) * ratio;

        return Color.rgb((int) r, (int) g, (int) b);
    }
}
