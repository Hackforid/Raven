package com.smilehacker.raven.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kleist on 14-5-20.
 */
public class SharedPreferenceManager {

    private final static String SHARED_CONFIG = "shared_config";
    private final static String SHARED_CONFIG_ENABLE = "shared_config_enable";

    private SharedPreferences mSharedPreferences;

    public SharedPreferenceManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_CONFIG, 0);
    }

    public void setEnable(Boolean enable) {
        mSharedPreferences.edit().putBoolean(SHARED_CONFIG_ENABLE, enable).commit();
    }

    public Boolean getEnable() {
        return mSharedPreferences.getBoolean(SHARED_CONFIG_ENABLE, true);
    }
}
