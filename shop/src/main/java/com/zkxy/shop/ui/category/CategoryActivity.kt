package com.zkxy.shop.ui.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gxy.common.base.BaseViewBindActivity
import com.gxy.common.common.loadsir.getLoadSir
import com.gxy.common.common.loadsir.setLoadContentStatus
import com.kingja.loadsir.core.LoadService
import com.zkxy.shop.databinding.ActivityCategoryBinding
import com.zkxy.shop.ui.category.adapter.CategoryPrimaryAdapter
import com.zkxy.shop.ui.category.adapter.CategorySecondaryAdapter
import com.zkxy.shop.ui.search.SearchActivity

/**
 * @author zhangyuxiang
 * @date 2024/9/24
 */
class CategoryActivity : BaseViewBindActivity<CategoryViewModel, ActivityCategoryBinding>() {

    private lateinit var mLoadService: LoadService<Any>
    private lateinit var categoryPrimaryAdapter: CategoryPrimaryAdapter
    private lateinit var categorySecondaryAdapter: CategorySecondaryAdapter

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, CategoryActivity::class.java))
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        mViewBind.apply {
            mLoadService = getLoadSir().register(clCategory) {
                mViewModel.fetchCategory()
            }
            categoryPrimaryAdapter = CategoryPrimaryAdapter().apply {
                onCategoryPrimaryClickListener = {
                    mViewModel.resetCategorySelect(it)
                }
                rvPrimary.adapter = this
            }
            categorySecondaryAdapter = CategorySecondaryAdapter().apply {
                onCategorySecondaryClickListener = {

                }
                onCategoryMinorClickListener = {

                }
                rvSecondary.adapter = this
            }
            toobarLayout.onRightIconClickListener = {
                SearchActivity.startActivity(context = this@CategoryActivity)
            }
        }
        mViewModel.fetchCategory()
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.apply {
            loadContentStatus.observe(this@CategoryActivity) {
                mLoadService.setLoadContentStatus(it)
            }
            categoryDataList.observe(this@CategoryActivity) { categoryEntities ->
                categoryPrimaryAdapter.setList(categoryEntities)
                categorySecondaryAdapter.setList(categoryEntities.find { it.isSelect == true }?.categorySecondaryList)
                mViewBind.rvSecondary.scrollToPosition(0)
            }
        }
    }
}