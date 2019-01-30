package com.wuc.yuku.share;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuc.yuku.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * @author: wuchao
 * @date: 2019/1/4 18:22
 * @desciption: 分享Dialog
 */
public class ShareDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    /**
     * 是否显示下载
     */
    private boolean isShowDownload;
    private DisplayMetrics mDisplayMetrics;

    /**
     * UI
     */
    private RelativeLayout mWeixinLayout;
    private RelativeLayout mWeixinMomentLayout;
    private RelativeLayout mQQLayout;
    private RelativeLayout mQZoneLayout;
    private RelativeLayout mDownloadLayout;
    private TextView mCancelView;

    /**
     * share relative
     */
    /**
     * 指定分享类型
     */
    private int mShareType;
    /**
     * 指定分享内容标题
     */
    private String mShareTitle;
    /**
     * 指定分享内容文本
     */
    private String mShareText;
    /**
     * 指定分享本地图片
     */
    private String mSharePhoto;
    /**
     * titleUrl是标题的网络链接，仅在人人网和QQ空间使用
     */
    private String mShareTileUrl;
    /**
     * siteUrl是分享此内容的网站地址，仅在QQ空间使用
     */
    private String mShareSiteUrl;
    /**
     * site是分享此内容的网站名称，仅在QQ空间使用
     */
    private String mShareSite;
    /**
     * url仅在微信（包括好友和朋友圈）中使用
     */
    private String mUrl;
    /**
     * 下载链接
     */
    private String mResourceUrl;
    private PlatformActionListener mListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
        }

        @Override
        public void onCancel(Platform platform, int i) {
        }
    };

    public ShareDialog(@NonNull Context context, boolean isShowDownload) {
        super(context, R.style.SheetDialogStyle);
        mContext = context;
        this.isShowDownload = isShowDownload;
        mDisplayMetrics = mContext.getResources().getDisplayMetrics();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_layout);
        initView();
    }

    private void initView() {
        //通过获取到dialog的window来控制dialog的宽高及位置
        Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = mDisplayMetrics.widthPixels;
            window.setAttributes(layoutParams);
        }
        mWeixinLayout = findViewById(R.id.weixin_layout);
        mWeixinLayout.setOnClickListener(this);
        mWeixinMomentLayout = findViewById(R.id.moment_layout);
        mWeixinMomentLayout.setOnClickListener(this);
        mQQLayout = findViewById(R.id.qq_layout);
        mQQLayout.setOnClickListener(this);
        mQZoneLayout = findViewById(R.id.qzone_layout);
        mQZoneLayout.setOnClickListener(this);
        mDownloadLayout = findViewById(R.id.download_layout);
        mDownloadLayout.setOnClickListener(this);
        if (isShowDownload) {
            mDownloadLayout.setVisibility(View.VISIBLE);
        }
        mCancelView = findViewById(R.id.cancel_view);
        mCancelView.setOnClickListener(this);
    }

    public void setResourceUrl(String resourceUrl) {
        mResourceUrl = resourceUrl;
    }

    /**
     * 指定分享内容标题
     */
    public void setShareTitle(String title) {
        mShareTitle = title;
    }

    /**
     * 指定分享本地图片
     */
    public void setImagePhoto(String photo) {
        mSharePhoto = photo;
    }

    /**
     * 指定分享类型
     */
    public void setShareType(int type) {
        mShareType = type;
    }

    /**
     * site是分享此内容的网站名称，仅在QQ空间使用
     */
    public void setShareSite(String site) {
        mShareSite = site;
    }

    /**
     * titleUrl是标题的网络链接，仅在人人网和QQ空间使用
     */
    public void setShareTitleUrl(String titleUrl) {
        mShareTileUrl = titleUrl;
    }

    /**
     * url仅在微信（包括好友和朋友圈）中使用
     */
    public void setUrl(String url) {
        mUrl = url;
    }

    /**
     * siteUrl是分享此内容的网站地址，仅在QQ空间使用
     */
    public void setShareSiteUrl(String siteUrl) {
        mShareSiteUrl = siteUrl;
    }

    /**
     * 指定分享内容文本
     */
    public void setShareText(String text) {
        mShareText = text;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weixin_layout:
                shareData(ShareManager.PlatformType.WeChat);
                break;
            case R.id.moment_layout:
                shareData(ShareManager.PlatformType.WechatMoments);
                break;
            case R.id.qq_layout:
                shareData(ShareManager.PlatformType.QQ);
                break;
            case R.id.qzone_layout:
                shareData(ShareManager.PlatformType.QZone);
                break;
            case R.id.cancel_view:
                dismiss();
                break;
            case R.id.download_layout:

                break;
            default:
        }
    }

    private void shareData(ShareManager.PlatformType platformType) {
        ShareData mData = new ShareData();
        Platform.ShareParams params = new Platform.ShareParams();
        params.setShareType(mShareType);
        params.setTitle(mShareTitle);
        params.setTitleUrl(mShareTileUrl);
        params.setSite(mShareSite);
        params.setSiteUrl(mShareSiteUrl);
        params.setText(mShareText);
        params.setImagePath(mSharePhoto);
        params.setUrl(mUrl);
        mData.mPlatformType = platformType;
        mData.mShareParams = params;
        ShareManager.getInstance().shareData(mData, mListener);
    }
}
