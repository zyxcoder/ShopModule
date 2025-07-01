package com.zkxy.shop.utils

import com.zyxcoder.mvvmroot.callback.lifecycle.ActivityManger

/**
 * @author zhangyuxiang
 * @date 2024/3/7
 */


/**
 * 退出登录
 */
fun loginOut() {
    //清空所有页面
    ActivityManger.finishAllActivity()
}