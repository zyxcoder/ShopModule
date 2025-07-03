package com.zk.common.common.loadsir

import com.zk.common.R
import com.kingja.loadsir.callback.Callback

/**
 * @author zhangyuxiang
 * @date 2024/1/26
 */
class ErrorCallback : Callback() {
    override fun onCreateView(): Int = R.layout.view_error_result
}