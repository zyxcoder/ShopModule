package com.zyxcoder.mvvmroot.network

/**
 * Create by zyx_coder on 2022/11/18
 */
class ApiException(var code: Int, override var message: String) : RuntimeException()