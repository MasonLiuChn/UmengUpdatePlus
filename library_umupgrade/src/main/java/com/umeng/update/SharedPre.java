package com.umeng.update;

/**
 * Created by liumeng on 3/8/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 系统临时保存辅助
 * Mason Liu
 * 2011-5-26上午10:02:17
 */
public class SharedPre {
    public static SharedPre sharedPre;
    private SharedPreferences sharedPreferences;
    private Context context;
    private SharedPreferences.Editor editor;

    public SharedPre(Context con) {
        context = con.getApplicationContext();
        sharedPreferences = context.getSharedPreferences("UmengUpdateShared", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPre getInstance(Context con) {
        if (sharedPre == null) {
            sharedPre = new SharedPre(con);
        }
        return sharedPre;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public boolean set(String key, boolean value) {
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public boolean set(String key, int value) {
        editor.putInt(key, value);
        return editor.commit();
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }
}
