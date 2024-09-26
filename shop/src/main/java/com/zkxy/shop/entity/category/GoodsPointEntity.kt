package com.zkxy.shop.entity.category

import androidx.annotation.Keep
import com.zkxy.shop.ui.goods.RuleType

/**
 * @author zhangyuxiang
 * @date 2024/9/26
 */
@Keep
data class GoodsPointEntity(
    val name: String?, val ruleType: RuleType, var isSelect: Boolean? = false
)