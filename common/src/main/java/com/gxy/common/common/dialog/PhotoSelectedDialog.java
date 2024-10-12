package com.gxy.common.common.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gxy.common.R;
import com.luck.picture.lib.interfaces.OnItemClickListener;
import com.luck.picture.lib.utils.DensityUtil;

/**
 * @author：luck
 * @date：2019-12-12 16:39
 * @describe：PhotoSelectedDialog
 */
public class PhotoSelectedDialog extends DialogFragment implements View.OnClickListener {
    public static final int TAKE_PICTURE_TYPE = 0;
    public static final int PHOTO_ALBUM_TYPE = 1;
    public static final int PDF_TYPE = 2;
    private boolean isCancel = true;

    public static final String IS_SHOW_PDF_SELECT = "is_show_pdf_select";

    public static PhotoSelectedDialog newInstance(Boolean isShowPdfSelect) {
        PhotoSelectedDialog photoItemSelectedDialog = new PhotoSelectedDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_SHOW_PDF_SELECT, isShowPdfSelect);
        photoItemSelectedDialog.setArguments(bundle);
        return photoItemSelectedDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
        }
        return inflater.inflate(R.layout.dialog_photo_selected, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvPicturePhoto = view.findViewById(R.id.ps_tv_photo);
        TextView tvPictureVideo = view.findViewById(R.id.ps_tv_video);
        TextView tvPDF = view.findViewById(R.id.ps_tv_pdf);
        View pdfLine = view.findViewById(R.id.pdf_top_line);
        TextView tvPictureCancel = view.findViewById(R.id.ps_tv_cancel);
        tvPictureVideo.setOnClickListener(this);
        tvPicturePhoto.setOnClickListener(this);
        tvPDF.setOnClickListener(this);
        tvPictureCancel.setOnClickListener(this);
        if (getArguments() != null && !getArguments().getBoolean(IS_SHOW_PDF_SELECT, false)) {
            tvPDF.setVisibility(View.GONE);
            pdfLine.setVisibility(View.GONE);
        } else {
            tvPDF.setVisibility(View.VISIBLE);
            pdfLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialogStyle();
    }

    /**
     * DialogFragment Style
     */
    private void initDialogStyle() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(DensityUtil.getRealScreenWidth(getContext()), RelativeLayout.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.PictureThemeDialogFragmentAnim);
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnDismissListener onDismissListener;

    public void setOnDismissListener(OnDismissListener listener) {
        this.onDismissListener = listener;
    }

    public interface OnDismissListener {
        void onDismiss(boolean isCancel, DialogInterface dialog);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (onItemClickListener != null) {
            if (id == R.id.ps_tv_photo) {
                onItemClickListener.onItemClick(v, TAKE_PICTURE_TYPE);
                isCancel = false;
            } else if (id == R.id.ps_tv_video) {
                onItemClickListener.onItemClick(v, PHOTO_ALBUM_TYPE);
                isCancel = false;
            } else if (id == R.id.ps_tv_pdf) {
                onItemClickListener.onItemClick(v, PDF_TYPE);
                isCancel = false;
            }
        }
        dismissAllowingStateLoss();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(isCancel, dialog);
        }
    }
}

