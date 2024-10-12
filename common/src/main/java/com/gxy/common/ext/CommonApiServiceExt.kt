package com.gxy.common.ext

import com.gxy.common.network.api.CommonApiService
import com.zyxcoder.mvvmroot.network.BaseNetworkApi

lateinit var baseNetworkApi: BaseNetworkApi
lateinit var COMMON_HTTP_URL: String

val commonApiService: CommonApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    baseNetworkApi.getApi(CommonApiService::class.java, COMMON_HTTP_URL)
}
