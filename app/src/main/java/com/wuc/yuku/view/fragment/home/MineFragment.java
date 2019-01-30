package com.wuc.yuku.view.fragment.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuc.sdk.okhttp.listener.DisposeDataListener;
import com.wuc.sdk.utils.ImageLoaderUtil;
import com.wuc.yuku.R;
import com.wuc.yuku.activity.LoginActivity;
import com.wuc.yuku.activity.SettingActivity;
import com.wuc.yuku.constant.Constant;
import com.wuc.yuku.manager.UserManager;
import com.wuc.yuku.module.update.UpdateModel;
import com.wuc.yuku.network.RequestCenter;
import com.wuc.yuku.service.update.UpdateService;
import com.wuc.yuku.util.Util;
import com.wuc.yuku.view.CommonDialog;
import com.wuc.yuku.view.QrCodeDialog;
import com.wuc.yuku.view.fragment.BaseFragment;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author: wuchao
 * @date: 2018/12/4 19:32
 * @desciption:
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {


    /**
     * UI
     */
    private View mContentView;
    private RelativeLayout mLoginLayout;
    private CircleImageView mPhotoView;
    private TextView mLoginInfoView;
    private TextView mLoginView;
    private RelativeLayout mLoginedLayout;
    private TextView mUserNameView;
    private TextView mTickView;
    private TextView mVideoPlayerView;
    private TextView mShareView;
    private TextView mQrCodeView;
    private TextView mUpdateView;
    private LoginBroadcastReceiver mReceiver = new LoginBroadcastReceiver();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        registerBroadcast();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_mine_layout, container, false);
        initView(mContentView);
        return mContentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //根据用户信息更新我们的fragment
        if (UserManager.getInstance().hasLogined()) {
            if (mLoginedLayout.getVisibility() == View.GONE) {
                mLoginLayout.setVisibility(View.GONE);
                mLoginedLayout.setVisibility(View.VISIBLE);
                mUserNameView.setText(UserManager.getInstance().getUser().data.name);
                mTickView.setText(UserManager.getInstance().getUser().data.tick);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterBroadcast();
    }

    /**
     * 取消注册广播
     */
    private void unRegisterBroadcast() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    private void initView(View mContentView) {
        mLoginLayout = mContentView.findViewById(R.id.login_layout);
        mLoginLayout.setOnClickListener(this);
        mLoginedLayout = mContentView.findViewById(R.id.logined_layout);
        mLoginedLayout.setOnClickListener(this);

        mPhotoView = mContentView.findViewById(R.id.photo_view);
        mPhotoView.setOnClickListener(this);
        mLoginView = mContentView.findViewById(R.id.login_view);
        mLoginView.setOnClickListener(this);
        mVideoPlayerView = mContentView.findViewById(R.id.video_setting_view);
        mVideoPlayerView.setOnClickListener(this);
        mShareView = mContentView.findViewById(R.id.share_imooc_view);
        mShareView.setOnClickListener(this);
        mQrCodeView = mContentView.findViewById(R.id.my_qrcode_view);
        mQrCodeView.setOnClickListener(this);
        mLoginInfoView = mContentView.findViewById(R.id.login_info_view);
        mUserNameView = mContentView.findViewById(R.id.username_view);
        mTickView = mContentView.findViewById(R.id.tick_view);

        mUpdateView = mContentView.findViewById(R.id.update_view);
        mUpdateView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.login_layout:
            case R.id.login_view:
                if (!UserManager.getInstance().hasLogined()) {
                    toLogin();
                }
                break;
            case R.id.video_setting_view:
                mContext.startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.share_imooc_view:
                break;
            case R.id.my_qrcode_view:
                if (!UserManager.getInstance().hasLogined()) {
                    toLogin();
                }else {
                    //已登陆根据用户ID生成二维码显示
                    QrCodeDialog dialog = new QrCodeDialog(mContext);
                    dialog.show();
                }
                break;
            case R.id.update_view:
                if (hasPermission(Constant.WRITE_READ_EXTERNAL_PERMISSION)) {
                    checkVersion();
                } else {
                    requestPermission(Constant.WRITE_READ_EXTERNAL_CODE, Constant.WRITE_READ_EXTERNAL_PERMISSION);
                }
                break;
        }
    }

    /**
     * 去登录页面
     */
    private void toLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 发送版本检查更新请求
     */
    public void checkVersion() {
        RequestCenter.checkVersion(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                final UpdateModel updateModel = (UpdateModel) responseObj;
                if (Util.getVersionCode(mContext) < updateModel.data.currentVersion) {
                    CommonDialog commonDialog = new CommonDialog(mContext, getString(R.string.update_new_version),
                            getString(R.string.update_title), getString(R.string.update_install),
                            getString(R.string.cancel), new CommonDialog.DialogClickListener() {
                        @Override
                        public void onDialogClick() {
                            Intent intent = new Intent(mContext, UpdateService.class);
                            mContext.startService(intent);
                        }
                    });
                    commonDialog.show();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter(LoginActivity.LOGIN_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, filter);
    }

    @Override
    public void doWriteSDCard() {
        checkVersion();
    }

    private class LoginBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (UserManager.getInstance().hasLogined()) {
                //更新我们的fragment
                if (mLoginedLayout.getVisibility() == View.GONE) {
                    mLoginLayout.setVisibility(View.GONE);
                    mLoginedLayout.setVisibility(View.VISIBLE);
                    mUserNameView.setText(UserManager.getInstance().getUser().data.name);
                    mTickView.setText(UserManager.getInstance().getUser().data.tick);
                    ImageLoaderUtil.getInstance(mContext).displayImage(mPhotoView, UserManager.getInstance().getUser().data.photoUrl);
                }
            }
        }
    }
}
