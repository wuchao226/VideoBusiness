package com.wuc.sdk.core.video;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wuc.sdk.constant.SDKConstant;
import com.wuc.sdk.core.VideoParameters;
import com.wuc.sdk.module.AdValue;
import com.wuc.sdk.report.ReportManager;
import com.wuc.sdk.utils.Utils;
import com.wuc.sdk.widget.CustomVideoView;
import com.wuc.sdk.widget.VideoFullDialog;

/**
 * @author: wuchao
 * @date: 2018/12/21 13:51
 * @desciption: 视频业务逻辑层
 */
public class VideoSlot implements CustomVideoView.VideoPlayerListener {

    private Context mContext;
    /**
     * UI
     */
    private CustomVideoView mVideoView;
    /**
     * 要添加到的父容器
     */
    private ViewGroup mParentView;
    /**
     * Data
     */
    private AdValue mAdValue;
    private SDKSlotListener mSlotListener;
    /**
     * 是否可自动暂停标志位
     */
    private boolean canPause = false;
    /**
     * 防止将要滑入滑出时播放器的状态改变
     */
    private int lastArea = 0;

    public VideoSlot(AdValue adValue, SDKSlotListener slotListener, CustomVideoView.FrameImageLoadListener frameLoadListener) {
        mAdValue = adValue;
        mSlotListener = slotListener;
        mParentView = slotListener.getVideoParent();
        mContext = mParentView.getContext();
        initVideoView(frameLoadListener);
    }

    private void initVideoView(CustomVideoView.FrameImageLoadListener frameLoadListener) {
        mVideoView = new CustomVideoView(mContext, mParentView);
        if (mAdValue != null) {
            mVideoView.setDataSource(mAdValue.resource);
            mVideoView.setFrameURI(mAdValue.thumb);
            mVideoView.setFrameLoadListener(frameLoadListener);
            mVideoView.setVideoPlayerListener(this);
        }
        RelativeLayout paddingView = new RelativeLayout(mContext);
        paddingView.setBackgroundColor(mContext.getResources().getColor(android.R.color.black));
        paddingView.setLayoutParams(mVideoView.getLayoutParams());
        mParentView.addView(paddingView);
        mParentView.addView(mVideoView);
    }

    public void updateVideoInScrollView() {
        int currentArea = Utils.getVisiblePercent(mParentView);
        //小于0表示未出现在屏幕上，不做处理
        if (currentArea < 0) {
            return;
        }
        //刚要滑入和滑出时，异常状态的处理
        if (Math.abs(currentArea - lastArea) >= 100) {
            return;
        }
        if (currentArea < SDKConstant.VIDEO_SCREEN_PERCENT) {
            //进入自动暂停状态
            if (canPause) {
                pauseVideo(true);
                canPause = false;
            }
            lastArea = 0;
            // 滑动出50%后标记为从头开始播
            mVideoView.setIsComplete(false);
            mVideoView.setIsRealPause(false);
            return;
        }
        if (isRealPause() || isComplete()) {
            //进入手动暂停或者播放结束，播放结束和不满足自动播放条件都作为手动暂停
            pauseVideo(false);
            canPause = false;
            return;
        }
        //满足自动播放条件或者用户主动点击播放，开始播放
        if (Utils.canAutoPlay(mContext, VideoParameters.getCurrentSetting()) || isPlaying()) {
            lastArea = currentArea;
            resumeVideo();
            canPause = true;
            mVideoView.setIsRealPause(false);
        } else {
            pauseVideo(false);
            //不能自动播放则设置为手动暂停效果
            mVideoView.setIsRealPause(true);
        }
    }

    /**
     * 暂停
     *
     * @param isAuto
     */
    public void pauseVideo(boolean isAuto) {
        if (mVideoView != null) {
            if (isAuto) {
                //发动暂停监测
                if (!isRealPause() && isPlaying()) {
                    try {
                        ReportManager.pauseVideoReport(mAdValue.event.pause.content, getPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            mVideoView.seekAndPause(0);
        }
    }

    /**
     * 播放器是否真正暂停
     */
    private boolean isRealPause() {
        if (mVideoView != null) {
            return mVideoView.isRealPause();
        }
        return false;
    }

    /**
     * 播放器是否播放完成
     */
    private boolean isComplete() {
        if (mVideoView != null) {
            return mVideoView.isComplete();
        }
        return false;
    }

    /**
     * 是否正在播放
     */
    private boolean isPlaying() {
        if (mVideoView != null) {
            return mVideoView.isPlaying();
        }
        return false;
    }

    private void resumeVideo() {
        if (mVideoView != null) {
            mVideoView.resume();
            if (isPlaying()) {
                //发自动播放监测
                sendSUSReport(true);
            }
        }
    }

    private int getPosition() {
        return mVideoView.getCurrentPosition() / SDKConstant.MILLION_UNIT;
    }

    /**
     * 发送视频开始播放监测
     */
    private void sendSUSReport(boolean isAuto) {
        try {
            ReportManager.susReport(mAdValue.startMonitor, isAuto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        mVideoView.destroy();
        mVideoView = null;
        mContext = null;
        mAdValue = null;
    }

    @Override
    public void onBufferUpdate(int time) {
        try {
            ReportManager.suReport(mAdValue.middleMonitor, time / SDKConstant.MILLION_UNIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickFullScreenBtn() {
        try {
            ReportManager.fullScreenReport(mAdValue.event.full.content, getPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取videoview在当前界面的属性
        Bundle bundle = Utils.getViewProperty(mParentView);
        //将播放器从view树中移除
        mParentView.removeView(mVideoView);
        VideoFullDialog dialog = new VideoFullDialog(mContext, mVideoView, mAdValue,
                mVideoView.getCurrentPosition());
        dialog.setListener(new VideoFullDialog.FullToSmallListener() {
            @Override
            public void getCurrentPlayPosition(int position) {
                backToSmallMode(position);
            }

            @Override
            public void playComplete() {
                bigPlayComplete();
            }
        });
        //为Dialog设置播放器数据Bundle对象
        dialog.setViewBundle(bundle);
        dialog.setSlotListener(mSlotListener);
        dialog.show();
    }

    private void backToSmallMode(int position) {
        if (mVideoView.getParent() == null) {
            mParentView.addView(mVideoView);
        }
        //防止动画导致偏离父容器
        mVideoView.setTranslationY(0);
        mVideoView.isShowFullBtn(true);
        //小屏静音播放
        mVideoView.mute(true);
        mVideoView.setVideoPlayerListener(this);
        mVideoView.seekAndResume(position);
        // 标为可自动暂停
        canPause = true;
    }

    private void bigPlayComplete() {
        if (mVideoView.getParent() == null) {
            mParentView.addView(mVideoView);
        }
        //防止动画导致偏离父容器
        mVideoView.setTranslationY(0);
        mVideoView.isShowFullBtn(true);
        mVideoView.mute(true);
        mVideoView.setVideoPlayerListener(this);
        mVideoView.seekAndPause(0);
        canPause = false;
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
            /*if (mVideoView.isFrameHidden() && !TextUtils.isEmpty(desationUrl)) {
                Intent intent = new Intent(mContext, AdBrowserActivity.class);
                intent.putExtra(AdBrowserActivity.KEY_URL, mXAdInstance.clickUrl);
                mContext.startActivity(intent);
                try {
                    ReportManager.pauseVideoReport(mAdValue.clickMonitor, mVideoView.getCurrentPosition()
                            / SDKConstant.MILLION_UNIT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        }
    }

    @Override
    public void onClickBackBtn() {

    }

    @Override
    public void onClickPlay() {
        sendSUSReport(false);
    }

    @Override
    public void onVideoLoadSuccess() {
        if (mSlotListener != null) {
            mSlotListener.onVideoLoadSuccess();
        }
    }

    @Override
    public void onVideoLoadFailed() {
        if (mSlotListener != null) {
            mSlotListener.onVideoLoadFailed();
        }
        //加载失败全部回到初始状态
        canPause = false;
    }

    @Override
    public void onVideoLoadComplete() {
        try {
            ReportManager.sueReport(mAdValue.endMonitor, false, getDuration());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mSlotListener != null) {
            mSlotListener.onVideoLoadComplete();
        }
        mVideoView.setIsRealPause(true);
    }

    private int getDuration() {
        return mVideoView.getDuration() / SDKConstant.MILLION_UNIT;
    }

    /**
     * 传递消息到app层
     */
    public interface SDKSlotListener {
        /**
         * 要添加到的父容器
         */
        ViewGroup getVideoParent();

        /**
         * 视频加载成功的事件监听
         */
        void onVideoLoadSuccess();

        /**
         * 视频加载失败的事件监听
         */
        void onVideoLoadFailed();

        /**
         * 视频播放完成的事件监听
         */
        void onVideoLoadComplete();

        /**
         * 点击视频区域的事件
         *
         * @param url
         */
        void onClickVideo(String url);
    }
}
