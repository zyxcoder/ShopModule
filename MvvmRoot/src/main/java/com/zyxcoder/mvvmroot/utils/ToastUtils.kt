package com.zyxcoder.mvvmroot.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.zyxcoder.mvvmroot.R

/**
 * Create by zyx_coder on 2022/11/18
 * toast工具类
 */
object ToastUtils {
    private var lastToast: Toast? = null

    /**
     * toast提示
     * @param context ctx
     * @param message 提示的内容字符串
     * @param gravity toast位置
     */
    @SuppressLint("InflateParams")
    fun showMvvmToast(context: Context?, message: String, gravity: Int) {
        context ?: return
        if (context is Activity && context.isFinishing) {
            return
        }
        Handler(Looper.getMainLooper()).post {
            try {
                val currentToast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
                currentToast.setGravity(gravity, 0, 0)
                val toastLayout =
                    (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                        .inflate(R.layout.layout_app_toast, null)
                toastLayout.apply {
                    findViewById<TextView>(R.id.tvToastContent).text = message
                }
                currentToast.view = toastLayout
                lastToast?.cancel()
                lastToast = currentToast
                currentToast.show()
            } catch (e: Exception) {
                //在某些系统的手机上弹自定义Toast有异常,如果出异常了,就用原生的Toast弹出方式
                lastToast = null
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}