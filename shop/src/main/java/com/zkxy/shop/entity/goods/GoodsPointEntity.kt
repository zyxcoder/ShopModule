package com.zkxy.shop.entity.goods

import androidx.annotation.Keep

/**
 * @author zhangyuxiang
 * @date 2024/9/26
 */
@Keep
data class GoodsPointEntity(
    val name: String?, val ruleType: RuleType, var isSelect: Boolean? = false,
    val goodsScorestart: Int? = null, val goodsScoreEnd: Int? = null
)