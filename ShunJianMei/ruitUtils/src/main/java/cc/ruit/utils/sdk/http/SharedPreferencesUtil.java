package cc.ruit.utils.sdk.http;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
	private static String SP_NAME = "msg_push";
    private static SharedPreferences preferences;
    
    /**
     * 获取SharedPreference对象
     * 
     * @param context
     * @return
     */
    private synchronized static SharedPreferences getSharedPreferences(Context context) {
        if (null == preferences) {
            preferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return preferences;
    }

    /**
     * 根据key将字符串value保存
     * 
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        try {
            getSharedPreferences(context).edit().putString(key, value).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据key从SharedPreference获取value
     * 
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Context context, String key,
            String defaultValue) {
        SharedPreferences shared = getSharedPreferences(context);
        if (null != shared) {
            return shared.getString(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * 根据key将字符串value保存
     * 
     * @param context
     * @param key
     * @param value
     */
    public static void putLong(Context context, String key, long value) {
        try {
            getSharedPreferences(context).edit().putLong(key, value).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据key从SharedPreference获取value
     * 
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences shared = getSharedPreferences(context);
        if (null != shared) {
            return shared.getLong(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * 根据key将value保存
     * 
     * @param context
     * @param key
     * @param value
     */
    public static void putInt(Context context, String key, int value) {
        getSharedPreferences(context).edit().putInt(key, value).commit();
    }

    /**
     * 根据key从SharedPreference获取value
     * 
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences shared = getSharedPreferences(context);
        if (null != shared) {
            return shared.getInt(key, defaultValue);
        }
        return defaultValue;
    }

    public static void putFloat(Context context, String key, float value) {
        getSharedPreferences(context).edit().putFloat(key, value).commit();
    }

    /**
     * 根据key从SharedPreference获取value
     * 
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static float getFloat(Context context, String key, int defaultValue) {
        SharedPreferences shared = getSharedPreferences(context);
        if (null != shared) {
            return shared.getFloat(key, defaultValue);
        }
        return defaultValue;
    }
    /**
     * @Description: 添加boolean
     * @author liliwei
     * @create 2013-11-29 下午2:21:20
     * @updateTime 2013-11-29 下午2:21:20
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context,String key,boolean value){
        getSharedPreferences(context).edit().putBoolean(key, value).commit();
    }
    /**
     * @Description: 获取boolean
     * @author liliwei
     * @create 2013-11-29 下午2:21:23
     * @updateTime 2013-11-29 下午2:21:23
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Context context,String key,boolean defaultValue){
        SharedPreferences shared = getSharedPreferences(context);
        if (null != shared) {
            return shared.getBoolean(key, defaultValue);
        }
        return defaultValue;
    }
    

}
