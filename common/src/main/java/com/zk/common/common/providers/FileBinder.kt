package com.zk.common.common.providers

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.chad.library.adapter.base.binder.QuickViewBindingItemBinder
import com.zk.common.R
import com.zk.common.databinding.ViewTypeFileBinding
import com.zk.common.entity.common.FileItemEntity
import com.zyxcoder.mvvmroot.ext.onContinuousClick
import com.zyxcoder.mvvmroot.utils.ImageOptions
import com.zyxcoder.mvvmroot.utils.loadImage

/**
 * @author zhangyuxiang
 * @date 2024/4/11
 * 上传文件模版
 */
class FileBinder(private val onUploadFileClickListener: ((ivHodler: ImageView) -> Unit)? = null) :
    QuickViewBindingItemBinder<FileItemEntity, ViewTypeFileBinding>() {
    override fun convert(holder: BinderVBHolder<ViewTypeFileBinding>, data: FileItemEntity) {
        holder.viewBinding.apply {
            tvContractFileDesc.text = data.lableName
            tvPleaseUploadContractFile.text = data.hintContent
            if (data.fileIsPic == true) {
                ivContractFile.loadImage(
                    url = data.fileAddress,
                    imageOptions = ImageOptions().apply {
                        placeholder = R.drawable.ic_upload_pic
                        error = R.drawable.ic_upload_pic
                        fallback = R.drawable.ic_upload_pic
                    })
            }
            ivContractFile.onContinuousClick {
                onUploadFileClickListener?.invoke(ivContractFile)
            }
        }
    }

    override fun onCreateViewBinding(
        layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int
    ) = ViewTypeFileBinding.inflate(layoutInflater, parent, false)
}