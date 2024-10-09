package com.zkxy.shop.entity.goods

import androidx.annotation.Keep

@Keep
data class AddressBookEntity(
    var acquiesce: Int?,
    val address: String?,
    val addressId: Int?,
    val administrativeRegion: String?,
    val contactTel: String?,
    val createTime: String?,
    val deleteFlag: Int?,
    val platformId: Int?,
    val tel: String?,
    val updateTime: String?,
    var isCheck: Boolean

)
