package com.wuc.sdk.core;

/**
 * @author: wuchao
 * @date: 2018/12/21 16:01
 * @desciption: 最终通知应用层广告是否成功
 */
public interface VideoManagerInterface {

    void onVideoSuccess();

    void onVideoFailed();

    void onClickVideo(String url);
}
