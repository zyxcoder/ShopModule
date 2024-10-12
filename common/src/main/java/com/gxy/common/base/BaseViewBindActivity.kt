package com.gxy.common.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.gxy.common.ext.dismissLoadingExt
import com.gxy.common.ext.showLoadingExt
import com.gyf.immersionbar.ImmersionBar
import com.zyxcoder.mvvmroot.base.activity.BaseVmVbActivity
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel

/**
 * @author zhangyuxiang
 * @date 2024/2/22
 */
abstract class BaseViewBindActivity<VM : BaseViewModel, VB : ViewBinding> :
    BaseVmVbActivity<VM, VB>() {


    abstract fun init(savedInstanceState: Bundle?)

    /**
     * 是否需要状态栏
     */
    open fun needStatusBar() = true

    /**
     * 状态是否为深色
     */
    open fun statusBarIsDarkFont() = true

    /**
     * 解决软键盘与底部输入框冲突问题,默认为false
     */
    open fun keyboardEnable() = false

    override fun createObserver() {
    }

    override fun showLoading(message: String) {
        showLoadingExt()
    }

    override fun dismissLoading() {
        dismissLoadingExt()
    }

    override fun initData() {
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (needStatusBar()) {
            ImmersionBar.with(this).keyboardEnable(keyboardEnable())
                .statusBarDarkFont(statusBarIsDarkFont()) //状态栏字体是深色，不写默认为亮色
                .init() //必须调用方可沉浸式
        } else {
            ImmersionBar.with(this).init()
        }
        init(savedInstanceState)
    }
}