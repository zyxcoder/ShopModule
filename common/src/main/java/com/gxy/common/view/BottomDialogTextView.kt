package com.gxy.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.gxy.common.R
import com.gxy.common.databinding.LayoutBottomTextBinding
import com.gxy.common.entity.common.BottomListEntity
import com.gxy.common.view.bottomdialog.BottomListDialogFragment
import com.zyxcoder.mvvmroot.ext.showToast

/**
 * @author zhangyuxiang
 * @date 2024/3/27
 * 点击弹出底部弹窗选择的TextView
 */
class BottomDialogTextView(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var mBinding: LayoutBottomTextBinding
    private var dialogTitle: String? = null
    private var dialogListInfo: ArrayList<BottomListEntity>? = null
    private var contentTag: Int? = null

    var onChooseChangeListener: ((bottomListEntity: BottomListEntity) -> Unit)? = null


    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.BottomDialogTextView)
        mBinding = LayoutBottomTextBinding.inflate(LayoutInflater.from(context), this, true)
        mBinding.apply {
            tvTitle.gravity=Gravity.CENTER
            tvTitle.text =
                attr.getString(R.styleable.BottomDialogTextView_bottomDialogTextViewTitle)
            dialogTitle =
                attr.getString(R.styleable.BottomDialogTextView_bottomDialogTextViewDialogTitle)
            val isShowSearchBox =
                attr.getBoolean(
                    R.styleable.BottomDialogTextView_bottomDialogTextViewShowSearchBox,
                    false
                )
            tvTitle.setOnClickListener {
                if ((dialogListInfo?.size ?: 0) > 0) {
                    showBottomDialog(
                        (context as? AppCompatActivity)?.supportFragmentManager,
                        isShowSearchBox
                    )
                } else {
                    context.showToast(R.string.bottom_dialog_no_data)
                }
            }
        }
        attr.recycle()
    }

    fun setSelectText(content: String?, tag: Int? = null) {
        mBinding.tvTitle.text = content
        contentTag = tag
    }

    fun getContent(): String {
        return mBinding.tvTitle.text.toString()
    }

    fun getContentTag() = contentTag


    fun setBottomDialogTitle(dialogTitle: String?) {
        this.dialogTitle = dialogTitle
    }

    fun setBottomDialogData(
        dialogTitle: String?, dialogListInfo: ArrayList<BottomListEntity>?
    ) {
        this.dialogTitle = dialogTitle
        this.dialogListInfo = dialogListInfo
    }

    private fun showBottomDialog(manager: FragmentManager?, isShowSearchBox: Boolean?) {
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
                    setSelectText(bottomListEntity.name, bottomListEntity.id)
                    onChooseChangeListener?.invoke(bottomListEntity)
                }
            }.show(it)
        }
    }
}