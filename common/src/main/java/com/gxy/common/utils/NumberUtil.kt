package com.gxy.common.utils


/**
 * @author zhangyuxiang
 * @date 2024/4/8
 */

import java.math.BigDecimal

object NumberUtil {
    private val CN_NUM = arrayOf("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖")
    private val CN_UNIT =
        arrayOf("", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟")
    private const val CN_NEGATIVE = "负"
    private const val CN_POINT = "点"

    private fun int2chineseNum(intNum: Int): String {
        var sb = StringBuilder()
        if (intNum == 0) {
            return sb.append("零").toString()
        }
        var intNum2 = intNum
        var isNegative = false
        if (intNum < 0) {
            isNegative = true
            intNum2 *= -1
        }
        var count = 0
        while (intNum2 > 0) {
            sb.insert(0, CN_NUM[intNum2 % 10] + CN_UNIT[count])
            intNum2 /= 10
            count++
        }

        if (isNegative) sb.insert(0, CN_NEGATIVE)


        return sb.toString().replace("零[千百十]".toRegex(), "零").replace("零+万".toRegex(), "万")
            .replace("零+亿".toRegex(), "亿").replace("亿万".toRegex(), "亿零")
            .replace("零+".toRegex(), "零").replace("零$".toRegex(), "")
    }

    fun bigDecimal2chineseNum(bigDecimalNum: BigDecimal): String {

        val sb = StringBuilder()

        val numStr = bigDecimalNum.abs().stripTrailingZeros().toPlainString()

        val split = numStr.split("\\.".toRegex()).toTypedArray()
        val integerStr = int2chineseNum(Integer.parseInt(split[0]))

        sb.append(integerStr)

        if (split.size == 2) {
            sb.append(CN_POINT)
            val decimalStr = split[1]
            val chars = decimalStr.toCharArray()
            for (i in chars.indices) {
                val index = Integer.parseInt(chars[i].toString())
                sb.append(CN_NUM[index])
            }
        }

        val signum = bigDecimalNum.signum()
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE)
        }

        if (split.size == 1) {
            sb.append("元整")
        } else {
            sb.append("元")
        }

        return sb.toString()
    }
}
