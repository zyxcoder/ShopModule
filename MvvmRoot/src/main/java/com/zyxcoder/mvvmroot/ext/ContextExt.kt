package com.zyxcoder.mvvmroot.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.provider.Settings
import android.view.Gravity
import androidx.annotation.StringRes
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.zyxcoder.mvvmroot.utils.ToastUtils

/**
 * Create by zyx_coder on 2022/11/18
 */
/**
 * showToast
 * @param stringRes toast string资源id
 * @param gravity toast位置
 */
@JvmOverloads
fun Context?.showToast(@StringRes stringRes: Int, gravity: Int = Gravity.CENTER) {
    showToast(
        try {
            this?.resources?.getString(stringRes) ?: "unknown toast string"
        } catch (e: Resources.NotFoundException) {
            "unknown resource string id"
        }, gravity
    )
}

/**
 * showToast
 * @param message toast字符串
 * @param gravity toast位置
 */
@JvmOverloads
fun Context?.showToast(message: String, gravity: Int = Gravity.CENTER) {
    ToastUtils.showMvvmToast(this, message, gravity)
}

/**
 * 请求权限
 * @param permissions 待请求的权限集合
 * @param onGranted 同意
 * @param onDenied 拒绝
 * @param onDeniedAndAlwaysNotRequest 拒绝且不再询问
 */
fun Context.requestPermissionsUseXXPermission(
    permissions: Array<String>,
    onGranted: ((grantPermissionList: MutableList<String>, allRequestPermissionGranted: Boolean) -> Unit)? = null,
    onDenied: ((deniedPermissionList: MutableList<String>) -> Unit)? = null,
    onDeniedAndAlwaysNotRequest: ((deniedPermissionList: MutableList<String>) -> Unit)? = null
) {
    XXPermissions.with(this)
        .permission(permissions)
        .request(object : OnPermissionCallback {
            override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                onGranted?.invoke(permissions, allGranted)
            }

            override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                if (doNotAskAgain) {
                    onDeniedAndAlwaysNotRequest?.invoke(permissions)
                } else {
                    onDenied?.invoke(permissions)
                }
            }
        })
}

/**
 * 此权限列表是否全部授权
 * @param permissions 权限列表
 * @return true 全部授权 false 非全部授权
 */
fun Context.hasPermissions(permissions: Array<String>): Boolean =
    XXPermissions.isGranted(this, permissions)

/**
 * 开启权限设置页
 */
fun Context.openPermissionSetting() {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            if (this !is Activity) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    } catch (e: Exception) {
        startActivity(Intent(Settings.ACTION_SETTINGS).apply {
            if (this !is Activity) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        })
    }
}