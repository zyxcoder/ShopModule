package com.gxy.common.view.bottomdialog

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gxy.common.base.BaseBottomSheetDialogFragment
import com.gxy.common.databinding.DialogBottomSelectListBinding
import com.gxy.common.entity.common.BottomListEntity
import com.zyxcoder.mvvmroot.ext.put

/**
 * @author zhangyuxiang
 * @date 2024/3/11
 */
class BottomListDialogFragment : BaseBottomSheetDialogFragment<DialogBottomSelectListBinding>() {


    var onChooseClickListener: ((bottomListEntity: BottomListEntity) -> Unit)? = null
    private lateinit var adapter: BottomListAdapter

    companion object {
        private const val DIALOG_TITLE = "dialog_title"
        private const val IS_SHOW_SEARCH_BOX = "IS_SHOW_SEARCH_BOX"
        fun newInstance(
            dialogTitle: String?,
            dialogListInfo: ArrayList<BottomListEntity>?,
            isShowSearchBox: Boolean? = false
        ): BottomListDialogFragment {
            BottomEntityHelper.instance.setBottomEntityData(
                title = dialogTitle, data = dialogListInfo
            )
            return BottomListDialogFragment().apply {
                arguments = Bundle().apply {
                    put(DIALOG_TITLE, dialogTitle)
                    put(
                        IS_SHOW_SEARCH_BOX,
                        //超过20条数据自动展示搜索框
                        isShowSearchBox == true || ((dialogListInfo?.size ?: 0) >= 20)
                    )
                }
            }
        }
    }

    override fun initView() {
        mViewBind.apply {
            tvTitle.text = arguments?.getString(DIALOG_TITLE)
            searchLayout.isVisible = arguments?.getBoolean(IS_SHOW_SEARCH_BOX, false) ?: false
            searchLayout.onSearchClickListener = { searchContent ->
                BottomEntityHelper.instance.getBottomEntity(
                    title = arguments?.getString(
                        DIALOG_TITLE
                    )
                )?.let { datas ->
                    adapter.setNewInstance(datas.filter {
                        it.name?.contains(searchContent) ?: false
                    }.toMutableList())
                }
            }
            adapter = BottomListAdapter().apply {
                rvBottomList.adapter = this
                onClickListener = { bottomListEntity ->
                    val preSelectIndex = adapter.data.indexOfFirst {
                        it.isCheck
                    }
                    val currentSelectIndex = adapter.data.indexOf(bottomListEntity)
                    if (preSelectIndex != -1) {
                        adapter.data[preSelectIndex].isCheck = false
                        adapter.notifyItemChanged(preSelectIndex)
                    }
                    adapter.data[currentSelectIndex].isCheck = true
                    adapter.notifyItemChanged(currentSelectIndex)
                    onChooseClickListener?.invoke(adapter.data[currentSelectIndex])
                    dismiss()
                }
            }
        }
    }

    override fun initData() {
        mViewBind.apply {
            adapter.setNewInstance(
                BottomEntityHelper.instance.getBottomEntity(
                    title = arguments?.getString(
                        DIALOG_TITLE
                    )
                )
            )
            rvBottomList.post {
                val currentSelectIndex = adapter.data.indexOfFirst {
                    it.isCheck
                }
                if (currentSelectIndex != -1) {
                    (rvBottomList.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
                        currentSelectIndex, 0
                    )
                }
            }
        }
    }


    override fun resetDialogHeightPercent(): Float = 0.5f

    fun show(manager: FragmentManager) {
        super.show(manager, "BottomListDialogFragment")
    }
}