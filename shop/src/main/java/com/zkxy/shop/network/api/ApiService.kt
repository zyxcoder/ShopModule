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
import com.zkxy.shop.entity.goods.RateEntity
import com.zkxy.shop.entity.home.GoodsEntity
import com.zkxy.shop.entity.home.HomeShopBannerEntity
import com.zkxy.shop.entity.home.UserPointEntity
import com.zkxy.shop.entity.order.AfterSaleDetailEntity
import com.zkxy.shop.entity.order.ConfirmAddressEntity
import com.zkxy.shop.entity.order.OrderDetailsEntity
import com.zkxy.shop.entity.order.OrderListEntity
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    companion object {
        const val RESPONSE_CODE_SUCCESS = "0"

        //一般我们成功的状态码返回是200，如果遇到特殊的成功状态码，需要在这里补充
        val SPECIAL_API_SUCCESS_STATUS_CODE = mapOf<String, String>()
    }

    /**
     * 商品分类
     */
    @POST("v1/app/shopMallGoods/goodsType")
    @FormUrlEncoded
    suspend fun getGoodsCategory(
        @Field("platformId") platformId: Int = appPlatformId,
        @Field("currentPage") currentPage: Int? = 1,
        @Field("pageSize") pageSize: Int? = 10000
    ): GxyApiResult<MutableList<GoodsCategoryEntity>>

    @POST("v1/app/shopMallGoods/goodsDetail")
    @FormUrlEncoded
    suspend fun goodsDetail(
        @Field("goodsId") goodsId: Int?, @Field("platformId") platformId: Int = appPlatformId
    ): GxyApiResult<GoodsDetailsEntity>

    /**
     * 搜索商品
     */
    @POST("v1/app/shopMallGoods/searchGoods")
    @FormUrlEncoded
    suspend fun searchGoods(
        @Field("platformId") platformId: Int = appPlatformId,
        @Field("goodsName") goodsName: String? = null,
        @Field("goodsSuggest") goodsSuggest: Int? = null,
        @Field("currentPage") currentPage: Int?,
        @Field("currentPageSize") pageSize: Int?,
        @Field("priceType") priceType: Int? = null,
        @Field("levelType") levelType: Int? = null,
        @Field("typeId") typeId: Int? = null,
        @Field("goodsScorestart") goodsScorestart: Int? = null,
        @Field("goodsScoreEnd") goodsScoreEnd: Int? = null,
        @Field("sort") sort: Int? = null
    ): GxyApiResult<MutableList<GoodsEntity>>

    //获取首页轮播
    @GET("v1/app/sysAd/getAd")
    suspend fun getHomeBanner(
        @Query("location") location: Int = 2
    ): GxyApiResult<MutableList<HomeShopBannerEntity>>

    /**
     * 查询对应规格库存和自提点
     */
    @POST("v1/app/shopMallGoods/goodsStockAddressSearch")
    @FormUrlEncoded
    suspend fun goodsStockAddressSearch(
        @Field("goodsId") goodsId: Int?,
        @Field("deliveryMode") deliveryMode: Int?,
        @Field("loadLon") upLoadLon: String? = appLoadLon,
        @Field("loadLat") upLoadLat: String? = appLoadLat,
        @Field("platformId") platformId: Int = appPlatformId
    ): GxyApiResult<PlaceOrderEntity>

    //获取用户常用地址
    @POST("v1/app/shopMallGoods/getUserAddress")
    @FormUrlEncoded
    suspend fun getUserAddress(
        @Field("tel") tel: String? = appUserTel,
    ): GxyApiResult<MutableList<AddressBookEntity>>

    //添加用户常用地址
    @POST("v1/app/shopMallGoods/addUserAddress")
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
    @POST("v1/app/shopMallGoods/updateUserAddress")
    @FormUrlEncoded
    suspend fun updateAddress(
        @Field("address") address: String?,
        @Field("addressId") addressId: Int?,
        @Field("administrativeRegion") administrativeRegion: String?,
    ): GxyApiResult<Any>

    //设置默认地址
    @POST("v1/app/shopMallGoods/addressDefault")
    @FormUrlEncoded
    suspend fun acquiesceAddress(
        @Field("addressId") addressId: Int?,
        @Field("acquiesce") acquiesce: Int?,
        @Field("platformId") platformId: Int = appPlatformId,
        @Field("tel") tel: String? = appUserTel
    ): GxyApiResult<Any>

    //删除地址
    @POST("v1/app/shopMallGoods/deleteAddress")
    @FormUrlEncoded
    suspend fun deleteAddress(
        @Field("addressId") addressId: Int?,
        @Field("tel") tel: String? = appUserTel,
        @Field("platformId") platformId: Int = appPlatformId,
        @Field("deleteFlag") deleteFlag: Int? = -1
    ): GxyApiResult<Any>

    //创建订单
    @POST("v1/app/shopMallGoods/create")
    @FormUrlEncoded
    suspend fun createOrder(
        @Field("deliveryAddress") deliveryAddress: String?,
        @Field("consignee") consignee: String?,
        @Field("consigneeTel") consigneeTel: String?,
        @Field("goodsId") goodsId: Int?,
        @Field("goodsNum") goodsNum: Int?,
        @Field("goodsSpecId") goodsSpecId: Int?,
        @Field("deliveryType") deliveryType: Int?,
        @Field("taxRate") taxRate: Int? = null,
        @Field("orderPlacer") orderPlacer: String? = appUserName,
        @Field("orderPlacerTel") orderPlacerTel: String? = appUserTel,
        @Field("payWay") payWay: Int? = 0,
        @Field("platformId") platformId: Int = appPlatformId
    ): GxyApiResult<OrderEntity>

    /**
     * 获取用户积分
     */
    @GET("v1/app/shopMallGoods/pointBalance")
    suspend fun getUserPoint(
        @Query("platformId") platformId: Int = appPlatformId,
        @Query("driverTel") phoneNumber: String?
    ): GxyApiResult<UserPointEntity>

    /**
     * 支付密码
     */
    @GET("v1/app/shopMallGoods/checkPwd")
    suspend fun checkUserPassWord(
        @Query("platformId") platformId: Int = appPlatformId,
        @Query("userTel") phoneNumber: String? = appUserTel,
        @Query("payPwd") password: String?
    ): GxyApiResult<String>

    //订单列表
    @POST("v1/app/shopMallGoods/orderListAPP")
    @FormUrlEncoded
    suspend fun orderListAPP(
        @Field("current") current: Int?,
        @FieldMap statusIds: MutableMap<String, MutableList<Int>?>?,
        @Field("key") key: String?,
        @Field("orderPlacerTel") orderPlacerTel: String? = appUserTel,
        @Field("platformId") platformId: Int = appPlatformId,
        @Field("size") size: Int?
    ): ApiResult<MutableList<OrderListEntity>>

    //售后列表
    @POST("v1/app/shopMallGoods/orderAfterSalesList")
    @FormUrlEncoded
    suspend fun orderAfterSalesList(
        @Field("current") current: Int?,
        @Field("key") key: String?,
        @Field("orderPlacerTel") orderPlacerTel: String? = appUserTel,
        @Field("platformId") platformId: Int = appPlatformId,
        @Field("size") size: Int?
    ): ApiResult<MutableList<OrderListEntity>>

    //售后撤销
    @POST("v1/app/shopMallGoods/orderAfterSalesRevoke")
    @FormUrlEncoded
    suspend fun orderAfterSalesRevoke(
        @Field("saleId") saleId: Int?,
        @Field("platformId") platformId: Int = appPlatformId,
        @Field("orderId") orderId: Int?
    ): ApiResult<Any>

    //申请售后
    @POST("v1/app/shopMallGoods/orderAfterSales")
    @FormUrlEncoded
    suspend fun orderAfterSales(
        @Field("cancelReason") cancelReason: String?,
        @Field("orderId") orderId: Int?,
        @Field("platformId") platformId: Int = appPlatformId
    ): ApiResult<Any>

    //订单详情
    @POST("v1/app/shopMallGoods/detailsApp")
    @FormUrlEncoded
    suspend fun orderDetails(
        @Field("orderId") orderId: Int?,
        @Field("platformId") platformId: Int = appPlatformId,
    ): GxyApiResult<OrderDetailsEntity>

    //取消订单
    @POST("v1/app/shopMallGoods/APPCancel")
    @FormUrlEncoded
    suspend fun orderCancel(
        @Field("orderId") orderId: Int?,
        @Field("orderDesc") orderDesc: String? = "",
        @Field("platformId") platformId: Int = appPlatformId,
    ): GxyApiResult<Any>

    //支付订单
    @POST("v1/app/shopMallGoods/orderPayment")
    @FormUrlEncoded
    suspend fun payment(
        @Field("orderCode") orderCode: String?,
        @Field("platformId") platformId: Int = appPlatformId,
    ): GxyApiResult<Any>

    //获取自提点
    @POST("v1/app/shopMallGoods/getAPPAddress")
    @FormUrlEncoded
    suspend fun getAPPAddress(
        @Field("goodsId") goodsId: Int?,
        @Field("deliveryType") deliveryType: Int = 2
    ): GxyApiResult<MutableList<ConfirmAddressEntity>?>

    //自提点选择
    @POST("v1/app/shopMallGoods/shipmentsApp")
    @FormUrlEncoded
    suspend fun shipmentsApp(
        @Field("orderId") orderId: Int?,
        @Field("deliveryCode") deliveryCode: String?,
        @Field("shipmentsAddress") shipmentsAddress: String?,
        @Field("shipmentsAddressId") shipmentsAddressId: Int?,
        @Field("driveTel") driveTel: String? = appUserTel,
        @Field("driveName") driveName: String? = appUserName
    ): GxyApiResult<String?>

    @GET("v1/app/shopMallGoods/getShippingFeeOrOilBalance")
    suspend fun getShippingFeeOrOilBlance(
        @Query("tel") tel: String? = appUserTel,
        @Query("platformId") platformId: Int = appPlatformId
    ): GxyApiResult<RateEntity>

    /**
     * 售后详情
     */
    @POST("/v1/app/shopMallGoods/orderAfterSalesDetails")
    @FormUrlEncoded
    suspend fun orderAfterSalesDetails(
        @Field("orderId") orderId: Int
    ): GxyApiResult<AfterSaleDetailEntity>
}