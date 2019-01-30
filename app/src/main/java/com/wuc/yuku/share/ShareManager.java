package com.wuc.yuku.share;

import android.content.Context;

import com.mob.MobSDK;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

import static com.wuc.yuku.share.ShareManager.PlatformType.QQ;
import static com.wuc.yuku.share.ShareManager.PlatformType.QZone;
import static com.wuc.yuku.share.ShareManager.PlatformType.WeChat;
import static com.wuc.yuku.share.ShareManager.PlatformType.WechatMoments;

/**
 * @author: wuchao
 * @date: 2019/1/4 18:00
 * @desciption: 分享功能统一入口，负责发送数据到指定平台,可以优化为一个单例模式
 */
public class ShareManager {

    /**
     * 要分享到的平台
     */
    private Platform mCurrentPlatform;

    private ShareManager() {
    }

    public static ShareManager getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 第一个执行的方法,最好在程序入口入执行
     *
     * @param context context
     */
    public static void initSDK(Context context) {
        MobSDK.init(context);
    }

    /**
     * 分享数据到不同的平台
     */
    public void shareData(ShareData shareData, PlatformActionListener listener) {
        switch (shareData.mPlatformType) {
            case QQ:
                mCurrentPlatform = ShareSDK.getPlatform(QQ.name());
                break;
            case QZone:
                mCurrentPlatform = ShareSDK.getPlatform(QZone.name());
                break;
            case WeChat:
                mCurrentPlatform = ShareSDK.getPlatform(WeChat.name());
                break;
            case WechatMoments:
                mCurrentPlatform = ShareSDK.getPlatform(WechatMoments.name());
                break;
            default:
        }
        //由应用层去处理回调,分享平台不关心。
        mCurrentPlatform.setPlatformActionListener(listener);
        mCurrentPlatform.share(shareData.mShareParams);
    }

    /**
     * 应用程序需要的平台
     */
    public enum PlatformType {
        //qq
        QQ,
        QZone,
        WeChat,
        WechatMoments
    }

    private static class Holder {
        private static final ShareManager INSTANCE = new ShareManager();
    }
}
