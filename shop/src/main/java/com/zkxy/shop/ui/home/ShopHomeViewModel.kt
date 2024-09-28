package com.zkxy.shop.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gxy.common.common.loadsir.LoadContentStatus
import com.gxy.common.network.api.ApiResult
import com.zkxy.shop.entity.home.GoodsEntity
import com.zkxy.shop.entity.home.HomeShopBannerEntity
import com.zkxy.shop.network.request.apiService
import com.zkxy.shop.utils.AesUtils
import com.zkxy.shop.utils.RSAUtil
import com.zkxy.shop.utils.RandomKey
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.request
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.json.JSONObject

/**
 * @author zhangyuxiang
 * @date 2024/9/19
 */
class ShopHomeViewModel : BaseViewModel() {

    //页面每次请求的数据条数，可修改
    var pageSize = 10

    val isRefreshing = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val topBannerDatas = MutableLiveData<MutableList<HomeShopBannerEntity>>()
    val firstGoodsDatas = MutableLiveData<MutableList<GoodsEntity>>()
    val moreGoodsDatas = MutableLiveData<MutableList<GoodsEntity>>()
    val loadContentStatus = MutableLiveData<LoadContentStatus>()
    val dataHasMore = MutableLiveData<Boolean>()

    /**
     * 获取列表数据
     * @param isFirst 是否是第一次请求
     * @param isRefresh 是否是下拉刷新
     * @param params 可变参数
     */
    fun getData(
        isFirst: Boolean,
        isRefresh: Boolean,
        start: Int
    ) {
        request<Job>(block = {
            if (isFirst) {
                loadContentStatus.value = LoadContentStatus.DEFAULT_LOADING
            }





            val randomKey = RandomKey.generateRandomKey()

            val jsonObject = JSONObject().apply {
                put("platformId",2)
                put("currentPage", 1)
                put("pageSize", 10000)
            }
            val data = jsonObject.toString()


            val content = AesUtils.encryptData(data,randomKey)


            val aesKey = RSAUtil.encryptByPublicKey(randomKey)


            val body = mutableMapOf<String, Any?>(
                "aesKey" to aesKey,
                "content" to content
            )

            val jsondata=apiService.getGoodsCategory(body).apiNoData()

            Log.d("测试数据",
                AesUtils.decryptData(jsondata,randomKey))





            if (isRefresh || isFirst) {
                isRefreshing.value = true
                //todo 获取刷新的其他非列表数据
                topBannerDatas.value = arrayListOf(
                    HomeShopBannerEntity("https://gd-hbimg.huaban.com/4bd2502a1859e4bcc9d0afeda5b8851d98a267dd18c54-81OUAo_fw1200webp"),
                    HomeShopBannerEntity("https://gd-hbimg.huaban.com/efd67641fbefe040be67e09709ecc06f7721a1751338e-dUZfor_fw1200webp"),
                    HomeShopBannerEntity("https://gd-hbimg.huaban.com/bbbaf5b863d654a241df97b4f1135b3af770b5b95fe9f-p1uGUi_fw1200webp"),
                    HomeShopBannerEntity("https://gd-hbimg.huaban.com/8fab765e0b0e7403e95b5c9bc439157b10a322e1119b9-aWLIOx_fw1200webp")
                )
                //todo 获取刷新的其他非列表数据

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