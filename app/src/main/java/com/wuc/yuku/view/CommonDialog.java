package com.wuc.yuku.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuc.sdk.utils.Utils;
import com.wuc.yuku.R;

/**
 * @author: wuchao
 * @date: 2018/12/26 10:55
 * @desciption: 通用消息对话框
 */
public class CommonDialog extends Dialog {

    private ViewGroup mContentView;
    private TextView mTitleView;
    private TextView mContentTextView;
    private TextView mSignalBtnConfirm;
    private TextView mBtnConfirm;
    private TextView mBtnCancel;

    public CommonDialog(Context context, String titleMsg, String contentMsg, String confirmText, int dialogwidth,
                        DialogClickListener confirmListener) {
        this(context, titleMsg, contentMsg, confirmText, null, dialogwidth, confirmListener, null);
    }

    public CommonDialog(@NonNull Context context, String title, String contentMsg, String confirmText, String cancelText,
                        int dialogWidth, final DialogClickListener confirmClick,
                        final DialogClickListener cancelClick) {
        super(context, R.style.Base_Theme_AppCompat_Dialog);
        initDialogStyle(title, contentMsg, confirmText, cancelText, dialogWidth, confirmClick, cancelClick);
    }

    public CommonDialog(Context context, String titleMsg, String contentMsg, String confirmText,
                        DialogClickListener confirmListener) {
        this(context, titleMsg, contentMsg, confirmText, null, Utils.dip2px(context, 260), confirmListener, null);
    }

    public CommonDialog(Context context, String msg, String contentMsg, String confirmText, int dialogWidth,
                        DialogClickListener confirmClick, DialogClickListener cancelClick) {
        this(context, msg, contentMsg, confirmText, null, dialogWidth, confirmClick, cancelClick);
    }

    public CommonDialog(Context context, String msg, String contentMsg, String confirmText, String cancelText,
                        int dialogWidth, DialogClickListener confirmClick) {
        this(context, msg, contentMsg, confirmText, cancelText, dialogWidth, confirmClick, null);
    }

    public CommonDialog(Context context, String msg, String contentMsg, String confirmText, String cancelText,
                        DialogClickListener confirmClick) {
        this(context, msg, contentMsg, confirmText, cancelText, Utils.dip2px(context, 260), confirmClick, null);
    }

    public void setPositiveButtonColor(int color) {
        mSignalBtnConfirm.setTextColor(color);
    }

    private void initDialogStyle(String title, String contentMsg, String confirmText, String cancelText,
                                 int dialogWidth, final DialogClickListener confirmClick,
                                 final DialogClickListener cancelClick) {
        setContentView(createDialogView(R.layout.dialog_common_layout));
        setParams(dialogWidth, LayoutParams.WRAP_CONTENT);
        LinearLayout layoutAll = (LinearLayout) findViewById(R.id.all_layout);
        LinearLayout layoutSignal = (LinearLayout) findViewById(R.id.signal_layout);
        mTitleView = (TextView) findViewById(R.id.title);
        mContentTextView = (TextView) findViewById(R.id.message);
        mContentTextView.setText(contentMsg);
        mContentTextView.setGravity(Gravity.CENTER);
        if (!TextUtils.isEmpty(title)) {
            mTitleView.setText(title);
        }
        if (TextUtils.isEmpty(cancelText)) {
            layoutAll.setVisibility(View.GONE);
            layoutSignal.setVisibility(View.VISIBLE);
            mSignalBtnConfirm = (TextView) findViewById(R.id.signal_confirm_btn);
            mSignalBtnConfirm.setText(confirmText);
            mSignalBtnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    confirmClick.onDialogClick();
                }
            });
        } else {
            layoutAll.setVisibility(View.VISIBLE);
            layoutSignal.setVisibility(View.GONE);
            mBtnConfirm = (TextView) findViewById(R.id.confirm_btn);
            mBtnCancel = (TextView) findViewById(R.id.cancle_btn);
            mBtnConfirm.setText(confirmText);
            mBtnCancel.setText(cancelText);
            mBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (cancelClick != null) {
                        cancelClick.onDialogClick();
                    }
                }
            });
            mBtnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    confirmClick.onDialogClick();
                }
            });
        }
    }

    private ViewGroup createDialogView(int layoutId) {
        mContentView = (ViewGroup) LayoutInflater.from(getContext()).inflate(layoutId, null);
        return mContentView;
    }

    private void setParams(int width, int height) {
        Window window = this.getWindow();
        if (window != null) {
            WindowManager.LayoutParams dialogParam = window.getAttributes();
            dialogParam.width = width;
            dialogParam.height = height;
            window.setAttributes(dialogParam);
        }
    }

    public interface DialogClickListener {
        void onDialogClick();
    }
}
