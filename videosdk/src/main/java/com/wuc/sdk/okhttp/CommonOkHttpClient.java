package com.wuc.sdk.okhttp;

import com.wuc.sdk.okhttp.https.HttpsUtils;
import com.wuc.sdk.okhttp.listener.DisposeDataHandle;
import com.wuc.sdk.okhttp.response.CommonJsonCallback;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author: wuchao
 * @date: 2018/12/5 16:08
 * @desciption: 用来发送get，post请求的工具类，包括设置一些请求的公用参数
 */
public class CommonOkHttpClient {

    /**
     * 超时参数
     */
    private static final int TIME_OUT = 30;

    private static OkHttpClient mOkHttpClient;

    //client配置参数
    static {
        //创建client构建者
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        //为构建者填充超时时间
        okHttpBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);

        okHttpBuilder.followRedirects(true);

        //https支持
        okHttpBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        //trust all the https point
        okHttpBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());
        //生成client对象
        mOkHttpClient = okHttpBuilder.build();
    }

    /**
     * 发送具体的http/https请求
     *
     * @param request
     * @param comCallback
     * @return
     */
    public static Call sendRequest(Request request, CommonJsonCallback comCallback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(comCallback);
        return call;
    }

    public static Call get(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    public static Call post(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }
}
