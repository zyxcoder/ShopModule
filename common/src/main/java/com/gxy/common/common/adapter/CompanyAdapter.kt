package com.gxy.common.common.adapter

import com.gxy.common.R
import com.gxy.common.databinding.ItemCompanyBinding
import com.gxy.common.entity.common.CompanyEntity
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingAdapter
import com.zyxcoder.mvvmroot.base.adapter.BaseViewBindingHolder

/**
 * @author zhangyuxiang
 * @date 2024/4/1
 */
class CompanyAdapter : BaseViewBindingAdapter<CompanyEntity, ItemCompanyBinding>(
    ItemCompanyBinding::inflate, R.layout.item_company
) {
    var onCompanyClickListener: ((item: CompanyEntity) -> Unit)? = null
    override fun convert(holder: BaseViewBindingHolder<ItemCompanyBinding>, item: CompanyEntity) {
        holder.viewBind.apply {
            tvCompany.text = item.companyName
            tvCompany.setOnClickListener {
                onCompanyClickListener?.invoke(item)
            }
        }
    }
}