package com.zkxy.shop.network.api

import com.gxy.common.network.api.ApiResult
import com.zkxy.shop.appLoadLat
import com.zkxy.shop.appLoadLon
import com.zkxy.shop.appPlatformId
import com.zkxy.shop.appUserName
import com.zkxy.shop.appUserTel
import com.zkxy.shop.entity.category.GoodsCategoryEntity
import com.zkxy.shop.entity.goods.AddressBookEntity
import com.zkxy.shop.entity.goods.GoodsDetailsEntity
import com.zkxy.shop.entity.goods.OrderEntity
import com.zkxy.shop.entity.goods.PlaceOrderEntity
import com.zkxy.shop.entity.home.GoodsEntity
import com.zkxy.shop.entity.home.HomeShopBannerEntity
import com.zkxy.shop.entity.home.UserPointEntity
import com.zkxy.shop.entity.order.ConfirmAddressEntity
import com.zkxy.shop.entity.order.OrderDetailsEntity
import com.zkxy.shop.entity.order.OrderListEntity
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    companion object {
        const val RESPONSE_CODE_SUCCESS = "0"

        //一般我们成功的状态码返回是200，如果遇到特殊的成功状态码，需要在这里补充
        val SPECIAL_API_SUCCESS_STATUS_CODE = mapOf<String, String>()
    }

    /**
     * 商品分类
     */
    @POST("app/mall/goods/goodsType")
    @FormUrlEncoded
    suspend fun getGoodsCategory(
        @Field("platformId") platformId: Int = appPlatformId,
        @Field("currentPage") currentPage: Int? = 1,
        @Field("pageSize") pageSize: Int? = 10000
    ): GxyApiResult<MutableList<GoodsCategoryEntity>>

    @POST("app/mall/goods/goodsDetail")
    @FormUrlEncoded
    suspend fun goodsDetail(
        @Field("goodsId") goodsId: Int?, @Field("platformId") platformId: Int = appPlatformId
    ): GxyApiResult<GoodsDetailsEntity>

    /**
     * 搜索商品
     */
    @POST("app/mall/goods/searchGoods")
    @FormUrlEncoded
    suspend fun searchGoods(
        @Field("platformId") platformId: Int = appPlatformId,
        @Field("goodsName") goodsName: String? = null,
        @Field("goodsSuggest") goodsSuggest: Int? = null,
        @Field("currentPage") currentPage: Int?,
        @Field("pageSize") pageSize: Int?,
        @Field("priceType") priceType: Int? = null,
        @Field("levelType") levelType: Int? = null,
        @Field("typeId") typeId: Int? = null,
        @Field("goodsScorestart") goodsScorestart: Int? = null,
        @Field("goodsScoreEnd") goodsScoreEnd: Int? = null,
        @Field("sort") sort: Int? = null
    ): GxyApiResult<MutableList<GoodsEntity>>

    //获取首页轮播
    @POST("notice/noticeList")
    @FormUrlEncoded
    suspend fun getHomeBanner(
        @Field("platformId") platformId: Int = appPlatformId
    ): GxyApiResult<MutableList<HomeShopBannerEntity>>

    /**
     * 查询对应规格库存和自提点
     */
    @POST("app/mall/goods/goodsStockAddressSearch")
    @FormUrlEncoded
    suspend fun goodsStockAddressSearch(
        @Field("goodsId") goodsId: Int?,
        @Field("deliveryMode") deliveryMode: Int?,
        @Field("loadLon") upLoadLon: String? = appLoadLon,
        @Field("loadLat") upLoadLat: String? = appLoadLat,
        @Field("platformId") platformId: Int = appPlatformId
    ): GxyApiResult<PlaceOrderEntity>

    //获取用户常用地址
    @POST("sys/getUserAddress")
    @FormUrlEncoded
    suspend fun getUserAddress(
        @Field("tel") tel: String? = appUserTel,
    ): GxyApiResult<MutableList<AddressBookEntity>>

    //添加用户常用地址
    @POST("sys/addUserAddress")
    @FormUrlEncoded
    suspend fun addUserAddress(
        @Field("address") address: String?,
        @Field("administrativeRegion") administrativeRegion: String?,
        @Field("tel") tel: String? = appUserTel,
        @Field("contactName") contactName: String? = "",
        @Field("contactTel") contactTel: String? = appUserTel,
        @Field("acquiesce") acquiesce: Int? = 0,
    ): GxyApiResult<Any>

    //修改用户常用地址
    @POST("sys/updateUserAddress")
    @FormUrlEncoded
    suspend fun updateAddress(
        @Field("address") address: String?,
        @Field("addressId") addressId: Int?,
        @Field("administrativeRegion") administrativeRegion: String?,
    ): GxyApiResult<Any>

    //设置默认地址
    @POST("sys/addressDefault")
    @FormUrlEncoded
    suspend fun acquiesceAddress(
        @Field("addressId") addressId: Int?,
        @Field("acquiesce") acquiesce: Int?,
        @Field("platformId") platformId: Int = appPlatformId,
        @Field("tel") tel: String? = appUserTel
    ): GxyApiResult<Any>

    //删除地址
    @POST("sys/deleteAddress")
    @FormUrlEncoded
    suspend fun deleteAddress(
        @Field("addressId") addressId: Int?,
        @Field("tel") tel: String? = appUserTel,
        @Field("platformId") platformId: Int = appPlatformId,
        @Field("deleteFlag") deleteFlag: Int? = -1
    ): GxyApiResult<Any>

    //创建订单
    @POST("order/create")
    @FormUrlEncoded
    suspend fun createOrder(
        @Field("deliveryAddress") deliveryAddress: String?,
        @Field("consignee") consignee: String?,
        @Field("consigneeTel") consigneeTel: String?,
        @Field("goodsId") goodsId: Int?,
        @Field("goodsNum") goodsNum: Int?,
        @Field("goodsSpecId") goodsSpecId: Int?,
        @Field("deliveryType") deliveryType: Int?,
        @Field("orderPlacer") orderPlacer: String? = appUserName,
        @Field("orderPlacerTel") orderPlacerTel: String? = appUserTel,
        @Field("payWay") payWay: Int? = 0,
        @Field("platformId") platformId: Int = appPlatformId
    ): GxyApiResult<OrderEntity>

    /**
     * 获取用户积分
     */
    @POST("points/getPoints")
    @FormUrlEncoded
    suspend fun getUserPoint(
        @Field("platformId") platformId: Int = appPlatformId,
        @Field("tel") phoneNumber: String?
    ): GxyApiResult<UserPointEntity>

    //订单列表
    @POST("order/orderListAPP")
    @FormUrlEncoded
    suspend fun orderListAPP(
        @Field("current") current: Int?,
        @FieldMap statusIds: MutableMap<String, MutableList<Int>?>?,
        @Field("key") key: String?,
        @Field("orderPlacerTel") orderPlacerTel: String? = appUserTel,
        @Field("platformId") platformId: Int = appPlatformId,
        @Field("size") size: Int?
    ): ApiResult<MutableList<OrderListEntity>>

    //订单详情
    @POST("order/detailsApp")
    @FormUrlEncoded
    suspend fun orderDetails(
        @Field("orderId") orderId: Int?,
        @Field("platformId") platformId: Int = appPlatformId,
    ): GxyApiResult<OrderDetailsEntity>

    //取消订单
    @POST("order/APPCancel")
    @FormUrlEncoded
    suspend fun orderCancel(
        @Field("orderId") orderId: Int?,
        @Field("orderDesc") orderDesc: String? = "",
        @Field("platformId") platformId: Int = appPlatformId,
    ): GxyApiResult<Any>

    //支付订单
    @POST("order/orderPayment")
    @FormUrlEncoded
    suspend fun payment(
        @Field("orderCode") orderCode: String?,
        @Field("platformId") platformId: Int = appPlatformId,
    ): GxyApiResult<Any>

    //获取自提点
    @POST("order/getAPPAddress")
    @FormUrlEncoded
    suspend fun getAPPAddress(
        @Field("goodsId") goodsId: Int?,
        @Field("deliveryType") deliveryType: Int = 2
    ): GxyApiResult<MutableList<ConfirmAddressEntity>?>

    //自提点选择
    @POST("order/shipmentsApp")
    @FormUrlEncoded
    suspend fun shipmentsApp(
        @Field("orderId") orderId: Int?,
        @Field("deliveryCode") deliveryCode: String?,
        @Field("shipmentsAddress") shipmentsAddress: String?,
        @Field("shipmentsAddressId") shipmentsAddressId: Int?,
        @Field("driveTel") driveTel: String? = appUserTel,
        @Field("driveName") driveName: String? = appUserName
    ): GxyApiResult<String?>

}