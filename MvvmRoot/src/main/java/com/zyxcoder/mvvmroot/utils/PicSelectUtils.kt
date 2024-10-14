package com.zyxcoder.mvvmroot.utils

import android.Manifest
import androidx.fragment.app.FragmentActivity
import com.donkingliang.imageselector.utils.ImageSelector
import com.zyxcoder.mvvmroot.R
import com.zyxcoder.mvvmroot.ext.hasPermissions
import com.zyxcoder.mvvmroot.ext.requestPermissionsUseXXPermission
import com.zyxcoder.mvvmroot.ext.showToast

/**
 * Create by zyx_coder on 2023/2/28
 * 照片选择权限请求
 */
class PicSelectUtils {
    companion object {
        const val CHOOSE_PIC_TYPE_IS_CAMERA = 0X00001
        const val CHOOSE_PIC_TYPE_IS_GALLERY = 0X00010
        const val SELECT_PIC_REQUEST_CODE = 0x0001

        //检查权限
        fun checkPermission(
            activity: FragmentActivity?,
            choosePicType: Int,
            onDeniedAndAlwaysNotRequest: ((deniedPermissionList: MutableList<String>) -> Unit)? = null
        ) {
            activity?.apply {
                val needCheckPermission = if (choosePicType == CHOOSE_PIC_TYPE_IS_CAMERA) {
                    arrayOf(Manifest.permission.CAMERA, "android.permission.READ_MEDIA_IMAGES")
                } else {
                    arrayOf("android.permission.READ_MEDIA_IMAGES")
                }
                if (hasPermissions(needCheckPermission)) {
                    choosePic(this, choosePicType)
                } else {
                    requestPermissionsUseXXPermission(
                        permissions = needCheckPermission,
                        onGranted = { _, allRequestPermissionGranted ->
                            if (allRequestPermissionGranted) {
                                choosePic(this, choosePicType)
                            }
                        },
                        onDenied = {
                            showToast(
                                if (it.contains(Manifest.permission.CAMERA)) {
                                    R.string.camera_permission_reject_permission_text
                                } else {
                                    R.string.gallery_permission_reject_permission_text
                                }
                            )
                        },
                        onDeniedAndAlwaysNotRequest = {
                            //若拒绝权限且不再询问,引导去设置开启权限
                            onDeniedAndAlwaysNotRequest?.invoke(it)
                        }
                    )
                }
            }
        }

        //选择图片
        private fun choosePic(activity: FragmentActivity?, choosePicType: Int) {
            activity?.apply {
                when (choosePicType) {
                    CHOOSE_PIC_TYPE_IS_CAMERA -> {
                        ImageSelector
                            .builder()
                            .onlyTakePhoto(true)
                            .start(this, SELECT_PIC_REQUEST_CODE)
                    }
                    CHOOSE_PIC_TYPE_IS_GALLERY -> {
                        ImageSelector.builder()
                            .useCamera(false)
                            .setSingle(true)
                            .canPreview(true)
                            .start(this, SELECT_PIC_REQUEST_CODE)
                    }
                }
            }
        }
    }
}