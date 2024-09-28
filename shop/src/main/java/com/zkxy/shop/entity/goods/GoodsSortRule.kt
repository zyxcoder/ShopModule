package com.zkxy.shop.entity.goods

import androidx.annotation.Keep

/**
 * @author zhangyuxiang
 * @date 2024/9/26
 */
/**
 * kotlin 1.7用密封类包对象不行，需要升级1.9，为了兼容，这里只能用枚举
 */
@Keep
enum class AllGoodsType(var title: String) {
    AllGoodsPoint("积分商品"), AllGoodsCash("现金商品")
}

@Keep
enum class RuleType(val content: String? = null) {
    DEFAULT_SORT("默认排序"),//默认排序
    PRICE_UP_SORT("价格升序"),//价格升序
    PRICE_DOWN_SORT("价格降序"),//价格降序
    POINT_1_500_SORT("1-500积分"),//1-500积分
    POINT_500_1000_SORT("500-1000积分"),//500-1000积分
    POINT_1000_1500_SORT("1000-1500积分"),//1000-1500积分
    POINT_1500_3000_SORT("1500-3000积分"),//1500-3000积分
    POINT_3000_5000_SORT("3000-5000积分"), //3000-5000积分
    POINT_5000_10000_SORT("5000-10000积分"), //5000-10000积分
    POINT_10000_SORT("10000积分以上") //10000积分以上
}

@Keep
enum class SortRule(var ruleType: RuleType? = null) {
    DEFAULT_SORT(RuleType.DEFAULT_SORT), //默认排序
    PRICE_SORT(RuleType.PRICE_DOWN_SORT), //价格排序
    POINT_SORT(RuleType.POINT_1_500_SORT)//积分排序
}


/**
 * 积分规则排序数组
 */
val goodsPointRuleList = arrayListOf(
    GoodsPointEntity(
        name = RuleType.POINT_1_500_SORT.content, ruleType = RuleType.POINT_1_500_SORT
    ), GoodsPointEntity(
        name = RuleType.POINT_500_1000_SORT.content, ruleType = RuleType.POINT_500_1000_SORT
    ), GoodsPointEntity(
        name = RuleType.POINT_1000_1500_SORT.content, ruleType = RuleType.POINT_1000_1500_SORT
    ), GoodsPointEntity(
        name = RuleType.POINT_1500_3000_SORT.content, ruleType = RuleType.POINT_1500_3000_SORT
    ), GoodsPointEntity(
        name = RuleType.POINT_3000_5000_SORT.content, ruleType = RuleType.POINT_3000_5000_SORT
    ), GoodsPointEntity(
        name = RuleType.POINT_5000_10000_SORT.content, ruleType = RuleType.POINT_5000_10000_SORT
    ), GoodsPointEntity(
        name = RuleType.POINT_10000_SORT.content, ruleType = RuleType.POINT_10000_SORT
    )
)
