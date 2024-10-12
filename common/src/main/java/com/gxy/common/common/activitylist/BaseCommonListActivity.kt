package com.gxy.common.common.activitylist

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.MODE_FIXED
import com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE
import com.google.android.material.tabs.TabLayout.Mode
import com.gxy.common.R
import com.gxy.common.base.BaseViewBindActivity
import com.gxy.common.common.adapter.SimpleFragmentPagerAdapter
import com.gxy.common.databinding.ActivityBaseCommonListBinding
import com.gxy.common.databinding.ItemTabCenterTextBinding
import com.gxy.common.utils.getScreenWidth
import com.zyxcoder.mvvmroot.utils.dpToPx

/**
 * @author zhangyuxiang
 * @date 2024/3/21
 * 带搜索的列表activity基类
 *
 * 由于MVVMBuild框架底层限制，第一个泛型必须是BaseViewModel，第二个必须是ViewBinding，虽然有点冗余，但前两个泛型必须写死，对于VB传入时就传ActivityBaseCommonListBinding即可
 */
abstract class BaseCommonListActivity<VM : BaseCommonListActivityViewModel, VB : ActivityBaseCommonListBinding, ItemVB : ViewBinding, ItemDataEntity> :
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
     * 设置下面的滑动Fragment
     */
    protected abstract fun provideFragments(): ArrayList<BaseCommonListFragment<out BaseCommonListFragmentViewModel<*>, *, *, *>>

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
     * 设置搜索框是否可见
     */
    protected open fun provideSearchVisibility(): Boolean = true

    /**
     * 右边按钮的点击事件
     */
    protected open fun provideRightClickListener() {}

    /**
     * ViewPager是否可滑动,有些Page的viewPager不可以滑动，比如物料Page，需要手动控制
     */
    protected open fun provideViewPagerCanScroll(): Boolean = true

    /**
     * Tab是否需要显示数字
     */
    protected open var provideTabHasCount: Boolean = false

    /**
     * 标题栏下的类型选择是否需要显示
     */
    protected open var provideSelectTypeIsVisibility: Boolean = false

    /**
     * 标题栏左右边距
     */
    protected open var provideTabPadding: Int = dpToPx(20f).toInt()

    /**
     * 设置Tab行为模式
     */
    @Mode
    protected open var provideTabMode = MODE_FIXED


    override fun init(savedInstanceState: Bundle?) {
        mViewBind.apply {
            viewSelectType.clRoot.isVisible = provideSelectTypeIsVisibility
            viewSelectType.tvClient.onChooseChangeListener = {
                startSearch()
            }
            viewSelectType.tvMateriel.onChooseChangeListener = {
                startSearch()
            }
            viewSelectType.tvFactory.onChooseChangeListener = {
                startSearch()
            }
            toobarLayout.apply {
                setTitleContent(provideTitleContent())
                setRightTitleContent(provideRightTitleContent())
                setRightTitleVisibility(provideRightTitleVisibility())
                onRightClickListener = {
                    provideRightClickListener()
                }
            }
            searchLayout.setSearchHintContent(provideSearchHintContent())
            searchLayout.isVisible = provideSearchVisibility()

            if (fragments.size > 1) {
                clContent.setBackgroundResource(R.drawable.ic_contract_list_bg)
            } else {
                clContent.setBackgroundColor(
                    Color.TRANSPARENT
                )
            }
            viewPager.setCanScroll(provideViewPagerCanScroll())
            clTab.isVisible = fragments.size > 1
            tabLayout.apply {
                tabMode = provideTabMode
                setupWithViewPager(viewPager.apply {
                    adapter = SimpleFragmentPagerAdapter(
                        supportFragmentManager, fragments
                    )
                    offscreenPageLimit = fragments.size
                })
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
                        ItemTabCenterTextBinding.inflate(layoutInflater).apply {
                            tvTabText.text = fragments[it].getTitle()
                            tvTabTextBig.text = fragments[it].getTitle()
                        }.root.apply {
                            post {
                                if (provideTabMode == MODE_FIXED && fragments.size > 1) {
                                    updateLayoutParams {
                                        width = getScreenWidth() / fragments.size
                                    }
                                }

                                if (provideTabMode == MODE_SCROLLABLE && fragments.size > 1) {
                                    setPadding(provideTabPadding, 0, provideTabPadding, 0)
                                }
                            }
                        }
                }
                updateTabView(getTabAt(0), true)
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

    protected open fun startSearch() {
        fragments[mViewBind.viewPager.currentItem].startSearch()
    }

    private fun updateTabView(tab: TabLayout.Tab?, isSelect: Boolean, listCount: Int? = null) {
        tab?.customView?.apply {
            val tvTabText = findViewById<TextView>(R.id.tvTabText)
            val tvTabTextBig = findViewById<TextView>(R.id.tvTabTextBig)
            val tvTabCount = findViewById<TextView>(R.id.tvTabCount)
            val tvTabCountBig = findViewById<TextView>(R.id.tvTabCountBig)
            tvTabTextBig.isInvisible = !isSelect
            tvTabCountBig.isVisible = isSelect && provideTabHasCount
            tvTabText.isInvisible = isSelect
            tvTabCount.isVisible = !isSelect && provideTabHasCount
            if (listCount != null) {
                tvTabCount.text = "$listCount"
                tvTabCountBig.text = "$listCount"
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
     * 用于获取选择弹窗内部选择内容,由于不知道实现类的参数名，所以这个方法需要实现类自己去实现搜索时传参功能
     */
    fun getSelectTypeParams(): Array<out Any> {
        return mViewBind.viewSelectType.let {
            arrayOf(
                it.tvClient.getContentTag() ?: -1,
                it.tvMateriel.getContentTag() ?: -1,
                it.tvFactory.getContentTag() ?: -1
            )
        }
    }
}