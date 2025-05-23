package com.gxy.common.common.activitylist

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.MODE_FIXED
import com.google.android.material.tabs.TabLayout.Mode
import com.gxy.common.R
import com.gxy.common.base.BaseViewBindActivity
import com.gxy.common.common.adapter.SimpleFragmentPagerAdapter
import com.gxy.common.databinding.ActivityBaseMultipleCommonListBinding
import com.gxy.common.databinding.ItemMultipleTabCenterTextBinding
import com.gxy.common.utils.getScreenWidth
import com.zyxcoder.mvvmroot.utils.dpToPx

/**
 * @author zhangyuxiang
 * @date 2024/3/21
 * 带搜索的列表activity基类
 *
 * 由于MVVMBuild框架底层限制，第一个泛型必须是BaseViewModel，第二个必须是ViewBinding，虽然有点冗余，但前两个泛型必须写死，对于VB传入时就传ActivityBaseCommonListBinding即可
 */
abstract class BaseMultipleCommonListActivity<VM : BaseMultipleCommonListViewModel, VB : ActivityBaseMultipleCommonListBinding, ItemVB : ViewBinding, ItemDataEntity> :
    BaseViewBindActivity<VM, VB>() {

    /**
     * 返回列表页面需要刷新的intent
     */
    val intentActivityResultRefreshLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                startSearch()
            }
        }

    private val fragments by lazy {
        provideFragments().apply {
            repeat(size) { position ->
                get(position).currentFragmentInViewPagerIndex = position
            }
        }
    }

    /**
     * 设置搜索框是否可见
     */
    protected open fun provideSearchLayoutVisibility(): Boolean = true

    /**
     * 设置下面的滑动Fragment
     */
    protected abstract fun provideFragments(): ArrayList<BaseMultipleListFragment<out BaseMultipleListFragmentViewModel<*>, *, *, *>>

    /**
     * 设置标题文案
     */
    protected abstract fun provideTitleContent(): String

    /**
     * 设置右边标题是否可见
     */
    protected open fun provideRightTitleVisibility(): Boolean = false

    /**
     * 设置右边标题文案
     */
    protected open fun provideRightTitleContent(): String? = null


    /**
     * 设置搜索框文案
     */
    protected open fun provideSearchHintContent(): String? = null


    /**
     * 右边按钮的点击事件
     */
    protected open fun provideRightClickListener() {}

    /**
     * 设置右边图标是否可见
     */
    protected open fun provideRightIconVisibility(): Boolean = false

    /**
     * 设置右边图标资源
     */
    protected open fun provideRightIconRes(): Int = R.drawable.ic_download

    /**
     * 右边图标按钮的点击事件
     */
    protected open fun provideRightIconClickListener() {}

    /**
     * ViewPager是否可滑动,有些Page的viewPager不可以滑动，比如物料Page，需要手动控制
     */
    protected open fun provideViewPagerCanScroll(): Boolean = true

    /**
     * Tab的Padding值
     */
    protected open fun provideTabPadding(): Int = dpToPx(20f).toInt()

    /**
     * Tab是否需要显示数字
     */
    protected open fun provideTabHasCount(): Boolean = false


    /**
     * Tab初始选中的位置，某些页面一进来viewPager会显示在某个位置，这里提供一个方法实现初始位置，避免接口的冗余调用
     */
    protected open fun provideTabInitSelectPosition(): Int = 0

    /**
     * Tab选中的颜色
     */
    @ColorRes
    protected open fun provideTabSelectColor(): Int = R.color.color_333333

    /**
     * Tab未选中的颜色
     */
    @ColorRes
    protected open fun provideTabUnSelectColor(): Int = R.color.color_666666

    /**
     * 设置Tab行为模式
     */
    @Mode
    protected open var provideTabMode = MODE_FIXED


    override fun init(savedInstanceState: Bundle?) {
        mViewBind.apply {
            toobarLayout.apply {
                setTitleContent(provideTitleContent())
                setRightTitleContent(provideRightTitleContent())
                setRightTitleVisibility(provideRightTitleVisibility())
                setRightIconVisibility(provideRightIconVisibility())
                setRightIconRes(provideRightIconRes())
                onRightIconClickListener = {
                    provideRightIconClickListener()
                }
                onRightClickListener = {
                    provideRightClickListener()
                }
            }
            searchLayout.isVisible = provideSearchLayoutVisibility()
            searchLayout.setSearchHintContent(provideSearchHintContent())
            clContent.setBackgroundColor(
                Color.TRANSPARENT
            )
            viewPager.setCanScroll(provideViewPagerCanScroll())
            clTab.isVisible = fragments.size > 1
            tabLayout.apply {
                tabMode = provideTabMode
                setupWithViewPager(viewPager.apply {
                    adapter = SimpleFragmentPagerAdapter(
                        supportFragmentManager, fragments
                    )
                    offscreenPageLimit = fragments.size
                    currentItem = provideTabInitSelectPosition()
                })
                post {
                    //通过源码，重设的位置与当前位置一样，那么会重调onTabReselected方法，而我们的onTabReselected的没有调用任何代码，所以相当于只执行一个初始选中动画
                    getTabAt(provideTabInitSelectPosition())?.select()
                }
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        updateTabView(tab, true)
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        updateTabView(tab, false)
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                })
                viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(
                        position: Int, positionOffset: Float, positionOffsetPixels: Int
                    ) {
                    }

                    override fun onPageSelected(position: Int) {
                        startSearch()
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                    }

                })
                repeat(tabCount) {
                    getTabAt(it)?.customView =
                        ItemMultipleTabCenterTextBinding.inflate(layoutInflater).apply {
                            tvTabText.text = fragments[it].getTitle()
                        }.root.apply {
                            post {
                                if (provideTabMode == MODE_FIXED && fragments.size > 1) {
                                    updateLayoutParams {
                                        width = getScreenWidth() / fragments.size
                                    }
                                }
                                if (provideTabMode == TabLayout.MODE_SCROLLABLE && fragments.size > 1) {
                                    updatePadding(provideTabPadding(), 0, provideTabPadding(), 0)
                                }
                            }
                        }
                }
            }
            searchLayout.apply {
                onSearchClickListener = {
                    startSearch()
                }
                onValueChangeListener = {
                    fragments.forEach { fragment ->
                        fragment.setSearchKey(it)
                    }
                }
            }
        }
    }

    fun startSearch() {
        fragments[mViewBind.viewPager.currentItem].startSearch()
    }

    private fun updateTabView(tab: TabLayout.Tab?, isSelect: Boolean, listCount: Int? = null) {
        tab?.customView?.apply {
            val tvTabText = findViewById<TextView>(R.id.tvTabText)
            val tvTabCount = findViewById<TextView>(R.id.tvTabCount)
            tvTabText.setTextColor(
                ContextCompat.getColor(
                    context, if (isSelect) provideTabSelectColor() else provideTabUnSelectColor()
                )
            )
            tvTabText.setTextSize(TypedValue.COMPLEX_UNIT_PX, dpToPx(if (isSelect) 16f else 15f))
            if (provideTabHasCount()) {
                tvTabCount.isVisible = true
                if (listCount != null && isSelect) {
                    tvTabCount.text = "$listCount"
                }
            } else {
                tvTabCount.isInvisible = true
            }
        }
    }

    /**
     * 更新当前Tab
     */
    fun updateCurrentTabView(position: Int, listCount: Int? = 0) {
        updateTabView(
            mViewBind.tabLayout.getTabAt(position),
            position == mViewBind.viewPager.currentItem,
            listCount
        )
    }

    /**
     * 更新Tab数量
     */
    fun updateTabCount(position: Int, listCount: Int? = 0) {
        mViewBind.tabLayout.getTabAt(position)?.customView?.apply {
            val tvTabCount = findViewById<TextView>(R.id.tvTabCount)
            if (provideTabHasCount()) {
                tvTabCount.isVisible = true
                if (listCount != null) {
                    tvTabCount.text = "$listCount"
                }
            } else {
                tvTabCount.isInvisible = true
            }
        }
    }

    /**
     * 更新Tab标题
     */
    fun updateTabTitle(position: Int, title: String? = "") {
        mViewBind.tabLayout.getTabAt(position)?.customView?.apply {
            val tvTabText = findViewById<TextView>(R.id.tvTabText)
            tvTabText.text = title
        }
    }


}