package com.zk.common.common.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.zk.common.R
import com.zk.common.databinding.LayoutZkLoadingDialogBinding
import com.zyxcoder.mvvmroot.utils.loadImage

class LoadingDialog : DialogFragment() {
    private lateinit var binding: LayoutZkLoadingDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = LayoutZkLoadingDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.ivLoading.loadImage(R.drawable.gif_loading)
    }
}