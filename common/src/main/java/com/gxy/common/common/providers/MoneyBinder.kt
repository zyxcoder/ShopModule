package com.gxy.common.common.providers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.binder.QuickViewBindingItemBinder
import com.gxy.common.R
import com.gxy.common.common.adapter.GroupTableAdapter
import com.gxy.common.databinding.ViewTypeMoneyBinding
import com.gxy.common.entity.common.MoneyItemEntity
import com.zyxcoder.mvvmroot.ext.getString

/**
 * @author zhangyuxiang
 * @date 2024/4/11
 */
class MoneyBinder : QuickViewBindingItemBinder<MoneyItemEntity, ViewTypeMoneyBinding>() {
    override fun convert(holder: BinderVBHolder<ViewTypeMoneyBinding>, data: MoneyItemEntity) {
        holder.viewBinding.moneyLayout.apply {
            initMoneyLayout(
                title = data.lableName,
                hintContent = getString(R.string.please_input) + "${data.lableName}",
                isRequireds = data.isRequireds ?: true,
                money = data.currentMoney ?: Double.MIN_VALUE
            )
            onInputChangeListener = { money ->
                ((adapter.recyclerView.parent.parent as? RecyclerView)?.adapter as? GroupTableAdapter)?.data?.firstOrNull {
                    it.tables.contains(data)
                }?.tables?.filterIsInstance<MoneyItemEntity>()?.findLast {
                    it == data
                }?.currentMoney = money
            }
        }
    }

    override fun onCreateViewBinding(
        layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int
    ): ViewTypeMoneyBinding = ViewTypeMoneyBinding.inflate(layoutInflater, parent, false)
}