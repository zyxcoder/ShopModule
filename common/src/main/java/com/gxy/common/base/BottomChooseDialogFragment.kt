package com.gxy.common.base

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.gxy.common.common.loadsir.getLoadSir
import com.gxy.common.common.loadsir.setLoadContentStatus
import com.gxy.common.databinding.DialogBottomChooseListBinding
import com.gxy.common.entity.common.BottomListEntity
import com.gxy.common.entity.common.BottomRequestEntity
import com.gxy.common.entity.common.SearchVoList
import com.gxy.common.view.bottomdialog.BottomListAdapter
import com.kingja.loadsir.core.LoadService
import com.zyxcoder.mvvmroot.ext.put

/**
 * @author zhangyuxiang
 * @date 2024/4/24
 * 底部选择弹窗，先弹出，再请求数据
 */
class BottomChooseDialogFragment :
    BaseBottomVBDialogFragment<BottomChooseDialogViewModel, DialogBottomChooseListBinding>() {
    var onChooseClickListener: ((bottomListEntity: BottomListEntity) -> Unit)? = null
    private lateinit var adapter: BottomListAdapter
    private lateinit var mLoadService: LoadService<Any>
    private var checkId: Int? = null
    private val nameId by lazy {
        arguments?.getString(NAME_ID, "")
    }
    private val formId by lazy {
        arguments?.getInt(FROM_ID, -1)
    }
    private var searchContent: String? = null

    private var isFirstShowSearchLayout = true

    companion object {
        private const val DIALOG_TITLE = "dialog_title"
        private const val NAME_ID = "name_id"
        private const val CHECK_ID = "check_id"
        private const val FROM_ID = "from_id"

        /**
         * @param dialogTitle 弹窗的标题
         * @param fromId
         * @param nameId 需要取的字段名称
         * @param checkId 选中的id
         */
        fun newInstance(
            dialogTitle: String, fromId: Int, nameId: String, checkId: Int = Int.MIN_VALUE
        ): BottomChooseDialogFragment {
            return BottomChooseDialogFragment().apply {
                arguments = Bundle().apply {
                    put(DIALOG_TITLE, dialogTitle)
                    put(NAME_ID, nameId)
                    put(CHECK_ID, checkId)
                    put(FROM_ID, fromId)
                }
            }
        }
    }

    override fun initView() {
        checkId = arguments?.getInt(CHECK_ID, Int.MIN_VALUE)
        mViewBind.apply {
            mLoadService = getLoadSir().register(refreshLayout) {
                mViewModel.fetchChooseList(
                    isFirst = true,
                    isRefresh = false,
                    nameId = nameId,
                    checkId = checkId,
                    bottomRequestEntity = BottomRequestEntity(
                        searchVoList = arrayListOf(
                            SearchVoList(
                                formId = formId,
                                inputName = nameId,
                                inputValue = searchContent,
                                start = 0
                            )
                        )
                    )
                )
            }
            tvTitle.text = arguments?.getString(DIALOG_TITLE)
            searchLayout.isVisible = false
            searchLayout.onSearchClickListener = { it ->
                searchContent = it
                mViewModel.fetchChooseList(
                    isFirst = true,
                    isRefresh = false,
                    nameId = nameId,
                    checkId = checkId,
                    bottomRequestEntity = BottomRequestEntity(
                        searchVoList = arrayListOf(
                            SearchVoList(
                                formId = formId,
                                inputName = nameId,
                                inputValue = searchContent,
                                start = 0
                            )
                        )
                    )
                )
            }
            refreshLayout.apply {
                setOnLoadMoreListener {
                    mViewModel.fetchChooseList(
                        isFirst = false,
                        isRefresh = false,
                        nameId = nameId,
                        checkId = checkId,
                        bottomRequestEntity = BottomRequestEntity(
                            searchVoList = arrayListOf(
                                SearchVoList(
                                    formId = formId,
                                    inputName = nameId,
                                    inputValue = searchContent,
                                    start = adapter.data.size
                                )
                            )
                        )
                    )
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
                    checkId = adapter.data[currentSelectIndex].id
                    adapter.data[currentSelectIndex].isCheck = true
                    adapter.notifyItemChanged(currentSelectIndex)
                    onChooseClickListener?.invoke(adapter.data[currentSelectIndex])
                    dismiss()
                }
            }
        }
    }

    override fun initData() {
        mViewModel.fetchChooseList(
            isFirst = true,
            isRefresh = false,
            nameId = nameId,
            checkId = checkId,
            bottomRequestEntity = BottomRequestEntity(
                searchVoList = arrayListOf(
                    SearchVoList(
                        formId = formId, inputName = nameId, inputValue = searchContent, start = 0
                    )
                )
            )
        )
    }

    override fun createObserver() {
        mViewModel.apply {
            loadContentStatus.observe(this@BottomChooseDialogFragment) {
                mLoadService.setLoadContentStatus(it)
            }
            bottomCommonOptionData.observe(this@BottomChooseDialogFragment) {
                adapter.setNewInstance(it.toMutableList())
                //大于20条，展示搜索框
                if (isFirstShowSearchLayout) {
                    mViewBind.searchLayout.isVisible = adapter.data.size >= 20
                    isFirstShowSearchLayout = false
                }
            }
            bottomCommonOptionMoreData.observe(this@BottomChooseDialogFragment) {
                adapter.addData(it.toMutableList())
            }
            isRefreshing.observe(this@BottomChooseDialogFragment) {
                if (!it) {
                    mViewBind.refreshLayout.finishRefresh()
                }
            }
            isLoading.observe(this@BottomChooseDialogFragment) {
                if (!it) {
                    mViewBind.refreshLayout.finishLoadMore()
                }
            }
            dataHasMore.observe(this@BottomChooseDialogFragment) {
                mViewBind.refreshLayout.setNoMoreData(!it)
            }
        }
    }


    override fun resetDialogHeightPercent(): Float = 0.5f

    fun show(manager: FragmentManager) {
        super.show(manager, "BottomChooseDialogFragment")
    }
}