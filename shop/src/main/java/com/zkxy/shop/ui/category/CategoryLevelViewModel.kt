package com.zkxy.shop.ui.category

import androidx.lifecycle.MutableLiveData
import com.gxy.common.common.loadsir.LoadContentStatus
import com.zkxy.shop.entity.category.GoodsCategoryEntity
import com.zkxy.shop.entity.goods.AllGoodsType
import com.zkxy.shop.entity.goods.RuleType
import com.zkxy.shop.entity.goods.SortRule
import com.zkxy.shop.entity.goods.goodsPointRuleList
import com.zkxy.shop.entity.home.GoodsEntity
import com.zkxy.shop.network.request.apiService
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job

/**
 * @author zhangyuxiang
 * @date 2024/9/27
 */
class CategoryLevelViewModel : BaseViewModel() {


    //页面每次请求的数据条数，可修改
    private var pageSize = 10

    val isRefreshing = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val firstGoodsDatas = MutableLiveData<MutableList<GoodsEntity>>()
    val moreGoodsDatas = MutableLiveData<MutableList<GoodsEntity>>()
    val loadContentStatus = MutableLiveData<LoadContentStatus>()
    val dataHasMore = MutableLiveData<Boolean>()

    /**
     * 获取列表数据
     * @param isFirst 是否是第一次请求
     * @param isRefresh 是否是下拉刷新
     * @param start 开始位置
     * @param selectAllGoodsType 选择商品类型:积分商品，现金商品
     * @param selectGoodsCategory 选择的商品分类，这里是三级分类
     * @param selectSortRule 选择的排序规则
     */
    fun getGoodsData(
        isFirst: Boolean,
        isRefresh: Boolean,
        start: Int,
        selectAllGoodsType: AllGoodsType,
        selectGoodsCategory: GoodsCategoryEntity?,
        selectSortRule: SortRule
    ) {
        request<Job>(block = {
            if (isFirst) {
                loadContentStatus.value = LoadContentStatus.DEFAULT_LOADING
            }
            if (isRefresh || isFirst) {
                isRefreshing.value = true
            } else {
                isLoading.value = true
            }

            val dataList = apiService.searchGoods(
                currentPage = start / pageSize + 1,
                pageSize = pageSize,
                priceType = if (selectAllGoodsType == AllGoodsType.AllGoodsPoint) 1 else 2,
                levelType = selectGoodsCategory?.levelType,
                typeId = selectGoodsCategory?.typeId,
                goodsScorestart = if (selectSortRule == SortRule.POINT_SORT) {
                    goodsPointRuleList.findLast { selectSortRule.ruleType == it.ruleType }?.goodsScorestart
                } else {
                    null
                },
                goodsScoreEnd = if (selectSortRule == SortRule.POINT_SORT) {
                    goodsPointRuleList.findLast { selectSortRule.ruleType == it.ruleType }?.goodsScoreEnd
                } else {
                    null
                },
                sort = when (selectSortRule.ruleType) {
                    RuleType.PRICE_DOWN_SORT -> {
                        1
                    }

                    RuleType.PRICE_UP_SORT -> {
                        2
                    }

                    RuleType.POINT_DOWN_SORT -> {
                        3
                    }

                    RuleType.POINT_UP_SORT -> {
                        4
                    }

                    else -> {
                        null
                    }
                }
            ).apiData()
            //当有更多数据时，后端返回的data的大小是大于等于pageSize
            dataHasMore.value = dataList.size >= pageSize
            if (isFirst || isRefresh) {
                if (dataList.isEmpty()) {
                    loadContentStatus.value = LoadContentStatus.DEFAULT_EMPTY
                } else {
                    loadContentStatus.value = LoadContentStatus.SUCCESS
                }
            }
            if (isRefresh || isFirst) {
                firstGoodsDatas.value = dataList
                isRefreshing.value = false
            } else {
                moreGoodsDatas.value = dataList
                isLoading.value = false
            }
        }, error = {
            if (isFirst || isRefresh) {
                loadContentStatus.value = LoadContentStatus.DEFAULT_ERROR
            }
            if (isRefresh || isFirst) {
                isRefreshing.value = false
            } else {
                isLoading.value = false
            }
            dataHasMore.value = false
        })
    }
}