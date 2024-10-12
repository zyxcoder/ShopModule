package com.gxy.common.entity.common

import android.annotation.SuppressLint
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.gxy.common.R
import com.gxy.common.common.providers.inter.ServerKeyInner

/**
 * @author zhangyuxiang
 * @date 2024/4/10
 */

/**
#type=-101 隐藏参数
#type=0 （iOS自定义，可输入字符）
#type=101 普通输入框
#type=102 普通输入框禁用
#type=103 普通输入框（iOS自定义，可输入字符）
#type=201 普通下拉框
#type=210 模糊搜索下拉框
#type=230 特殊下拉框写死值
#type=501 日期框 年月日
#type=503 日期框 年月日时分秒
#type=852 文件上传
#type=853 图片上传
 */
enum class InputLayoutType {
    DEFAULT,//普通无单位
    DEFAULT_UNIT,//普通带单位
    DEFAULT_DIALOG,//底部弹窗
    DIALOG_DATE_YMD,//日期弹窗，年月日
    DIALOG_DATE_YMDHMS//日期弹窗,年月日时分秒
}

@Keep
data class InputItemEntity(
    val lableName: String?,
    val isRequireds: Boolean? = false,
    val isCanEdit: Boolean? = true,
    val isCanInput: Boolean? = true,
    val unitContent: String? = null,
    val isShowSearchBox: Boolean? = false,
    val inputLayoutType: InputLayoutType,
    val textInputText: Int? = null,
    val postServerKey: String,
    var selectId: Int? = null,
    var selectContent: String? = null,
    val showBottomLine: Boolean? = true,
    val dialogListInfo: ArrayList<BottomListEntity>? = null,
    var result: Any? = null,//结果，初始化时如果有初始值，请将值附上
    val specialLableHint: String? = null,
//    var onAfterChangeListener: ((text: String?) -> Unit)? = null,
    var onFocusChangeListener: ((isFocus: Boolean?, truckCode: String?) -> Unit)? = null,
    val dialogFromId: Int = Int.MIN_VALUE
) : ServerKeyInner {
    override fun getServerKey() = postServerKey
    override fun getServerValue() = result
    override fun hashCode(): Int {
        return getUUid().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InputItemEntity

        if (lableName != other.lableName) return false
        if (isRequireds != other.isRequireds) return false
        if (isCanEdit != other.isCanEdit) return false
        if (isCanInput != other.isCanInput) return false
        if (unitContent != other.unitContent) return false
        if (isShowSearchBox != other.isShowSearchBox) return false
        if (inputLayoutType != other.inputLayoutType) return false
        if (textInputText != other.textInputText) return false
        if (postServerKey != other.postServerKey) return false
        if (selectId != other.selectId) return false
        if (selectContent != other.selectContent) return false
        if (showBottomLine != other.showBottomLine) return false
        if (dialogListInfo != other.dialogListInfo) return false
        if (result != other.result) return false
        if (specialLableHint != other.specialLableHint) return false
        if (dialogFromId != other.dialogFromId) return false
        if (getUUid() != other.getUUid()) return false
        return true
    }
}

@Keep
data class InputPhoneItemEntity(
    val isRequireds: Boolean? = false,
    val isCanEdit: Boolean? = true,
    val hintContent: String? = null,
    val title: String? = null,
    val phone: String? = null,
    var result: Any? = null,//结果，初始化时如果有初始值，请将值附上
    val postServerKey: String,
) : ServerKeyInner {
    override fun getServerKey() = postServerKey
    override fun getServerValue() = result
    override fun hashCode(): Int {
        return getUUid().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InputPhoneItemEntity

        if (isRequireds != other.isRequireds) return false
        if (isCanEdit != other.isCanEdit) return false
        if (hintContent != other.hintContent) return false
        if (title != other.title) return false
        if (phone != other.phone) return false
        if (result != other.result) return false
        if (postServerKey != other.postServerKey) return false
        if (getUUid() != other.getUUid()) return false
        return true
    }
}

@Keep
data class CheckItemEntity(
    val lableName: String?,
    val isRequireds: Boolean? = false,
    var isCheck: Boolean? = false,
    val postServerKey: String
) : ServerKeyInner {
    override fun getServerKey() = postServerKey
    override fun getServerValue() = isCheck
    override fun hashCode(): Int {
        return getUUid().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CheckItemEntity

        if (lableName != other.lableName) return false
        if (isRequireds != other.isRequireds) return false
        if (isCheck != other.isCheck) return false
        if (postServerKey != other.postServerKey) return false
        if (getUUid() != other.getUUid()) return false
        return true
    }
}

@Keep
data class FileItemEntity(
    val lableName: String?,
    val hintContent: String? = null,
    val isRequireds: Boolean? = false,
    val postServerKey: String,
    var fileAddress: String? = null,
    val fileIsPic: Boolean? = true
) : ServerKeyInner {
    override fun getServerKey() = postServerKey
    override fun getServerValue() = fileAddress
    override fun hashCode(): Int {
        return getUUid().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileItemEntity

        if (lableName != other.lableName) return false
        if (hintContent != other.hintContent) return false
        if (isRequireds != other.isRequireds) return false
        if (postServerKey != other.postServerKey) return false
        if (fileAddress != other.fileAddress) return false
        if (fileIsPic != other.fileIsPic) return false
        if (getUUid() != other.getUUid()) return false
        return true
    }
}

@Keep
data class TitleItemEntity(
    val lableName: String?,
    val postServerKey: String? = null,
    var lableValue: String? = null,
    val isShowBottomLine: Boolean? = false
) : ServerKeyInner {
    override fun getServerKey() = postServerKey
    override fun getServerValue() = lableValue
    override fun hashCode(): Int {
        return getUUid().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TitleItemEntity

        if (lableName != other.lableName) return false
        if (postServerKey != other.postServerKey) return false
        if (lableValue != other.lableValue) return false
        if (isShowBottomLine != other.isShowBottomLine) return false
        if (getUUid() != other.getUUid()) return false
        return true
    }
}

@Keep
data class DialogSelectItemEntity(
    val lableName: String?,
    @DrawableRes val drawableRes: Int,
    val postServerKey: String,
    val isRequireds: Boolean? = true,
    var selectId: Int? = null,
    var selectContent: String? = null,
    val showEnter: Boolean? = true,
    val dialogListInfo: ArrayList<BottomListEntity>? = null,
    val marginTop: Int? = 0,
    val marginBottom: Int? = 0
) : ServerKeyInner {
    override fun getServerKey() = postServerKey
    override fun getServerValue() = selectId
    override fun hashCode(): Int {
        return getUUid().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DialogSelectItemEntity

        if (lableName != other.lableName) return false
        if (drawableRes != other.drawableRes) return false
        if (postServerKey != other.postServerKey) return false
        if (isRequireds != other.isRequireds) return false
        if (selectId != other.selectId) return false
        if (selectContent != other.selectContent) return false
        if (showEnter != other.showEnter) return false
        if (dialogListInfo != other.dialogListInfo) return false
        if (marginTop != other.marginTop) return false
        if (marginBottom != other.marginBottom) return false
        if (getUUid() != other.getUUid()) return false
        return true
    }
}

@Keep
data class MoneyItemEntity(
    val lableName: String?,
    val postServerKey: String,
    var currentMoney: Double? = Double.MIN_VALUE,
    val isRequireds: Boolean? = false
) : ServerKeyInner {
    override fun getServerKey() = postServerKey
    override fun getServerValue() = if (currentMoney == Double.MIN_VALUE) {
        null
    } else {
        currentMoney
    }

    override fun hashCode(): Int {
        return getUUid().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MoneyItemEntity

        if (lableName != other.lableName) return false
        if (postServerKey != other.postServerKey) return false
        if (currentMoney != other.currentMoney) return false
        if (isRequireds != other.isRequireds) return false
        if (getUUid() != other.getUUid()) return false
        return true
    }
}

@Keep
data class TxtItemEntity(
    val lableName: String?,
    var lableValue: String? = null,
    val postServerKey: String? = null,
    @DrawableRes val backgroundRes: Int? = R.drawable.round_solid_6d8df2_top_8
) : ServerKeyInner {
    override fun getServerKey() = postServerKey
    override fun getServerValue() = lableValue
    override fun hashCode(): Int {
        return getUUid().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TxtItemEntity

        if (lableName != other.lableName) return false
        if (lableValue != other.lableValue) return false
        if (postServerKey != other.postServerKey) return false
        if (backgroundRes != other.backgroundRes) return false
        if (getUUid() != other.getUUid()) return false
        return true
    }
}

@Keep
data class SelectTimeItemEntity(
    val lableName: String?,
    var startTime: String? = null,
    var endTime: String? = null,
    val postServerKey: String? = null,
    val isRequireds: Boolean? = false,
    val isShowBottomLine: Boolean? = false,
    val startTimePostServerKey: String? = null,
    val endTimePostServerKey: String? = null,
    @SuppressLint("ResourceAsColor") @ColorInt val selectColor: Int = R.color.color_333333
) : ServerKeyInner {
    override fun getServerKey() = postServerKey
    override fun getServerValue() = arrayListOf(startTime, endTime)
    override fun hashCode(): Int {
        return getUUid().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SelectTimeItemEntity

        if (lableName != other.lableName) return false
        if (startTime != other.startTime) return false
        if (endTime != other.endTime) return false
        if (postServerKey != other.postServerKey) return false
        if (isRequireds != other.isRequireds) return false
        if (isShowBottomLine != other.isShowBottomLine) return false
        if (startTimePostServerKey != other.startTimePostServerKey) return false
        if (endTimePostServerKey != other.endTimePostServerKey) return false
        if (selectColor != other.selectColor) return false
        if (getUUid() != other.getUUid()) return false
        return true
    }
}

@Keep
data class IconTitleItemEntity(
    val lableName: String?,
    var lableValue: String? = "",
    @DrawableRes val iconRes: Int? = R.drawable.ic_contract_num,
    val postServerKey: String? = null,
    val isShowBottomLine: Boolean = true,
) : ServerKeyInner {
    override fun getServerKey() = postServerKey
    override fun getServerValue() = lableValue
    override fun hashCode(): Int {
        return getUUid().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IconTitleItemEntity

        if (lableName != other.lableName) return false
        if (lableValue != other.lableValue) return false
        if (iconRes != other.iconRes) return false
        if (postServerKey != other.postServerKey) return false
        if (isShowBottomLine != other.isShowBottomLine) return false
        if (getUUid() != other.getUUid()) return false
        return true
    }
}

@Keep
data class CardIdItemEntity(
    val isRequireds: Boolean? = false,
    val postRightServerKey: String? = null,
    val postReverseServerKey: String? = null,
    var postRightServerValue: String? = null,
    var postReverseServerValue: String? = null,
    var rightCradImageUrl: String? = null,
    var reverseCradImageUrl: String? = null,
    var carDesc: String? = null,
    var cardRightSideDesc: String? = null,
    var cardReverseSideDesc: String? = null,
    var postCardRightSideDesc: String? = null,
    var postCardReverseSideDesc: String? = null,
    @DrawableRes var rightSidePlaceholder: Int? = R.drawable.ic_card_right_side,
    @DrawableRes var reverseSidePlaceholder: Int? = R.drawable.ic_card_reverse_side,
    @StringRes var pleasePostCardRightSide: Int = R.string.please_post_card_right_side,
    @StringRes var pleasePostCardReverseSide: Int = R.string.please_post_card_reverse_side,
    val marginTop: Int? = 0,
    val marginBottom: Int? = 0
) : ServerKeyInner {
    //特殊处理ServerKey
    override fun getServerKey() = ""

    override fun getServerValue() = arrayListOf(rightCradImageUrl, reverseCradImageUrl)
    override fun hashCode(): Int {
        return getUUid().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CardIdItemEntity

        if (isRequireds != other.isRequireds) return false
        if (postRightServerKey != other.postRightServerKey) return false
        if (postReverseServerKey != other.postReverseServerKey) return false
        if (postRightServerValue != other.postRightServerValue) return false
        if (postReverseServerValue != other.postReverseServerValue) return false
        if (rightCradImageUrl != other.rightCradImageUrl) return false
        if (reverseCradImageUrl != other.reverseCradImageUrl) return false
        if (carDesc != other.carDesc) return false
        if (cardRightSideDesc != other.cardRightSideDesc) return false
        if (cardReverseSideDesc != other.cardReverseSideDesc) return false
        if (postCardRightSideDesc != other.postCardRightSideDesc) return false
        if (postCardReverseSideDesc != other.postCardReverseSideDesc) return false
        if (rightSidePlaceholder != other.rightSidePlaceholder) return false
        if (reverseSidePlaceholder != other.reverseSidePlaceholder) return false
        if (marginTop != other.marginTop) return false
        if (marginBottom != other.marginBottom) return false
        if (getUUid() != other.getUUid()) return false
        return true
    }
}