package com.wuc.yuku.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wuc.sdk.constant.SDKConstant.AutoPlaySetting;
import com.wuc.sdk.core.VideoParameters;
import com.wuc.yuku.R;
import com.wuc.yuku.activity.base.BaseActivity;
import com.wuc.yuku.db.SpUtils;

/**
 * @author: wuchao
 * @date: 2018/12/25 23:19
 * @desciption: 视频播放设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    /**
     * UI
     */
    private RelativeLayout mWifiLayout;
    private RelativeLayout mAlwayLayout;
    private RelativeLayout mNeverLayout;
    private CheckBox mWifiBox, mAlwayBox, mNeverBox;
    private ImageView mBackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);
        initView();
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.back_view);
        mWifiLayout = (RelativeLayout) findViewById(R.id.wifi_layout);
        mWifiBox = (CheckBox) findViewById(R.id.wifi_check_box);
        mAlwayLayout = (RelativeLayout) findViewById(R.id.alway_layout);
        mAlwayBox = (CheckBox) findViewById(R.id.alway_check_box);
        mNeverLayout = (RelativeLayout) findViewById(R.id.close_layout);
        mNeverBox = (CheckBox) findViewById(R.id.close_check_box);

        mBackView.setOnClickListener(this);
        mWifiLayout.setOnClickListener(this);
        mAlwayLayout.setOnClickListener(this);
        mNeverLayout.setOnClickListener(this);

        int currentSetting = SpUtils.getInt(SpUtils.VIDEO_PLAY_SETTING, 1);
        switch (currentSetting) {
            case 0:
                mAlwayBox.setBackgroundResource(R.drawable.setting_selected);
                mWifiBox.setBackgroundResource(0);
                mNeverBox.setBackgroundResource(0);
                break;
            case 1:
                mAlwayBox.setBackgroundResource(0);
                mWifiBox.setBackgroundResource(R.drawable.setting_selected);
                mNeverBox.setBackgroundResource(0);
                break;
            case 2:
                mAlwayBox.setBackgroundResource(0);
                mWifiBox.setBackgroundResource(0);
                mNeverBox.setBackgroundResource(R.drawable.setting_selected);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alway_layout:
                SpUtils.putInt(SpUtils.VIDEO_PLAY_SETTING, 0);
                VideoParameters.setCurrentSetting(AutoPlaySetting.AUTO_PLAY_3G_4G_WIFI);
                mAlwayBox.setBackgroundResource(R.drawable.setting_selected);
                mWifiBox.setBackgroundResource(0);
                mNeverBox.setBackgroundResource(0);
                break;
            case R.id.close_layout:
                SpUtils.putInt(SpUtils.VIDEO_PLAY_SETTING, 2);
                VideoParameters.setCurrentSetting(AutoPlaySetting.AUTO_PLAY_NEVER);
                mAlwayBox.setBackgroundResource(0);
                mWifiBox.setBackgroundResource(0);
                mNeverBox.setBackgroundResource(R.drawable.setting_selected);
                break;
            case R.id.wifi_layout:
                SpUtils.putInt(SpUtils.VIDEO_PLAY_SETTING, 1);
                VideoParameters.setCurrentSetting(AutoPlaySetting.AUTO_PLAY_ONLY_WIFI);
                mAlwayBox.setBackgroundResource(0);
                mWifiBox.setBackgroundResource(R.drawable.setting_selected);
                mNeverBox.setBackgroundResource(0);
                break;
            case R.id.back_view:
                finish();
                break;
            default:
                break;
        }
    }
}
