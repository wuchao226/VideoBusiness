package com.wuc.sdk.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wuc.sdk.R;
import com.wuc.sdk.constant.SDKConstant;
import com.wuc.sdk.core.video.VideoSlot;
import com.wuc.sdk.module.AdValue;
import com.wuc.sdk.report.ReportManager;
import com.wuc.sdk.utils.Utils;

/**
 * @author: wuchao
 * @date: 2018/12/24 15:34
 * @desciption: 全屏显示视频
 */
public class VideoFullDialog extends Dialog implements CustomVideoView.VideoPlayerListener {

    private CustomVideoView mVideoView;
    private Context mContext;
    private RelativeLayout mRootView;
    private ViewGroup mParentView;
    private ImageView mBackButton;
    private AdValue mAdValue;
    private FullToSmallListener mFullToSmallListener;
    private boolean isFirst = true;
    /**
     * 动画要执行的平均值
     */
    private int deltaY;
    /**
     * 从小屏到全屏的播放位置
     */
    private int mPosition;

    private VideoSlot.SDKSlotListener mSlotListener;
    private Bundle mStartBundle;
    /**
     * 用于Dialog出入场动画
     */
    private Bundle mEndBundle;


    public VideoFullDialog(@NonNull Context context, CustomVideoView videoView, AdValue adValue, int position) {
        super(context, R.style.dialog_full_screen);
        mContext = context;
        mAdValue = adValue;
        mPosition = position;
        mVideoView = videoView;
    }

    public void setViewBundle(Bundle bundle) {
        mStartBundle = bundle;
    }

    public void setListener(FullToSmallListener listener) {
        this.mFullToSmallListener = listener;
    }

    public void setSlotListener(VideoSlot.SDKSlotListener slotListener) {
        mSlotListener = slotListener;
    }

    @Override
    public void onBufferUpdate(int time) {
        try {
            if (mAdValue != null) {
                ReportManager.suReport(mAdValue.middleMonitor, time / SDKConstant.MILLION_UNIT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickFullScreenBtn() {
        onClickVideo();
    }

    @Override
    public void onClickVideo() {
        String desationUrl = mAdValue.clickUrl;
        if (mSlotListener != null) {
            if (mVideoView.isFrameHidden() && !TextUtils.isEmpty(desationUrl)) {
                mSlotListener.onClickVideo(desationUrl);
                try {
                    ReportManager.pauseVideoReport(mAdValue.clickMonitor, mVideoView.getCurrentPosition()
                            / SDKConstant.MILLION_UNIT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            //走默认样式
            if (mVideoView.isFrameHidden() && !TextUtils.isEmpty(desationUrl)) {
                /*Intent intent = new Intent(mContext, AdBrowserActivity.class);
                intent.putExtra(AdBrowserActivity.KEY_URL, mAdValue.clickUrl);
                mContext.startActivity(intent);
                try {
                    ReportManager.pauseVideoReport(mAdValue.clickMonitor, mVideoView.getCurrentPosition()
                            / SDKConstant.MILLION_UNIT);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        }
    }

    @Override
    public void onClickBackBtn() {
        runExitAnimator();
    }

    @Override
    public void onClickPlay() {

    }

    @Override
    public void onVideoLoadSuccess() {
        if (mVideoView != null) {
            mVideoView.resume();
        }
    }

    @Override
    public void onVideoLoadFailed() {

    }

    @Override
    public void onVideoLoadComplete() {
        try {
            int position = mVideoView.getDuration() / SDKConstant.MILLION_UNIT;
            ReportManager.sueReport(mAdValue.endMonitor, true, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dismiss();
        if (mFullToSmallListener != null) {
            mFullToSmallListener.playComplete();
        }
    }

    @Override
    public void dismiss() {
        mParentView.removeView(mVideoView);
        super.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sdk_dialog_video_layout);
        initVideoView();
    }

    @Override
    public void onBackPressed() {
        onClickBackBtn();
        //禁止掉返回键本身的关闭功能,转为自己的关闭效果
        //super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //super.onWindowFocusChanged(hasFocus);
        //防止第一次，有些手机仍显示全屏按钮
        mVideoView.isShowFullBtn(false);
        if (!hasFocus) {
            mPosition = mVideoView.getCurrentPosition();
            mVideoView.pauseForFullScreen();
        } else {
            //为了适配某些手机不执行seekandresume中的播放方法
            if (isFirst) {
                mVideoView.seekAndResume(mPosition);
            } else {
                mVideoView.resume();
            }
        }
        isFirst = false;
    }

    private void initVideoView() {
        mParentView = (RelativeLayout) findViewById(R.id.content_layout);
        mBackButton = findViewById(R.id.sdk_player_close_btn);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBackBtn();
            }
        });
        mRootView = findViewById(R.id.root_view);
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickVideo();
            }
        });
        //mRootView.setVisibility(View.INVISIBLE);
        mVideoView.setVideoPlayerListener(this);
        mVideoView.mute(false);
        mParentView.addView(mVideoView);
       /* mParentView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mParentView.getViewTreeObserver().removeOnPreDrawListener(this);
                prepareScene();
                runEnterAnimation();
                return true;
            }
        });*/
    }

    /**
     * 准备动画所需数据
     */
    private void prepareScene() {
        mEndBundle = Utils.getViewProperty(mVideoView);
        //将desationview移到originalview位置处
        deltaY = (mStartBundle.getInt(Utils.PROPNAME_SCREENLOCATION_TOP)
                - mEndBundle.getInt(Utils.PROPNAME_SCREENLOCATION_TOP));
        mVideoView.setTranslationY(deltaY);
    }

    /**
     * 准备入场动画
     */
    private void runEnterAnimation() {
        mVideoView.animate()
                .setDuration(200)
                .setInterpolator(new LinearInterpolator())
                .translationY(0)
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        mRootView.setVisibility(View.VISIBLE);
                    }
                });
    }

    /**
     * 准备出场动画
     */
    private void runExitAnimator() {
        mVideoView.animate()
                .setDuration(200)
                .setInterpolator(new LinearInterpolator())
                .translationY(deltaY)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                        try {
                            ReportManager.exitfullScreenReport(mAdValue.event.exitFull.content, mVideoView.getCurrentPosition()
                                    / SDKConstant.MILLION_UNIT);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (mFullToSmallListener != null) {
                            mFullToSmallListener.getCurrentPlayPosition(mVideoView.getCurrentPosition());
                        }
                    }
                }).start();
    }

    public interface FullToSmallListener {
        /**
         * 当前播放位置（全屏播放中点击关闭按钮或者back健时回调）
         *
         * @param position
         */
        void getCurrentPlayPosition(int position);

        /**
         * 全屏播放结束时回调
         */
        void playComplete();
    }
}
