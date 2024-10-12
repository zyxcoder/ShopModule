package com.gxy.common.common.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.gxy.common.R
import com.gxy.common.databinding.LayoutLoadingDialogBinding
import com.zyxcoder.mvvmroot.utils.loadImage

class LoadingDialog : DialogFragment() {
    private lateinit var binding: LayoutLoadingDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = LayoutLoadingDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.ivLoading.loadImage(R.drawable.gif_loading)
    }
}