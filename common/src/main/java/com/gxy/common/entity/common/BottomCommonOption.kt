package com.gxy.common.entity.common

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author zhangyuxiang
 * @date 2024/4/24
 */
@Keep
data class BottomCommonOption(
    @SerializedName("coalType") val coalType: List<HashMap<String?, String?>>?,
    @SerializedName("coalshedId") val coalshedId: List<HashMap<String?, String?>>?,
    @SerializedName("materialTypeId") val materialTypeId: List<HashMap<String?, String?>>?,
    @SerializedName("materialUnitId") val materialUnitId: List<HashMap<String?, String?>>?,
    @SerializedName("auditId") val auditId: List<HashMap<String?, String?>>?,
    @SerializedName("invoiceTypeId") val invoiceTypeId: List<HashMap<String?, String?>>?,
    @SerializedName("materialId") val materialId: List<HashMap<String?, String?>>?,
    @SerializedName("paybasisId") val paybasisId: List<HashMap<String?, String?>>?,
    @SerializedName("paytypeId") val paytypeId: List<HashMap<String?, String?>>?,
    @SerializedName("purchaseId") val purchaseId: List<HashMap<String?, String?>>?,
    @SerializedName("purposeId") val purposeId: List<HashMap<String?, String?>>?,
    @SerializedName("roadtypeId") val roadtypeId: List<HashMap<String?, String?>>?,
    @SerializedName("saleId") val saleId: List<HashMap<String?, String?>>?,
    @SerializedName("schemeId") val schemeId: List<HashMap<String?, String?>>?,
    @SerializedName("transportId") val transportId: List<HashMap<String?, String?>>?,
    @SerializedName("jgAddrId") val jgAddrId: List<HashMap<String?, String?>>?,
    @SerializedName("signTypeId") val signTypeId: List<HashMap<String?, String?>>?,
    @SerializedName("lossTypeId") val lossTypeId: List<HashMap<String?, String?>>?,
    @SerializedName("weightTypeId") val weightTypeId: List<HashMap<String?, String?>>?,
    @SerializedName("logisticsId") val logisticsId: List<HashMap<String?, String?>>?,
    @SerializedName("orderPayTypeId") val orderPayTypeId: List<HashMap<String?, String?>>?,
    @SerializedName("orderTransportId") val orderTransportId: List<HashMap<String?, String?>>?,
    @SerializedName("groupId") val groupId: List<HashMap<String?, String?>>?,
    @SerializedName("rechargePayType") val rechargePayType: List<HashMap<String?, String?>>?,
    @SerializedName("roleId") val roleId: List<HashMap<String?, String?>>?,
    @SerializedName("assaytypeId") val assaytypeId: List<HashMap<String?, String?>>?,
    @SerializedName("assayindexId") val assayindexId: List<HashMap<String?, String?>>?,
    @SerializedName("carrierId") val carrierId: List<HashMap<String?, String?>>?,
    @SerializedName("truckId") val truckId: List<HashMap<String?, String?>>?,
    @SerializedName("bindingType") val bindingType: List<HashMap<String?, String?>>?,
    @SerializedName("billDriver") val billDriver: List<HashMap<String?, String?>>?,
    @SerializedName("billTruck") val billTruck: List<HashMap<String?, String?>>?
) {
    fun getValueByKey(key: String?): List<HashMap<String?, String?>>? {
        return when (key) {
            "coalType" -> coalType
            "coalshedId" -> coalshedId
            "materialTypeId" -> materialTypeId
            "materialUnitId" -> materialUnitId
            "auditId" -> auditId
            "invoiceTypeId" -> invoiceTypeId
            "materialId" -> materialId
            "paybasisId" -> paybasisId
            "paytypeId" -> paytypeId
            "purchaseId" -> purchaseId
            "purposeId" -> purposeId
            "roadtypeId" -> roadtypeId
            "saleId" -> saleId
            "schemeId" -> schemeId
            "transportId" -> transportId
            "jgAddrId" -> jgAddrId
            "signTypeId" -> signTypeId
            "lossTypeId" -> lossTypeId
            "weightTypeId" -> weightTypeId
            "logisticsId" -> logisticsId
            "orderPayTypeId" -> orderPayTypeId
            "orderTransportId" -> orderTransportId
            "groupId" -> groupId
            "rechargePayType" -> rechargePayType
            "roleId" -> roleId
            "assaytypeId" -> assaytypeId
            "assayindexId" -> assayindexId
            "carrierId" -> carrierId
            "truckId" -> truckId
            "bindingType" -> bindingType
            "billTruck" -> billTruck
            "billDriver" -> billDriver
            else -> null
        }
    }
}