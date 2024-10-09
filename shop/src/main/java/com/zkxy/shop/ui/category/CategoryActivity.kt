package com.zkxy.shop.ui.category

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.gxy.common.base.BaseViewBindActivity
import com.gxy.common.common.loadsir.getLoadSir
import com.gxy.common.common.loadsir.setLoadContentStatus
import com.kingja.loadsir.core.LoadService
import com.zkxy.shop.databinding.ActivityCategoryBinding
import com.zkxy.shop.entity.goods.AllGoodsType
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

    //选中的商品类型：积分商品，现金商品
    private var currentAllGoodsType = AllGoodsType.AllGoodsPoint

    companion object {
        private const val ALL_GOODS_TYPE = "all_goods_type"//商品类型
        fun startActivity(context: Context, allGoodsType: AllGoodsType) {
            context.startActivity(Intent(context, CategoryActivity::class.java).apply {
                putExtra(ALL_GOODS_TYPE, allGoodsType)
            })
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        currentAllGoodsType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(ALL_GOODS_TYPE, AllGoodsType::class.java)
        } else {
            intent.getSerializableExtra(ALL_GOODS_TYPE) as? AllGoodsType
        } ?: AllGoodsType.AllGoodsPoint

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
                    CategoryLevelActivity.startActivity(
                        context = this@CategoryActivity,
                        categorySecondaryEntity = it,
                        allGoodsType = currentAllGoodsType
                    )
                }
                onCategoryMinorClickListener = { categorySecondaryEntity, categoryMinorEntity ->
                    CategoryLevelActivity.startActivity(
                        context = this@CategoryActivity,
                        categorySecondaryEntity = categorySecondaryEntity,
                        categoryMinorEntity = categoryMinorEntity,
                        allGoodsType = currentAllGoodsType
                    )
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
                categorySecondaryAdapter.setList(categoryEntities.find { it.isSelect == true }?.children)
                mViewBind.rvSecondary.scrollToPosition(0)
            }
        }
    }
}