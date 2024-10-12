package com.gxy.common.common.table

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.text.parseAsHtml
import androidx.core.view.isVisible
import com.gxy.common.R
import com.gxy.common.base.BaseViewBindActivity
import com.gxy.common.common.adapter.GroupTableAdapter
import com.gxy.common.databinding.ActivityBaseTableBinding
import com.gxy.common.entity.common.CardIdItemEntity
import com.gxy.common.entity.common.CheckItemEntity
import com.gxy.common.entity.common.DialogSelectItemEntity
import com.gxy.common.entity.common.FileItemEntity
import com.gxy.common.entity.common.GroupTableEntity
import com.gxy.common.entity.common.InputItemEntity
import com.gxy.common.entity.common.InputLayoutType
import com.gxy.common.entity.common.MoneyItemEntity
import com.gxy.common.entity.common.SelectTimeItemEntity
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast


/**
 * @author zhangyuxiang
 * @date 2024/4/11
 */
abstract class BaseTableActivity<VM : BaseTableViewModel, VB : ActivityBaseTableBinding> :
    BaseViewBindActivity<VM, VB>() {
    protected lateinit var grouTableAdapter: GroupTableAdapter
    override fun init(savedInstanceState: Bundle?) {
        mViewBind.apply {
            toobarLayout.setTitleContent(provideTitleContent())
            tvStarTag.text = getString(R.string.numbered_fields_are_required).replace(
                "*", "<font color='#FF3636'>*</font>"
            ).parseAsHtml()
            tvStarTag.isVisible = provideStarTagIsVisibility
            spaceStarTag.isVisible = provideStarTagIsVisibility
            btSave.isVisible = provideSubmitIsBottom.not()
            btSaveBottom.isVisible = provideSubmitIsBottom
            btSave.text = provideSubmitContent()
            btSaveBottom.text = provideSubmitContent()
            grouTableAdapter = GroupTableAdapter().apply {
                onUploadFileClickListener = { ivHodler ->
                    provideFileUploadClickListener?.invoke(ivHodler)
                }
                onUpLoadReverseSidePic = { data, ivHodler ->
                    provideUpLoadReverseSidePicClickListener?.invoke(data, ivHodler)
                }
                onUpLoadRightSidePic = { data, ivHodler ->
                    provideUpLoadRightSidePicClickListener?.invoke(data, ivHodler)
                }
                rvTable.adapter = this
            }
            arrayListOf(btSave, btSaveBottom).forEach {
                it.onContinuousClick {
                    if (checkDataIsLegal()) {
                        handleSumbit()
                    }
                }
            }
        }
        mViewModel.fetchTableData(*provideInitParams())
    }

    /**
     * 处理表格提交事件
     */
    private fun handleSumbit() {
        val uploadMap = getAllParamsFromAdapter()
        Log.d("提交结果", uploadMap.toString())
        if (handleSubmitBySelf()) {
            handleSubmitOperation(uploadMap)
        } else {
            if (provideIsAddOrModify()) {
                mViewModel.insertUserData(uploadMap)
            } else {
                mViewModel.updateUserData(uploadMap)
            }
        }
    }

    /**
     * 设置标题文案
     */
    protected abstract fun provideTitleContent(): String

    /**
     * 初始化请求接口的参数
     */
    protected open fun provideInitParams(): Array<out Any> = arrayOf()

    /**
     * 这个页面是新增还是编辑页面
     * true表示新增，false表示编辑
     */
    protected abstract fun provideIsAddOrModify(): Boolean

    /**
     * 自己处理提交逻辑
     */
    protected abstract fun handleSubmitBySelf(): Boolean

    /**
     * 自己处理提交操作的回调方法
     */
    protected open fun handleSubmitOperation(map: Map<String?, Any?>) {}


    /**
     * 设置分组表格
     */
    protected open fun provideGroupTables(tables: ArrayList<GroupTableEntity>) {
        grouTableAdapter.setNewInstance(tables)
    }


    /**
     * 设置提交按钮是否在最底部悬浮
     */
    protected open var provideSubmitIsBottom: Boolean = false

    /**
     * 提交按钮的文案
     */
    protected open fun provideSubmitContent(): String = ""

    /**
     * 文件上传交由各自activity实现
     */
    protected open var provideFileUploadClickListener: ((ivHodler: ImageView) -> Unit)? = null

    /**
     * 驾驶证正面上传
     */
    protected open var provideUpLoadRightSidePicClickListener: ((data: CardIdItemEntity, ivHodler: ImageView) -> Unit)? =
        null

    /**
     * 驾驶证反面上传
     */
    protected open var provideUpLoadReverseSidePicClickListener: ((data: CardIdItemEntity, ivHodler: ImageView) -> Unit)? =
        null

    /**
     * 必填标志是否显示
     */
    protected open var provideStarTagIsVisibility: Boolean = true


    private fun checkDataIsLegal(): Boolean {
        repeat(grouTableAdapter.itemCount) { count ->
            val groupTableEntity = grouTableAdapter.getItemOrNull(count)
            repeat(groupTableEntity?.tables?.size ?: 0) { index ->
                if (groupTableEntity != null) {
                    when (val itemEntity = groupTableEntity.tables[index]) {
                        is CheckItemEntity -> {
                            if (itemEntity.isRequireds == true && itemEntity.getServerValue() == null) {
                                showToast(getString(R.string.please_select) + itemEntity.lableName)
                                return false
                            }
                        }

                        is DialogSelectItemEntity -> {
                            if (itemEntity.isRequireds == true && itemEntity.getServerValue() == null) {
                                showToast(getString(R.string.please_select) + itemEntity.lableName)
                                return false
                            }
                        }

                        is FileItemEntity -> {
                            if (itemEntity.isRequireds == true && itemEntity.getServerValue() == null) {
                                itemEntity.hintContent?.let {
                                    showToast(it)
                                    return false
                                }
                            }
                        }

                        is InputItemEntity -> {
                            if (itemEntity.isRequireds == true && itemEntity.getServerValue() == null) {
                                if (itemEntity.inputLayoutType == InputLayoutType.DEFAULT || itemEntity.inputLayoutType == InputLayoutType.DEFAULT_UNIT) {
                                    showToast(getString(R.string.please_input) + itemEntity.lableName)
                                } else {
                                    showToast(getString(R.string.please_select) + itemEntity.lableName)
                                }
                                return false
                            }
                        }

                        is MoneyItemEntity -> {
                            if (itemEntity.isRequireds == true && itemEntity.getServerValue() == null) {
                                showToast(getString(R.string.please_input) + itemEntity.lableName)
                                return false
                            }
                        }

                        is SelectTimeItemEntity -> {
                            if (itemEntity.isRequireds == true) {
                                if (itemEntity.startTime == null) {
                                    showToast(R.string.please_select_start_time)
                                    return false
                                }
                                if (itemEntity.endTime == null) {
                                    showToast(R.string.please_select_end_time)
                                    return false
                                }
                            }
                        }

                        is CardIdItemEntity -> {
                            if (itemEntity.isRequireds == true) {
                                if (itemEntity.rightCradImageUrl == null) {
                                    showToast(getString(itemEntity.pleasePostCardRightSide))
                                    return false
                                }
                                if (itemEntity.reverseCradImageUrl == null) {
                                    showToast(getString(itemEntity.pleasePostCardReverseSide))
                                    return false
                                }
                            }
                        }
                    }
                }
            }
        }
        return true
    }

    private fun getAllParamsFromAdapter(): Map<String?, Any?> {
        val uploadMap = mutableMapOf<String?, Any?>()
        repeat(grouTableAdapter.itemCount) {
            val groupTableEntity = grouTableAdapter.data.getOrNull(it)
            repeat(groupTableEntity?.tables?.size ?: 0) { index ->
                val serverKeyInner = groupTableEntity?.tables?.getOrNull(index)
                if (serverKeyInner is SelectTimeItemEntity) {
                    //选择时间特殊判断
                    uploadMap[serverKeyInner.startTimePostServerKey] = serverKeyInner.startTime
                    uploadMap[serverKeyInner.endTimePostServerKey] = serverKeyInner.endTime
                } else if (serverKeyInner is CardIdItemEntity) {
                    //上传驾驶证特殊判断
                    uploadMap[serverKeyInner.postRightServerKey] =
                        serverKeyInner.postRightServerValue
                    uploadMap[serverKeyInner.postReverseServerKey] =
                        serverKeyInner.postReverseServerValue
                } else {
                    uploadMap[serverKeyInner?.getServerKey()] = serverKeyInner?.getServerValue()
                }
            }
        }
        return uploadMap.filter { !it.key.isNullOrEmpty() }
    }

    override fun createObserver() {
        mViewModel.apply {
            updateSuccess.observe(this@BaseTableActivity) {
                if (it) {
                    setResult(RESULT_OK)
                    finish()
                }
            }
            insertSuccess.observe(this@BaseTableActivity) {
                if (it) {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }
}