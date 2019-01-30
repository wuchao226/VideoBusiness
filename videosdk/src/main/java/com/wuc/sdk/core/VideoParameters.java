package com.wuc.sdk.core;

import com.wuc.sdk.constant.SDKConstant.AutoPlaySetting;

/**
 * @author: wuchao
 * @date: 2018/12/21 13:36
 * @desciption: SDK全局参数配置, 都用静态来保证统一性
 */
public class VideoParameters {
    /**
     * 用来记录可自动播放的条件，默认可以自动播放
     */
    private static AutoPlaySetting currentSetting = AutoPlaySetting.AUTO_PLAY_3G_4G_WIFI;

    public static AutoPlaySetting getCurrentSetting() {
        return currentSetting;
    }

    public static void setCurrentSetting(AutoPlaySetting setting) {
        currentSetting = setting;
    }
}
