package com.smilehacker.raven.service;

import android.annotation.TargetApi;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.smilehacker.raven.util.SharedPreferenceManager;
import com.smilehacker.raven.util.TTSHelper;

/**
 * Created by kleist on 14-5-18.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NLService extends NotificationListenerService{

    private TTSHelper mTTSHelper;
    private SharedPreferenceManager mSharedPreferenceManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mTTSHelper = new TTSHelper(this);
        mSharedPreferenceManager = new SharedPreferenceManager(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        if (!mSharedPreferenceManager.getEnable()) {
            return;
        }
        mTTSHelper.readNotification(statusBarNotification.getNotification(), statusBarNotification.getPackageName());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTTSHelper.releaseTTS();
    }
}
