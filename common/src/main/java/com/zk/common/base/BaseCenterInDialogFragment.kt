package com.zk.common.base

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.zk.common.R
import com.zk.common.utils.getScreenHeight
import com.zk.common.utils.getScreenWidth

open class BaseCenterInDialogFragment : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.TransparentProgressDialog)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ((context?.getScreenWidth() ?: 0) * resetDialogWidthPercent()).toInt(),
            if (resetDialogHeightPercent() == 0f) ViewGroup.LayoutParams.WRAP_CONTENT else ((context?.getScreenHeight()
                ?: 0) * resetDialogHeightPercent()).toInt()
        )

        dialog?.window?.attributes = dialog?.window?.attributes.apply {
            this?.windowAnimations = R.style.centerDialogZoomAnim
        }
    }

    open fun resetDialogWidthPercent(): Float = 0.85f

    open fun resetDialogHeightPercent(): Float = 0f

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (_: Exception) {
        }
    }
}