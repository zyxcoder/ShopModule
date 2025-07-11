package com.zkxy.shop.entity.order

import androidx.annotation.Keep

/**
 * @author zhangyuxiang
 * @date 2025/7/10
 */
@Keep
data class AfterSaleDetailEntity(
    val afterSaleAddress: Any?,
    val afterSaleApplyTime: String?,
    val afterSaleConfirmation: Any?,
    val afterSaleConfirmationId: Any?,
    val afterSaleConfirmationTime: Any?,
    val afterSaleDesc: String?,
    val afterSaleStatus: Any?,
    val afterSaleStatusName: Any?,
    val afterSaleType: Int?,
    val cancelReason: Any?,
    val cashMethod: String?,
    val consigner: String?,
    val consignerTel: String?,
    val createTime: String?,
    val deleteFlag: Int?,
    val goodsId: Int?,
    val goodsImg: String?,
    val goodsName: String?,
    val goodsNum: Int?,
    val goodsSource: Any?,
    val goodsSpecName: String?,
    val goodsType: String?,
    val mallAfterSaleDesc: Any?,
    val mallAfterSaleState: Any?,
    val orderCode: String?,
    val orderId: Int?,
    val orderPlacer: String?,
    val orderPlacerTel: String?,
    val payPoints: Double?,
    val payment: Double?,
    val platformPoints: Double?,
    val platformMoney: Double?,
    val refundConfirmer: Any?,
    val refundConfirmerId: Int?,
    val refundConfirmerTime: String?,
    val refundRefuseReason: String?,
    val refundTime: String?,
    val saleCode: String?,
    val saleId: Int?,
    val shippingTime: String?,
    val shopSource: Any?,
    val statusId: Int?,
    val tmsAfterSaleState: Int?,
    val updateTime: String?,
    val saleAddress: SaleAddressEntity?
)

@Keep
data class SaleAddressEntity(
    val addressId: Int?,
    val addressStatus: Int?,
    val deleteFlag: Int?,
    val saleAddress: String?,
    val saleConsignee: String?,
    val saleConsigneeTel: String?
)