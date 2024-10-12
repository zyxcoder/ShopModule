package com.gxy.common.ext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.bigkoo.pickerview.builder.TimePickerBuilder
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Calendar
import java.util.Date

/**
 * @author zhangyuxiang
 * @date 2024/3/12
 */

/**
 * 展示年月日时分秒的时间选择器
 * @param onDateSelectListener 时间选择回调
 */
fun Context.showAllTimePicker(
    viewGroup: ViewGroup? = null,
    onDateSelectListener: ((date: Date?) -> Unit)?
) {
    TimePickerBuilder(
        this
    ) { date, _ -> onDateSelectListener?.invoke(date) }.setType(
        booleanArrayOf(
            true, true, true, true, true, true
        )
    )// 默认全部显示
        .setCancelText("取消")//取消按钮文字
        .setSubmitText("确定")//确认按钮文字
        .setTitleSize(20)//标题文字大小
        .setTitleText("请选择时间")//标题文字
        .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
        .isCyclic(false)//是否循环滚动
        .setTitleColor(Color.BLACK)//标题文字颜色
        .setSubmitColor(Color.BLUE)//确定按钮文字颜色
        .setCancelColor(Color.BLUE)//取消按钮文字颜色
        .setDate(Calendar.getInstance())// 如果不设置的话，默认是系统时间*/
        .setRangDate(Calendar.getInstance().apply {
            set(1900, 1, 1)
        }, Calendar.getInstance().apply {
            set(2100, 12, 31)
        })//起始终止年月日设定
        .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
        .isDialog(false)//是否显示为对话框样式
        .setDecorView(viewGroup)
        .build().apply {
            setKeyBackCancelable(false)
        }.show()
}


/**
 * 展示年月日的时间选择器
 * @param onDateSelectListener 时间选择回调
 */
fun Context.showDefaultTimePicker(
    viewGroup: ViewGroup? = null,
    onDateSelectListener: ((date: Date?) -> Unit)?
) {
    TimePickerBuilder(
        this
    ) { date, _ -> onDateSelectListener?.invoke(date) }.setType(
        booleanArrayOf(
            true, true, true, false, false, false
        )
    )// 默认全部显示
        .setCancelText("取消")//取消按钮文字
        .setSubmitText("确定")//确认按钮文字
        .setTitleSize(20)//标题文字大小
        .setTitleText("请选择时间")//标题文字
        .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
        .isCyclic(false)//是否循环滚动
        .setTitleColor(Color.BLACK)//标题文字颜色
        .setSubmitColor(Color.BLUE)//确定按钮文字颜色
        .setCancelColor(Color.BLUE)//取消按钮文字颜色
        .setDate(Calendar.getInstance())// 如果不设置的话，默认是系统时间*/
        .setRangDate(Calendar.getInstance().apply {
            set(1900, 1, 1)
        }, Calendar.getInstance().apply {
            set(2100, 12, 31)
        })//起始终止年月日设定
        .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
        .isDialog(false)//是否显示为对话框样式
        .setDecorView(viewGroup)
        .build().apply {
            setKeyBackCancelable(false)
        }.show()
}

//展示年月日时分的时间选择器
fun Context.showTimePicker(
    viewGroup: ViewGroup? = null,
    onDateSelectListener: ((date: Date?) -> Unit)?
) {
    TimePickerBuilder(
        this
    ) { date, _ -> onDateSelectListener?.invoke(date) }.setType(
        booleanArrayOf(
            true, true, true, true, true, false
        )
    )// 默认全部显示
        .setCancelText("取消")//取消按钮文字
        .setSubmitText("确定")//确认按钮文字
        .setTitleSize(20)//标题文字大小
        .setTitleText("请选择时间")//标题文字
        .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
        .isCyclic(false)//是否循环滚动
        .setTitleColor(Color.BLACK)//标题文字颜色
        .setSubmitColor(Color.BLUE)//确定按钮文字颜色
        .setCancelColor(Color.BLUE)//取消按钮文字颜色
        .setDate(Calendar.getInstance())// 如果不设置的话，默认是系统时间*/
        .setRangDate(Calendar.getInstance().apply {
            set(1900, 1, 1)
        }, Calendar.getInstance().apply {
            set(2100, 12, 31)
        })//起始终止年月日设定
        .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
        .isDialog(false)//是否显示为对话框样式
        .setDecorView(viewGroup)
        .build().apply {
            setKeyBackCancelable(false)
        }.show()
}

/**
 * 复制文字到剪切板
 * @param text 复制的文字
 */
fun Context.copyText(text: String) {
    (getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.setPrimaryClip(
        ClipData.newPlainText("text", text)
    )
}

/**
 * 创建pdf临时文件
 */
fun Context.createTempFileFromInputStream(inputStream: InputStream?): File? {
    var tempFile: File? = null
    try {
        tempFile = File.createTempFile("temp", ".pdf", cacheDir)
        val outputStream = FileOutputStream(tempFile)
        val buffer = ByteArray(8 * 1024)
        var bytesRead: Int
        while ((inputStream!!.read(buffer).also { bytesRead = it }) != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return tempFile
}
