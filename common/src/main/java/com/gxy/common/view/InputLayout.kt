package com.gxy.common.view

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentManager
import com.gxy.common.R
import com.gxy.common.base.BottomChooseDialogFragment
import com.gxy.common.databinding.ViewInputBinding
import com.gxy.common.entity.common.BottomListEntity
import com.gxy.common.view.bottomdialog.BottomListDialogFragment
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.ext.showToast


/**
 * @author zhangyuxiang
 * @date 2024/3/4
 */
class InputLayout(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private var dialogTitle: String? = null
    private var dialogListInfo: ArrayList<BottomListEntity>? = null
    private var mBinding: ViewInputBinding

    private var hintContent: String? = null

    private var contentTag: Int? = null

    private var dialogFromId: Int = -1
    private var dialogNameId: String? = null

    var onInputChangeListener: ((content: String?) -> Unit)? = null
    var onFocusChangeListener: ((isFocus: Boolean?) -> Unit)? = null

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.InputLayout)
        mBinding = ViewInputBinding.inflate(LayoutInflater.from(context), this, true)
        mBinding.apply {
            tvTitle.text = attr.getString(R.styleable.InputLayout_title)
            tvStar.isVisible = attr.getBoolean(R.styleable.InputLayout_is_requireds, false)
            val isCanInput = attr.getBoolean(R.styleable.InputLayout_is_input, false)
            val isShowEnter = attr.getBoolean(R.styleable.InputLayout_is_show_enter, true)
            val isShowBottomLine =
                attr.getBoolean(R.styleable.InputLayout_is_show_bottom_line, true)

            viewLine.isVisible = isShowBottomLine
            if (isShowEnter) {
                groupName.isVisible = isCanInput.not()
            } else {
                groupName.visibility = GONE
                tvMessage.visibility = VISIBLE
            }

            etName.isVisible = isCanInput
            etName.inputType =
                attr.getInt(R.styleable.InputLayout_android_inputType, InputType.TYPE_CLASS_TEXT)

            hintContent = attr.getString(R.styleable.InputLayout_input_hint)
            etName.hint = hintContent
            val selectContent = attr.getString(R.styleable.InputLayout_select_content)
            tvName.text = if (selectContent.isNullOrEmpty()) hintContent else selectContent
            tvName.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (selectContent.isNullOrEmpty()) R.color.color_BCBCBD else R.color.color_333333
                )
            )
            tvMessage.setTextColor(
                ContextCompat.getColor(
                    context, if (attr.getBoolean(
                            R.styleable.InputLayout_editTextCanEdit, true
                        )
                    ) R.color.color_333333 else R.color.color_black_999999
                )
            )
            tvUnit.isVisible = attr.getBoolean(R.styleable.InputLayout_hase_unit, false)
            tvUnit.text = attr.getString(R.styleable.InputLayout_unit_content)
            //默认需要展示底部弹窗
            val isNeedShowBottomDialog =
                attr.getBoolean(R.styleable.InputLayout_is_need_show_bottom_dialog, true)
            val isShowSearchBox = attr.getBoolean(R.styleable.InputLayout_show_search_box, false)

            if (!isCanInput && isNeedShowBottomDialog) {
                clRoot.onContinuousClick {
                    showBottomDialog(
                        (context as? AppCompatActivity)?.supportFragmentManager, isShowSearchBox
                    )
                }
            }

            //设置EditText能否编辑
            etName.isFocusable = attr.getBoolean(R.styleable.InputLayout_editTextCanEdit, true)
            etName.isFocusableInTouchMode =
                attr.getBoolean(R.styleable.InputLayout_editTextCanEdit, true)
            mBinding.etName.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (attr.getBoolean(
                            R.styleable.InputLayout_editTextCanEdit,
                            true
                        )
                    ) R.color.color_333333 else R.color.color_black_999999
                )
            )
            etName.doAfterTextChanged {
                onInputChangeListener?.invoke(it.toString())
            }
            etName.setOnFocusChangeListener { _, hasFocus ->
                onFocusChangeListener?.invoke(hasFocus)
            }
        }
        attr.recycle()
    }

    /**
     * 初始化布局，主要用于Table初始化
     * @param inputType 输入类型
     * @param isRequireds 是否必填
     * @param isCanInput 是否可输入
     * @param isShowEnter  是否展示进入图标
     * @param inputType 输入类型
     * @param hintContent 提示词
     * @param selectContent 选中的文案
     * @param haseUnit 是否有单位
     * @param unitContent 单位
     * @param isNeedShowBottomDialog 点击是否弹出底部弹窗
     * @param isShowSearchBox 底部弹窗是否需要展示搜索框
     * @param isCanEdit 是否可编辑
     */
    fun initInputLayout(
        title: String? = null,
        isRequireds: Boolean = false,
        isCanInput: Boolean = false,
        isShowEnter: Boolean = true,
        inputType: Int? = null,
        hintContent: String? = null,
        selectContent: String? = null,
        haseUnit: Boolean = false,
        unitContent: String? = null,
        isNeedShowBottomDialog: Boolean = true,
        isShowSearchBox: Boolean = false,
        isCanEdit: Boolean = true,
        isShowBottomLine: Boolean = true
    ) {
        mBinding.apply {
            post {
                tvTitle.text = title
                tvStar.isVisible = isRequireds
                viewLine.isVisible = isShowBottomLine
                if (isShowEnter) {
                    groupName.isVisible = isCanInput.not()
                } else {
                    groupName.visibility = GONE
                    tvMessage.visibility = VISIBLE
                }
                etName.isVisible = isCanInput
                etName.inputType = inputType ?: InputType.TYPE_CLASS_TEXT
                etName.hint = hintContent
                tvName.text = if (selectContent.isNullOrEmpty()) hintContent else selectContent
                tvName.setTextColor(
                    ContextCompat.getColor(
                        context,
                        if (selectContent.isNullOrEmpty()) R.color.color_BCBCBD else R.color.color_333333
                    )
                )
                tvMessage.setTextColor(
                    ContextCompat.getColor(
                        context, if (isCanEdit) R.color.color_333333 else R.color.color_black_999999
                    )
                )
                tvUnit.isVisible = haseUnit
                tvUnit.text = unitContent
                //默认需要展示底部弹窗

                if (!isCanInput && isNeedShowBottomDialog) {
                    clRoot.onContinuousClick {
                        showBottomDialog(
                            (context as? AppCompatActivity)?.supportFragmentManager, isShowSearchBox
                        )
                    }
                }
                //设置EditText能否编辑
                setEditTextCanEdit(isCanEdit)
            }
        }
    }

    /**
     * 设置标题名称
     */
    fun setTitle(title: String?) {
        mBinding.tvTitle.text = title
    }

    /**
     * 设置EditText是否可编辑
     */
    fun setEditTextCanEdit(isCanEdit: Boolean) {
        mBinding.etName.isFocusable = isCanEdit
        mBinding.etName.isFocusableInTouchMode = isCanEdit
        mBinding.etName.setTextColor(
            ContextCompat.getColor(
                context, if (isCanEdit) R.color.color_333333 else R.color.color_black_999999
            )
        )
        if (!isCanEdit) {
            mBinding.tvStar.isVisible = false
        }
    }

    fun setMessageText(content: String?) {
        if (!content.isNullOrEmpty()) mBinding.tvMessage.text = content
    }

    fun setSelectText(content: String?, tag: Int? = null) {
        contentTag = tag
        mBinding.tvName.text = if (content.isNullOrEmpty()) hintContent else content
        mBinding.tvName.setTextColor(
            ContextCompat.getColor(
                context, if (content.isNullOrEmpty()) R.color.color_BCBCBD else R.color.color_333333
            )
        )
        mBinding.etName.setText(content)
    }

    fun getContent(): String? {
        mBinding.apply {
            return if (groupName.isVisible) {
                if (tvName.text.toString() == hintContent) {
                    null
                } else {
                    tvName.text.toString()
                }
            } else if (etName.isVisible) {
                etName.text.toString()
            } else tvMessage.text.toString()
        }
    }

    fun getContentTag() = contentTag

    /**
     * 判断是否为空，并且为空弹toast
     */
    fun contentIsEmptyAndShowToast(): Boolean {
        mBinding.apply {
            return if (groupName.isVisible) {
                tvName.text.toString().let {
                    if (it == hintContent || it.isEmpty()) {
                        hintContent?.also { hintContent -> context.showToast(hintContent) }
                        return true
                    }
                    it.isEmpty()
                }
            } else {
                if (etName.text.toString().isEmpty()) {
                    hintContent?.also { hintContent -> context.showToast(hintContent) }
                }
                etName.text.toString().isEmpty()
            }
        }
    }

    fun setBottomDialogData(
        dialogTitle: String?,
        dialogListInfo: ArrayList<BottomListEntity>? = null,
        dialogFromId: Int = -1,
        dialogNameId: String = "",
        dialogCheckId: Int? = null
    ) {
        this.dialogTitle = dialogTitle
        this.dialogListInfo = dialogListInfo
        this.dialogFromId = dialogFromId
        this.dialogNameId = dialogNameId
        this.contentTag = dialogCheckId
    }

    private fun showBottomDialog(manager: FragmentManager?, isShowSearchBox: Boolean?) {
        if (dialogFromId > 0 && !dialogNameId.isNullOrEmpty()) {
            manager?.also {
                BottomChooseDialogFragment.newInstance(
                    dialogTitle = this.dialogTitle ?: "",
                    fromId = this.dialogFromId,
                    nameId = this.dialogNameId ?: "",
                    checkId = getContentTag() ?: Int.MIN_VALUE
                ).apply {
                    onChooseClickListener = { bottomListEntity ->
                        setSelectText(bottomListEntity.name, bottomListEntity.id)
                        onEntityChangeListener?.invoke(bottomListEntity.data)
                    }
                }.show(it)
            }
        } else {
            dialogListInfo?.find {
                it.id == contentTag
            }?.isCheck = true
            manager?.also {
                if (dialogListInfo.isNullOrEmpty()) {
                    return
                }
                BottomListDialogFragment.newInstance(
                    dialogTitle = this.dialogTitle,
                    dialogListInfo = this.dialogListInfo,
                    isShowSearchBox = isShowSearchBox
                ).apply {
                    onChooseClickListener = { bottomListEntity ->
                        if (bottomListEntity.id == SELECT_OTHER_ITEM) {
                            showBottomDialog(manager, isShowSearchBox)
                        } else {
                            setSelectText(bottomListEntity.name, bottomListEntity.id)
                        }
                        onEntityChangeListener?.invoke(bottomListEntity.data)
                        onEntityIdChangeListener?.invoke(bottomListEntity.id)
                    }
                }.show(it)
            }
        }
    }

    var onEntityChangeListener: ((content: Any?) -> Unit)? = null
    var onEntityIdChangeListener: ((content: Int?) -> Unit)? = null
}

const val SELECT_OTHER_ITEM = Int.MAX_VALUE