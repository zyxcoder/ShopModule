package com.zyxcoder.mvvmroot.base.activity

import android.view.View
import androidx.databinding.ViewDataBinding
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.utils.inflateBindingWithGeneric

/**
 * Create by zyx_coder on 2022/11/17
 * 包含ViewModel 和Databind ViewModelActivity基类，把ViewModel 和Databind注入进来了
 * 需要使用Databind继承它
 */
abstract class BaseVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmActivity<VM>() {

    override fun layoutId(): Int = 0

    lateinit var mDatabind: DB

    override fun initDataBind(): View? {
        mDatabind = inflateBindingWithGeneric(layoutInflater)
        return mDatabind.root
    }
}