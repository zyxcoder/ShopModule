package com.zkxy.shop.test.adapter

import com.zkxy.shop.R
import com.zkxy.shop.databinding.ItemListTestBinding
import com.zkxy.shop.entity.TestItemData
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder

/**
 * @author zhangyuxiang
 * @date 2024/9/9
 */
class TestListAdapter : BaseViewBindingAdapter<TestItemData, ItemListTestBinding>(
    ItemListTestBinding::inflate, R.layout.item_list_test
) {
    override fun convert(holder: BaseViewBindingHolder<ItemListTestBinding>, item: TestItemData) {
        holder.viewBind.apply {
            tvName.text = item.name
        }
    }
}