package com.wuc.yuku.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.wuc.yuku.application.MyApplication;

/**
 * @author: wuchao
 * @date: 2018/12/25 16:45
 * @desciption: SP 工具类
 */
public class SpUtils {

    /**
     * 上次基金更新时间
     */
    public static final String LAST_UPDATE_PRODUCT = "last_update_product";
    public static final String VIDEO_PLAY_SETTING = "video_play_setting";
    public static final String IS_SHOW_GUIDE = "is_show_guide";
    /**
     * Preference文件名
     */
    private static final String SHARE_PREFREENCE_NAME = "imooc.pre";
    /**
     * 提示：
     * Activity.getPreferences(int model)生成 Activity名.xml  用于Activity内部存储
     * PreferenceManager.getDefaultSharedPreferences(Context)生成 包名_preferences.xml
     * Context.getSharedPreferences(String name,int model)生成name.xml
     */
    private static SharedPreferences mPreferences =
            MyApplication.getInstance().getSharedPreferences(SHARE_PREFREENCE_NAME, Context.MODE_PRIVATE);

    private static SharedPreferences.Editor mEditor = mPreferences.edit();

    /**
     * 向SP存入指定key对应的数据
     * 其中value可以是String、boolean、float、int、long等各种基本类型的值
     *
     * @param key
     * @param value
     */
    public static void putString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public static void putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public static void putFloat(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    public static void putInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public static void putLong(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    /**
     * 清空SP里所以数据
     */
    public static void clear() {
        mEditor.clear();
        mEditor.commit();
    }

    /**
     * 删除SP里指定key对应的数据项
     *
     * @param key
     */
    public static void remove(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }

    /**
     * 获取SP数据里指定key对应的value。如果key不存在，则返回默认值defValue。
     *
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(String key, String defValue) {
        return mPreferences.getString(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return mPreferences.getBoolean(key, defValue);
    }

    public static float getFloat(String key, float defValue) {
        return mPreferences.getFloat(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return mPreferences.getInt(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return mPreferences.getLong(key, defValue);
    }

    /**
     * 判断SP是否包含特定key的数据
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        return mPreferences.contains(key);
    }
}
