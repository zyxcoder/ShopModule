package com.gxy.common.common.activitylist

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.gxy.common.base.BaseViewBindFragment
import com.gxy.common.common.loadsir.getLoadSir
import com.gxy.common.common.loadsir.setLoadContentStatus
import com.gxy.common.databinding.FragmentBaseMultipleListBinding
import com.kingja.loadsir.core.LoadService
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter

/**
 * @author zhangyuxiang
 * @date 2024/6/22
 */
abstract class BaseMultipleListFragment<VM : BaseMultipleListFragmentViewModel<ItemDataEntity>, VB : FragmentBaseMultipleListBinding, ItemVB : ViewBinding, ItemDataEntity>(
    private val title: String? = null
) : BaseViewBindFragment<VM, VB>() {

    /**
     * 返回列表页面需要刷新的intent
     */
    val intentActivityResultRefreshLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                startSearch()
            }
        }

    private lateinit var adapter: BaseViewBindingAdapter<ItemDataEntity, ItemVB>
    private lateinit var mLoadService: LoadService<Any>
    private var searchKey: String? = null


    /**
     * 当前Fragment在ViewPager中是第几个
     */
    var currentFragmentInViewPagerIndex: Int = 0

    /**
     * 获取请求参数，动态参数，由于是动态，设置和获取时请注意顺序
     */
    protected abstract fun provideRequestParams(): Array<out Any>

    /**
     * 列表adapter
     */
    protected abstract fun provideAdapter(): BaseViewBindingAdapter<ItemDataEntity, ItemVB>

    /**
     * Tab的数量是否需要根据列表数据刷新
     */
    protected open fun provideTabCountNeedRefreshByList(): Boolean = true

    /**
     * 获取标题
     */
    fun getTitle() = title

    /**
     * 设置搜索key
     */
    fun setSearchKey(inputSearchKey: String?) {
        searchKey = inputSearchKey
    }


    /**
     * 开始搜索
     */
    fun startSearch() {
        //如果之前已经加载过才需要重新刷新
        if (isLazyLoaded) {
            mViewModel.getList(
                isFirst = true,
                isRefresh = false,
                start = 0,
                searchKey = searchKey,
                *provideRequestParams()
            )
            onRefresh()
        }
    }

    override fun lazyLoadData() {
        mViewModel.getList(
            isFirst = true,
            isRefresh = false,
            start = 0,
            searchKey = searchKey,
            *provideRequestParams()
        )
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.apply {
            mLoadService = getLoadSir().register(refreshLayout) {
                mViewModel.getList(
                    isFirst = true,
                    isRefresh = false,
                    start = 0,
                    searchKey = searchKey,
                    *provideRequestParams()
                )
                onRefresh()
            }
            adapter = provideAdapter().apply {
                rvInfo.adapter = this
            }
            refreshLayout.apply {
                setOnRefreshListener {
                    mViewModel.getList(
                        isFirst = false,
                        isRefresh = true,
                        start = 0,
                        searchKey = searchKey,
                        *provideRequestParams()
                    )
                    onRefresh()
                }
                setOnLoadMoreListener {
                    mViewModel.getList(
                        isFirst = false,
                        isRefresh = false,
                        start = rvInfo.adapter?.itemCount ?: 0,
                        searchKey = searchKey,
                        *provideRequestParams()
                    )
                }
            }
        }
    }

   protected open fun onRefresh() {

    }

    override fun createObserver() {
        mViewModel.apply {
            loadContentStatus.observe(this@BaseMultipleListFragment) {
                mLoadService.setLoadContentStatus(it)
            }
            isRefreshing.observe(this@BaseMultipleListFragment) {
                if (!it) {
                    mViewBind.refreshLayout.finishRefresh()
                }
            }
            isLoading.observe(this@BaseMultipleListFragment) {
                if (!it) {
                    mViewBind.refreshLayout.finishLoadMore()
                }
            }
            firstDatas.observe(this@BaseMultipleListFragment) {
                adapter.setNewInstance(it)
                mViewBind.rvInfo.postDelayed({
                    mViewBind.rvInfo.scrollToPosition(0)
                }, 100)
            }
            moreDatas.observe(this@BaseMultipleListFragment) {
                adapter.addData(it)
            }
            dataHasMore.observe(this@BaseMultipleListFragment) {
                mViewBind.refreshLayout.setNoMoreData(!it)
            }
            if (provideTabCountNeedRefreshByList()) {
                listCount.observe(this@BaseMultipleListFragment) {
                    (activity as? BaseMultipleCommonListActivity<*, *, *, *>)?.updateCurrentTabView(
                        currentFragmentInViewPagerIndex, it
                    )
                }
            }
        }
    }
}