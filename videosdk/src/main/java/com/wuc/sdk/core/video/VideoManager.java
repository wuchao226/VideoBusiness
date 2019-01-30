package com.wuc.sdk.core.video;

import android.view.ViewGroup;

import com.wuc.sdk.core.VideoManagerInterface;
import com.wuc.sdk.module.AdValue;
import com.wuc.sdk.okhttp.HttpConstant;
import com.wuc.sdk.okhttp.HttpConstant.Params;
import com.wuc.sdk.report.ReportManager;
import com.wuc.sdk.utils.ResponseEntityToModule;
import com.wuc.sdk.utils.Utils;
import com.wuc.sdk.widget.CustomVideoView;

/**
 * @author: wuchao
 * @date: 2018/12/21 15:58
 * @desciption: 管理slot, 与外界进行通信
 */
public class VideoManager implements VideoSlot.SDKSlotListener {

    private ViewGroup mParentView;
    private VideoSlot mVideoSlot;
    private AdValue mAdValue;
    private VideoManagerInterface mListener;
    private CustomVideoView.FrameImageLoadListener mFrameLoadListener;

    public VideoManager(ViewGroup parentView, String value, CustomVideoView.FrameImageLoadListener imageLoadListener) {
        mParentView = parentView;
        mAdValue = (AdValue) ResponseEntityToModule.
                parseJsonToModule(value, AdValue.class);
        mFrameLoadListener = imageLoadListener;
        load();
    }

    private void load() {
        if (mAdValue != null && mAdValue.resource != null) {
            mVideoSlot = new VideoSlot(mAdValue, this, mFrameLoadListener);
            //发送解析成功事件
            sendAnalysisReport(Params.ad_analize, HttpConstant.AD_DATA_SUCCESS);
        } else {
            //创建空的slot,不响应任何事件
            mVideoSlot = new VideoSlot(null, this, mFrameLoadListener);
            if (mListener != null) {
                mListener.onVideoFailed();
            }
            sendAnalysisReport(Params.ad_analize, HttpConstant.AD_DATA_FAILED);
        }
    }

    /**
     * 发送广告数据解析成功监测
     */
    private void sendAnalysisReport(Params step, String result) {
        try {
            ReportManager.sendAdMonitor(Utils.isPad(mParentView.getContext().
                            getApplicationContext()), mAdValue == null ? "" : mAdValue.resourceID,
                    (mAdValue == null ? null : mAdValue.adid), Utils.getAppVersion(mParentView.getContext()
                            .getApplicationContext()), step, result);
        } catch (Exception e) {

        }
    }

    public void setListener(VideoManagerInterface listener) {
        mListener = listener;
    }

    /**
     * 根据滑动距离来判断是否可以自动播放, 出现超过50%自动播放，离开超过50%,自动暂停
     */
    public void updateVideoInScrollView() {
        if (mVideoSlot != null) {
            mVideoSlot.updateVideoInScrollView();
        }
    }

    /**
     * release the video
     */
    public void destroy() {
        mVideoSlot.destroy();
    }

    @Override
    public ViewGroup getVideoParent() {
        return mParentView;
    }

    @Override
    public void onVideoLoadSuccess() {
        if (mListener != null) {
            mListener.onVideoSuccess();
        }
        sendAnalysisReport(Params.ad_load, HttpConstant.AD_PLAY_SUCCESS);
    }

    @Override
    public void onVideoLoadFailed() {
        if (mListener != null) {
            mListener.onVideoFailed();
        }
        sendAnalysisReport(Params.ad_load, HttpConstant.AD_PLAY_FAILED);
    }

    @Override
    public void onVideoLoadComplete() {

    }

    @Override
    public void onClickVideo(String url) {
        if (mListener != null) {
            mListener.onClickVideo(url);
        } else {
//            Intent intent = new Intent(mParentView.getContext(), AdBrowserActivity.class);
//            intent.putExtra(AdBrowserActivity.KEY_URL, url);
//            mParentView.getContext().startActivity(intent);
        }
    }
}
