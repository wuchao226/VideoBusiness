package com.wuc.sdk.okhttp.listener;

/**
 * @author: wuchao
 * @date: 2018/12/5 17:31
 * @desciption:
 */
public class DisposeDataHandle {
    public DisposeDataListener mListener = null;
    public Class<?> mClass = null;
    public String mSource = null;

    public DisposeDataHandle(DisposeDataListener listener) {
        mListener = listener;
    }

    public DisposeDataHandle(DisposeDataListener listener, Class<?> aClass) {
        mListener = listener;
        mClass = aClass;
    }

    public DisposeDataHandle(DisposeDataListener listener, String source) {
        mListener = listener;
        mSource = source;
    }
}
