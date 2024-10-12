package com.gxy.common.common.popup

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isInvisible
import com.gxy.common.R
import com.gxy.common.common.adapter.CompanyAdapter
import com.gxy.common.databinding.PopupCompanyBinding
import com.gxy.common.entity.common.CompanyEntity
import razerdp.basepopup.BasePopupWindow

/**
 * @author zhangyuxiang
 * @date 2024/3/29
 */
class CompanyPopup(
    context: Context,
    private val currentCompany: CompanyEntity,
    private val companyList: List<CompanyEntity>,
    private val targetView: View
) : BasePopupWindow(context) {

    private lateinit var adapter: CompanyAdapter
    var onSelectCompanyClickListener: ((item: CompanyEntity) -> Unit)? = null

    init {
        setContentView(R.layout.popup_company)
        showAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_scale_top_in)
        dismissAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_scale_top_out)
    }

    override fun onViewCreated(contentView: View) {
        PopupCompanyBinding.bind(contentView).apply {
            adapter = CompanyAdapter().apply {
                rvCompany.adapter = this
                onCompanyClickListener = {
                    onSelectCompanyClickListener?.invoke(it)
                }
                setNewInstance(companyList.toMutableList())
            }
            tvCompany.text = currentCompany.companyName
        }
    }

    fun showPopUp() {
        targetView.isInvisible = true
        showPopupWindow(0, targetView.y.toInt())
    }

    override fun dismiss() {
        super.dismiss()
        targetView.isInvisible = false
    }
}