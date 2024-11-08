package com.zkxy.shop.utils;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

//依赖 com.google.zxing:core:3.5.3
public final class CodeUtil {

    public static final int DEFAULT_REQ_WIDTH = 480;
    public static final int DEFAULT_REQ_HEIGHT = 640;

    private CodeUtil() {
        throw new AssertionError();
    }

    /**
     * 生成二维码
     *
     * @param content 二维码的内容
     * @param size    二维码的大小
     * @return
     */
    public static Bitmap createQRCode(@NonNull String content, int size) {
        return createQRCode(content, size, null);
    }

    /**
     * 生成二维码
     *
     * @param content   二维码的内容
     * @param size      二维码的大小
     * @param codeColor 二维码的颜色
     * @return
     */
    public static Bitmap createQRCode(@NonNull String content, int size, @ColorInt int codeColor) {
        return createQRCode(content, size, null, codeColor);
    }

    /**
     * 生成我二维码
     *
     * @param content 二维码的内容
     * @param size    二维码的大小
     * @param logo    Logo大小默认占二维码的20%
     * @return
     */
    public static Bitmap createQRCode(@NonNull String content, int size, @Nullable Bitmap logo) {
        return createQRCode(content, size, logo, Color.BLACK);
    }

    /**
     * 生成我二维码
     *
     * @param content   二维码的内容
     * @param size      二维码的大小
     * @param logo      Logo大小默认占二维码的20%
     * @param codeColor 二维码的颜色
     * @return
     */
    public static Bitmap createQRCode(@NonNull String content, int size, @Nullable Bitmap logo, @ColorInt int codeColor) {
        return createQRCode(content, size, logo, 0.2f, codeColor);
    }

    /**
     * 生成二维码
     *
     * @param content 二维码的内容
     * @param size    二维码的大小
     * @param logo    二维码中间的Logo
     * @param ratio   Logo所占比例 因为二维码的最大容错率为30%，所以建议ratio的范围小于0.3
     * @return
     */
    public static Bitmap createQRCode(@NonNull String content, int size, @Nullable Bitmap logo, @FloatRange(from = 0.0f, to = 1.0f) float ratio) {
        //配置参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //容错级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置空白边距的宽度
        hints.put(EncodeHintType.MARGIN, 1); //default is 4
        return createQRCode(content, size, logo, ratio, hints);
    }

    /**
     * 生成二维码
     *
     * @param content   二维码的内容
     * @param size      二维码的大小
     * @param logo      二维码中间的Logo
     * @param ratio     Logo所占比例 因为二维码的最大容错率为30%，所以建议ratio的范围小于0.3
     * @param codeColor 二维码的颜色
     * @return
     */
    public static Bitmap createQRCode(@NonNull String content, int size, @Nullable Bitmap logo, @FloatRange(from = 0.0f, to = 1.0f) float ratio, @ColorInt int codeColor) {
        //配置参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //容错级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置空白边距的宽度
        hints.put(EncodeHintType.MARGIN, 1); //default is 4
        return createQRCode(content, size, logo, ratio, hints, codeColor);
    }

    public static Bitmap createQRCode(@NonNull String content, int size, @Nullable Bitmap logo, @FloatRange(from = 0.0f, to = 1.0f) float ratio, @Nullable Map<EncodeHintType, ?> hints) {
        return createQRCode(content, size, logo, ratio, hints, Color.BLACK);
    }

    /**
     * 生成二维码
     *
     * @param content   二维码的内容
     * @param size      二维码的大小
     * @param logo      二维码中间的Logo
     * @param ratio     Logo所占比例 因为二维码的最大容错率为30%，所以建议ratio的范围小于0.3
     * @param hints
     * @param codeColor 二维码的颜色
     * @return
     */
    public static Bitmap createQRCode(@NonNull String content, int size, @Nullable Bitmap logo, @FloatRange(from = 0.0f, to = 1.0f) float ratio, @Nullable Map<EncodeHintType, ?> hints, @ColorInt int codeColor) {
        try {

            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
            int[] pixels = new int[size * size];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * size + x] = codeColor;
                    } else {
                        pixels[y * size + x] = Color.WHITE;
                    }
                }
            }

            // 生成二维码图片的格式
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);

            if (logo != null) {
                bitmap = addLogo(bitmap, logo, ratio);
            }

            return bitmap;
        } catch (Exception e) {
        }

        return null;
    }

    /**
     * 在二维码中间添加Logo图案
     *
     * @param src   原图
     * @param logo  中间的Logo
     * @param ratio Logo所占比例 因为二维码的最大容错率为30%，所以建议ratio的范围小于0.3
     * @return
     */
    private static Bitmap addLogo(@Nullable Bitmap src, @Nullable Bitmap logo, @FloatRange(from = 0.0f, to = 1.0f) float ratio) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小
        float scaleFactor = srcWidth * ratio / logoWidth;
        Bitmap bitmap;
        try {
            bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2f, srcHeight / 2f);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2f, (srcHeight - logoHeight) / 2f, null);
            canvas.save();
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
        }
        return bitmap;
    }

}