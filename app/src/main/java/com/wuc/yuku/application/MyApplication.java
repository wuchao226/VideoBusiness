package com.wuc.yuku.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.wuc.yuku.share.ShareManager;

import cn.jpush.android.api.JPushInterface;

/**
 * @author: wuchao
 * @date: 2018/12/4 19:21
 * @desciption: 1、整个程序的入口，2、初始化工作，3、为整个应用的其它模块提供上下文
 */
public class MyApplication extends MultiDexApplication {

    private static MyApplication mApplication = null;

    public static MyApplication getInstance() {
        return mApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        Logger.addLogAdapter(new AndroidLogAdapter());
        initShareSDK();
        initJPush();
    }

    /**
     * 初始化ShareSDK
     */
    private void initShareSDK() {
        ShareManager.initSDK(this);
    }

    /**
     * 初始化JPush
     */
    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
