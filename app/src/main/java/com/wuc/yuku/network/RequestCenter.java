package com.wuc.yuku.network;

import com.wuc.sdk.okhttp.CommonOkHttpClient;
import com.wuc.sdk.okhttp.listener.DisposeDataHandle;
import com.wuc.sdk.okhttp.listener.DisposeDataListener;
import com.wuc.sdk.okhttp.request.CommonRequest;
import com.wuc.sdk.okhttp.request.RequestParams;
import com.wuc.yuku.module.course.BaseCourseModel;
import com.wuc.yuku.module.recommend.BaseRecommendModel;
import com.wuc.yuku.module.update.UpdateModel;
import com.wuc.yuku.module.user.User;

/**
 * @author: wuchao
 * @date: 2018/12/6 15:27
 * @desciption: 存放应用中的所有请求
 */
public class RequestCenter {
    /**
     * 首页产品
     *
     * @param listener
     */
    public static void requestRecommendData(DisposeDataListener listener) {
        RequestCenter.getRequest(HttpConstants.HOME_RECOMMEND, null, listener, BaseRecommendModel.class);
    }

    /**
     * 根据参数发送所有的get请求
     *
     * @param url
     * @param params
     * @param listener
     * @param clazz
     */
    public static void getRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

    /**
     * 应用版本号请求
     *
     * @param listener
     */
    public static void checkVersion(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.CHECK_UPDATE, null, listener, UpdateModel.class);
    }

    /**
     * 根据参数发送所有的post请求
     *
     * @param url
     * @param params
     * @param listener
     * @param clazz
     */
    public static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

    /**
     * 用户登录请求
     */
    public static void login(String userName, String password, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("mb", userName);
        params.put("pwd", password);
        RequestCenter.postRequest(HttpConstants.LOGIN, params, listener, User.class);
    }

    /**
     * 请求课程详情
     *
     * @param listener
     */
    public static void requestCourseDetail(String courseId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("courseId", courseId);
        RequestCenter.postRequest(HttpConstants.COURSE_DETAIL, params, listener, BaseCourseModel.class);
    }
}
