package com.gxy.common.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gxy.common.R
import com.gxy.common.utils.getScreenHeight
import com.gxy.common.utils.inflateBindingWithBottomVBDialogFragmentGeneric
import com.zyxcoder.mvvmroot.base.appContext
import com.zyxcoder.mvvmroot.base.viewmodel.BaseViewModel
import com.zyxcoder.mvvmroot.ext.getVmClazz

/**
 * 通用BottomSheetDialogFragment
 * 除了bottomSheet的默认白色，方便实现各个圆角
 */
abstract class BaseBottomVBDialogFragment<VM : BaseViewModel, VB : ViewBinding> :
    BottomSheetDialogFragment() {

    lateinit var mViewModel: VM

    //该类绑定的 ViewBinding
    private var _binding: VB? = null
    val mViewBind: VB get() = _binding!!

    /**
     * 创建DataBinding
     */
    private fun initDataBind(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = inflateBindingWithBottomVBDialogFragmentGeneric(inflater, container, false)
        return mViewBind.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 是否背景变暗
        if (!isBackgroundDimEnabled()) {
            setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogFragmentTheme)
        } else {
            setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogFragmentThemeDim)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            attributes = attributes.apply {
                windowAnimations = R.style.popwin_anim_style
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 去除BottomSheet默认的白色背景以实现圆角
        val bottomSheet: View = (dialog as BottomSheetDialog).delegate
            .findViewById(com.google.android.material.R.id.design_bottom_sheet)
            ?: return
        // 初始完全展开
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isHideable = couldScrollDownClose()
        resetPeekHeight()?.let { behavior.peekHeight = it }

        bottomSheet.updateLayoutParams<ViewGroup.LayoutParams> {
            height = if (resetDialogHeightPercent() == 0f) {
                ViewGroup.LayoutParams.MATCH_PARENT
            } else {
                ((context?.getScreenHeight() ?: 0) * resetDialogHeightPercent()).toInt()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initDataBind(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = createViewModel()
        initView()
        initData()
        createObserver()
    }

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application = appContext)
        )[getVmClazz(this)]
    }

    open fun initView() {
        // Override if need
    }

    open fun initData() {
        // Override if need
    }

    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()

    /**
     * 设置弹窗的高度,不设置就MATCH_PARENT了
     */
    open fun resetDialogHeightPercent(): Float = 0.0f

    /**
     * 设置弹窗的高度,不设置就MATCH_PARENT了
     */
    open fun couldScrollDownClose(): Boolean = true

    /**
     * 重新设置的peekHeight，折叠后的高度
     */
    open fun resetPeekHeight(): Int? = context?.getScreenHeight()

    /**
     * 是否背景变暗，默认变暗
     */
    open fun isBackgroundDimEnabled() = true

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (e: Exception) {
        }
    }
}
