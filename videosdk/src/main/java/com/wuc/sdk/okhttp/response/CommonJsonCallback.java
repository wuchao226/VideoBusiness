package com.wuc.sdk.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.wuc.sdk.okhttp.execption.OkHttpException;
import com.wuc.sdk.okhttp.listener.DisposeDataHandle;
import com.wuc.sdk.okhttp.listener.DisposeDataListener;
import com.wuc.sdk.utils.ResponseEntityToModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author: wuchao
 * @date: 2018/12/5 22:26
 * @desciption: 处理json回调
 */
public class CommonJsonCallback implements Callback {

    /**
     * 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
     */
    protected final String RESULT_CODE = "ecode";
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";
    protected final String COOKIE_STORE = "Set-Cookie";
    /**
     * 自定义异常类型
     * the network relative error
     */
    protected final int NETWORK_ERROR = -1;
    /**
     * the JSON relative error
     */
    protected final int JSON_ERROR = -2;
    /**
     * the unknow error
     */
    protected final int OTHER_ERROR = -3;
    /**
     * 进行消息的转发
     */
    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;

    public CommonJsonCallback(DisposeDataHandle handle) {
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException ioexception) {
        Log.e("IOException", "IOException：" + ioexception.getMessage());
        //此时还在非UI线程，因此要转发
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onFailure(new OkHttpException(NETWORK_ERROR, ioexception));
                }
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    /**
     * 处理服务器响应数据
     */
    private void handleResponse(Object responseObj) {
        if (responseObj == null || "".equals(responseObj.toString().trim())) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        try {
            JSONObject result = new JSONObject(responseObj.toString());
            if (mClass == null) {
                mListener.onSuccess(result);
            } else {
                //将json对象转为实体对象
                Object obj = ResponseEntityToModule.parseJsonObjectToModule(result, mClass);
                if (obj != null) {
                    mListener.onSuccess(obj);
                } else {
                    mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                }
            }
        } catch (JSONException e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
            e.printStackTrace();
        }
    }
}
