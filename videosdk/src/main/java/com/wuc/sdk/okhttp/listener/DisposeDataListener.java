package com.wuc.sdk.okhttp.listener;

/**
 * @author: wuchao
 * @date: 2018/12/5 17:29
 * @desciption: 自定义事件监听
 */
public interface DisposeDataListener {
    /**
     * 请求成功回调事件处理
     *
     * @param responseObj
     */
    public void onSuccess(Object responseObj);

    /**
     * 请求失败回调事件处理
     *
     * @param reasonObj
     */
    public void onFailure(Object reasonObj);
}
