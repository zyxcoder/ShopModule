package com.zkxy.shop.utils

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.annotation.ColorInt
import com.zkxy.shop.ext.normalFormat

/**
 * @author zhangyuxiang
 * @date 2024/9/19
 */
fun formatProductInfo(
    price: Double?,
    points: Double?,
    mainTextSize: Int = 16,
    minorTextSize: Int = 12,
    isNeedSpace: Boolean = true,
    @ColorInt chineseTextColor: Int? = null
): SpannableStringBuilder {
    val spannableString = SpannableStringBuilder()


    val tempPrice = if (price != null && price > 0) {
        price
    } else {
        null
    }
    val tempPoints = if (points != null && points > 0) {
        points
    } else {
        null
    }


    // 处理积分部分
    tempPoints?.let {
        if (it > 0) {
            // 添加积分值
            val pointsText = it.normalFormat()
            spannableString.append(pointsText)
            spannableString.setSpan(
                AbsoluteSizeSpan(mainTextSize, true), // 积分数字大小为16sp
                0, pointsText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // 添加粗体样式
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD), 0, pointsText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (isNeedSpace) {
                // 添加空格表示2dp间隔
                spannableString.append("\u00A0") // 在积分和“积分”之间添加2个空格
            }

            val spaceLength = if (isNeedSpace) 1 else 0

            // 添加“积分”字样
            spannableString.append("积分")
            spannableString.setSpan(
                AbsoluteSizeSpan(minorTextSize, true), // “积分”文本大小为12sp
                pointsText.length + spaceLength, // 注意这个计算
                pointsText.length + 2 + spaceLength, // 更新长度
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            chineseTextColor?.let { color ->
                spannableString.setSpan(
                    ForegroundColorSpan(color), pointsText.length + spaceLength, // 注意这个计算
                    pointsText.length + 2 + spaceLength, // 更新长度
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    // 添加空格表示2dp间隔，如果两者都有值
    if (tempPoints != null && tempPoints > 0 && tempPrice != null) {
        if (isNeedSpace) {
            spannableString.append("\u00A0") // 在积分部分和价格之间添加2个空格
        }
    }

    // 处理价格部分
    tempPrice?.let {
        if (it > 0) {
            // 添加“+”
            if (tempPoints != null && tempPoints > 0) {
                spannableString.append("+")
                spannableString.setSpan(
                    AbsoluteSizeSpan(14, true), // “+”文本大小为14sp
                    spannableString.length - 1,
                    spannableString.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                // 添加粗体样式
                spannableString.setSpan(
                    StyleSpan(Typeface.BOLD),
                    spannableString.length - 1,
                    spannableString.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                // 添加空格表示2dp间隔
                spannableString.append(" ") // 在“+”和价格之间添加2个空格
            }

            // 添加价格值
            val priceText = it.normalFormat() // 格式化价格
            spannableString.append(priceText)
            spannableString.setSpan(
                AbsoluteSizeSpan(mainTextSize, true), // 价格数字大小为16sp
                spannableString.length - priceText.length,
                spannableString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // 添加粗体样式
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                spannableString.length - priceText.length,
                spannableString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            if (isNeedSpace) {
                // 添加空格表示2dp间隔
                spannableString.append("\u00A0") // 在价格和“元”之间添加2个空格
            }

            // 添加“元”字样
            spannableString.append("元")
            spannableString.setSpan(
                AbsoluteSizeSpan(minorTextSize, true), // “元”文本大小为12sp
                spannableString.length - 1, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            chineseTextColor?.let { color ->
                spannableString.setSpan(
                    ForegroundColorSpan(color),
                    spannableString.length - 1,
                    spannableString.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }
    return spannableString
}