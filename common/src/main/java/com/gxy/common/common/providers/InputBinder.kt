package com.gxy.common.common.providers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.binder.QuickViewBindingItemBinder
import com.gxy.common.R
import com.gxy.common.common.adapter.GroupTableAdapter
import com.gxy.common.databinding.ViewTypeInputBinding
import com.gxy.common.entity.common.InputItemEntity
import com.gxy.common.entity.common.InputLayoutType
import com.gxy.common.ext.DateFormatType
import com.gxy.common.ext.dateToString
import com.gxy.common.ext.showAllTimePicker
import com.gxy.common.ext.showDefaultTimePicker
import com.zyxcoder.mvvmroot.ext.onContinuousClick


/**
 * @author zhangyuxiang
 * @date 2024/4/10
 * 输入框模版
 */
class InputBinder : QuickViewBindingItemBinder<InputItemEntity, ViewTypeInputBinding>() {
    override fun convert(holder: BinderVBHolder<ViewTypeInputBinding>, data: InputItemEntity) {
        holder.viewBinding.apply {
            inputData.apply {
                onInputChangeListener = {
                    ((adapter.recyclerView.parent.parent as? RecyclerView)?.adapter as? GroupTableAdapter)?.data?.firstOrNull {
                        it.tables.contains(data)
                    }?.tables?.filterIsInstance<InputItemEntity>()?.findLast {
                        it == data
                    }?.apply {
                        result = if (getContentTag() != null) getContentTag() else {
                            if (getContent().isNullOrEmpty()) {
                                null
                            } else {
                                getContent()
                            }
                        }
//                        data.onAfterChangeListener?.invoke(getContent())
                        selectId = getContentTag()
                        selectContent = getContent()
                    }
                }
                onFocusChangeListener = {
                    data.onFocusChangeListener?.invoke(it, getContent())
                }
                when (data.inputLayoutType) {
                    InputLayoutType.DEFAULT, InputLayoutType.DEFAULT_UNIT -> {
                        initInputLayout(
                            title = data.lableName,
                            isRequireds = data.isRequireds ?: false,
                            isCanInput = data.isCanInput ?: true,
                            isShowEnter = false,
                            inputType = data.textInputText,
                            hintContent = context.getString(R.string.please_input) + "${data.lableName}",
                            selectContent = context.getString(R.string.please_select) + "${data.lableName}",
                            haseUnit = data.inputLayoutType == InputLayoutType.DEFAULT_UNIT,
                            unitContent = data.unitContent,
                            isNeedShowBottomDialog = false,
                            isShowSearchBox = false,
                            isCanEdit = data.isCanEdit ?: true,
                            isShowBottomLine = data.showBottomLine ?: true
                        )
                    }

                    InputLayoutType.DEFAULT_DIALOG -> {
                        initInputLayout(
                            title = data.lableName,
                            isRequireds = data.isRequireds ?: false,
                            isCanInput = false,
                            isShowEnter = true,
                            inputType = data.textInputText,
                            hintContent = context.getString(R.string.please_select) + "${data.lableName}",
                            selectContent = data.selectContent,
                            haseUnit = false,
                            unitContent = data.unitContent,
                            isNeedShowBottomDialog = true,
                            isShowSearchBox = data.isShowSearchBox ?: false,
                            isCanEdit = true,
                            isShowBottomLine = data.showBottomLine ?: true
                        )
                        setBottomDialogData(
                            dialogTitle = context.getString(R.string.please_select) + "${if (data.specialLableHint.isNullOrEmpty()) data.lableName else data.specialLableHint}",
                            dialogListInfo = data.dialogListInfo,
                            dialogFromId = data.dialogFromId,
                            dialogCheckId = data.selectId,
                            dialogNameId = data.postServerKey
                        )
                    }

                    InputLayoutType.DIALOG_DATE_YMD, InputLayoutType.DIALOG_DATE_YMDHMS -> {
                        initInputLayout(
                            title = data.lableName,
                            isRequireds = data.isRequireds ?: false,
                            isCanInput = false,
                            isShowEnter = true,
                            inputType = data.textInputText,
                            hintContent = context.getString(R.string.please_select) + "${data.lableName}",
                            selectContent = data.selectContent,
                            haseUnit = false,
                            unitContent = data.unitContent,
                            isNeedShowBottomDialog = false,
                            isShowSearchBox = false,
                            isCanEdit = true,
                            isShowBottomLine = data.showBottomLine ?: true
                        )
                        onContinuousClick {
                            if (data.inputLayoutType == InputLayoutType.DIALOG_DATE_YMD) {
                                context.showDefaultTimePicker {
                                    inputData.setSelectText(
                                        content = it.dateToString(
                                            DateFormatType.TYPE_YMD
                                        )
                                    )
                                }
                            } else {
                                context.showAllTimePicker {
                                    inputData.setSelectText(
                                        content = it.dateToString(
                                            DateFormatType.TYPE_YMDHMS
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                if (data.isCanEdit == true) {
                    setSelectText(content = data.selectContent, tag = data.selectId)
                } else {
                    setMessageText(content = data.selectContent)
                }
            }
        }
    }

    override fun onCreateViewBinding(
        layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int
    ): ViewTypeInputBinding = ViewTypeInputBinding.inflate(layoutInflater, parent, false)

}