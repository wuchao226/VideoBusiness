package com.wuc.yuku.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuc.sdk.utils.Utils;
import com.wuc.yuku.R;
import com.wuc.yuku.manager.UserManager;
import com.wuc.yuku.zxing.encode.CodeCreator;

/**
 * @author: wuchao
 * @date: 2019/1/4 11:40
 * @desciption: 二维码生成
 */
public class QrCodeDialog extends Dialog {

    private Context mContext;

    /**
     * UI
     */
    private ImageView mQrCodeView;
    private TextView mTickView;
    private TextView mCloseView;

    public QrCodeDialog(@NonNull Context context) {
        super(context, 0);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_code_layout);
        initView();
    }

    private void initView() {
        mQrCodeView = findViewById(R.id.qrcode_view);
        mTickView = findViewById(R.id.tick_view);
        mCloseView = findViewById(R.id.close_view);
        mCloseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        String name = UserManager.getInstance().getUser().data.name;
        mQrCodeView.setImageBitmap(CodeCreator.createQRCode(name, Utils.dip2px(mContext, 200),
                Utils.dip2px(mContext, 200), null));
        mTickView.setText(name + mContext.getString(R.string.personal_info));
    }
}
