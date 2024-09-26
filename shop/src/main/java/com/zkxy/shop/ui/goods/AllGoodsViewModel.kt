package com.zkxy.shop.ui.goods

import androidx.lifecycle.MutableLiveData
import com.gxy.common.common.loadsir.LoadContentStatus
import com.gxy.common.network.api.ApiResult
import com.zkxy.shop.entity.category.CategoryEntity
import com.zkxy.shop.entity.category.CategoryMinorEntity
import com.zkxy.shop.entity.category.CategorySecondaryEntity
import com.zkxy.shop.entity.home.GoodsEntity
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

/**
 * @author zhangyuxiang
 * @date 2024/9/25
 */
class AllGoodsViewModel : BaseViewModel() {


    val loadCategoryContentStatus = MutableLiveData<LoadContentStatus>()

    val categoryDataList = MutableLiveData<MutableList<CategoryEntity>>()


    //页面每次请求的数据条数，可修改
    var pageSize = 10

    val isRefreshing = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val firstGoodsDatas = MutableLiveData<MutableList<GoodsEntity>>()
    val moreGoodsDatas = MutableLiveData<MutableList<GoodsEntity>>()
    val loadGoodsContentStatus = MutableLiveData<LoadContentStatus>()
    val dataHasMore = MutableLiveData<Boolean>()

    /**
     * 获取列表数据
     * @param isFirst 是否是第一次请求
     * @param isRefresh 是否是下拉刷新
     * @param params 可变参数
     */
    fun getGoodsData(
        isFirst: Boolean,
        isRefresh: Boolean,
        start: Int
    ) {
        request<Job>(block = {
            if (isFirst) {
                loadGoodsContentStatus.value = LoadContentStatus.DEFAULT_LOADING
            }
            if (isRefresh || isFirst) {
                isRefreshing.value = true
            } else {
                isLoading.value = true
            }

            //todo 获取列表数据
            delay(1000)
            val goodsList = mutableListOf<GoodsEntity>()
            repeat(10) {
                if (it % 2 == 0) {
                    goodsList.add(
                        GoodsEntity(
                            goodsName = "清风原木纯品金",
                            goodsUrl = "https://gd-hbimg.huaban.com/310d6a3729d1e1199f4aa07e275425f92e694eedf4678-JtZ2Oq_fw1200webp",
                            goodsInventory = 10,
                            goodsId = 1,
                            goodsPoint = 23.9,
                            goodsPrice = 38.9
                        )
                    )
                } else {
                    goodsList.add(
                        GoodsEntity(
                            goodsName = "清风原木纯品金装清风原木纯品金装系列抽取式纸巾150抽x3层清风原木纯品金装系列抽取式纸巾150抽x3层清风原木纯品金装系列抽取式纸巾150抽x3层系列抽取式纸巾150抽x3层 12包",
                            goodsUrl = "https://gd-hbimg.huaban.com/468d5c6d327b411c5041fdd4d9ddb7b6d9015bb2d6d9b-GBjrha_fw1200webp",
                            goodsInventory = 0,
                            goodsId = 1,
                            goodsPoint = null,
                            goodsPrice = 67.9
                        )
                    )
                }
            }
            val apiResult = ApiResult<MutableList<GoodsEntity>>(
                statusDesc = "qwqwwq",
                statusCode = "0",
                listCount = 10,
                hasMore = true,
                data = goodsList
            )
            //todo 获取列表数据


            val dataList = apiResult.apiData()
            //当有更多数据时，后端返回的data的大小是大于等于pageSize
            dataHasMore.value = dataList.size >= pageSize
            if (isFirst || isRefresh) {
                if (dataList.isEmpty()) {
                    loadGoodsContentStatus.value = LoadContentStatus.DEFAULT_EMPTY
                } else {
                    loadGoodsContentStatus.value = LoadContentStatus.SUCCESS
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
                loadGoodsContentStatus.value = LoadContentStatus.DEFAULT_ERROR
            }
            if (isRefresh || isFirst) {
                isRefreshing.value = false
            } else {
                isLoading.value = false
            }
            dataHasMore.value = false
        })
    }


    fun fetchCategory() {
        request<Job>(block = {
            loadCategoryContentStatus.value = LoadContentStatus.DEFAULT_LOADING


            //todo 模拟网络请求
            delay(1000)
            val list = arrayListOf<CategoryEntity>()
            val categoryMinorList = arrayListOf<CategoryMinorEntity>()
            val categorySecondaryList = arrayListOf<CategorySecondaryEntity>()
            repeat(10) {
                categoryMinorList.add(
                    CategoryMinorEntity(
                        categoryId = it, categoryName = "进口洗发水123" + it
                    )
                )
            }
            repeat(10) {
                categorySecondaryList.add(
                    CategorySecondaryEntity(
                        categoryId = it,
                        categoryName = "日用品123456" + it,
                        categoryMinorList = categoryMinorList
                    )
                )
            }

            repeat(10) {
                list.add(
                    CategoryEntity(
                        categoryId = it,
                        categoryName = "个护清洁数据" + it,
                        isSelect = it == 0,
                        categorySecondaryList = categorySecondaryList
                    )
                )
            }
            categoryDataList.value = list

            if ((categoryDataList.value?.size ?: 0) > 0) {
                loadCategoryContentStatus.value = LoadContentStatus.SUCCESS
                //第一次请求完分类数据后，请求一次商品数据
                getGoodsData(isFirst = true, isRefresh = false, start = 0)
            } else {
                loadCategoryContentStatus.value = LoadContentStatus.DEFAULT_EMPTY
            }
        }, error = {
            loadCategoryContentStatus.value = LoadContentStatus.DEFAULT_ERROR
        })
    }
}