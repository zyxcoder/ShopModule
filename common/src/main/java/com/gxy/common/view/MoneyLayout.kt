package com.gxy.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.gxy.common.R
import com.gxy.common.databinding.LayoutMoneyBinding
import com.gxy.common.utils.NumberUtil
import com.zyxcoder.mvvmroot.ext.showToast
import java.math.BigDecimal

/**
 * @author zhangyuxiang
 * @date 2024/4/8
 */
class MoneyLayout(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var mBinding: LayoutMoneyBinding
    private var hintContent: String? = null
    var onInputChangeListener: ((money: Double?) -> Unit)? = null

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.MoneyLayout)
        mBinding = LayoutMoneyBinding.inflate(LayoutInflater.from(context), this, true)
        mBinding.apply {
            tvTitle.text = attr.getString(R.styleable.MoneyLayout_moneyLayoutTitle)
            hintContent = attr.getString(R.styleable.MoneyLayout_moneyLayoutHint)
            tvStar.isVisible = attr.getBoolean(R.styleable.MoneyLayout_moneyLayoutIsRequireds, true)
            etAmount.hint = hintContent
            val money = attr.getFloat(R.styleable.MoneyLayout_moneyLayoutContent, Float.MIN_VALUE)
            if (money == Float.MIN_VALUE) {
                etAmount.text = null
                etAmountDesc.text = null
                onInputChangeListener?.invoke(null)
            } else {
                etAmount.setText("$money")
                etAmountDesc.setText(
                    NumberUtil.bigDecimal2chineseNum(
                        BigDecimal.valueOf(
                            money.toString().toDouble()
                        )
                    )
                )
                onInputChangeListener?.invoke(money.toString().toDouble())
            }
            etAmount.doAfterTextChanged {
                val input = it.toString()
                if (input.isNotEmpty() && !input.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                    it?.delete(it.length - 1, it.length)
                }
                if (it.toString().isEmpty()) {
                    etAmountDesc.text = null
                    onInputChangeListener?.invoke(null)
                    return@doAfterTextChanged
                }
                etAmountDesc.setText(
                    NumberUtil.bigDecimal2chineseNum(
                        it.toString().toBigDecimalOrNull() ?: BigDecimal(0)
                    )
                )
                onInputChangeListener?.invoke(it.toString().toDoubleOrNull())
            }
        }
        attr.recycle()
    }

    /**
     * 初始化MoneyLayout，主要用于Table表单
     */
    fun initMoneyLayout(
        title: String?, hintContent: String?, money: Double = 0.0, isRequireds: Boolean?
    ) {
        mBinding.apply {
            post {
                tvTitle.text = title
                tvStar.isVisible = isRequireds ?: true
                etAmount.hint = hintContent
                if (money == Double.MIN_VALUE) {
                    etAmount.text = null
                    etAmountDesc.text = null
                    onInputChangeListener?.invoke(null)
                } else {
                    etAmount.setText("$money")
                    etAmountDesc.setText(
                        NumberUtil.bigDecimal2chineseNum(
                            BigDecimal.valueOf(
                                money
                            )
                        )
                    )
                    onInputChangeListener?.invoke(money)
                }
            }
        }
    }

    fun getMoney(): Double? {
        return mBinding.etAmount.text.toString().toDoubleOrNull()
    }

    fun setMoney(money: Double) {
        mBinding.etAmount.setText("$money")
        mBinding.etAmountDesc.setText(NumberUtil.bigDecimal2chineseNum(BigDecimal.valueOf(money)))
    }

    /**
     * 判断是否为空，并且为空弹toast
     */
    fun contentIsEmptyAndShowToast(): Boolean {
        return mBinding.run {
            if (etAmount.text.toString().isEmpty()) {
                hintContent?.also { hintContent -> context.showToast(hintContent) }
            }
            etAmount.text.toString().isEmpty()
        }
    }


}