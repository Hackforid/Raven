package com.smilehacker.raven.service;

import android.annotation.TargetApi;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.smilehacker.raven.util.TTSHelper;

/**
 * Created by kleist on 14-5-18.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NLService extends NotificationListenerService{

    private TTSHelper mTTSHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mTTSHelper = new TTSHelper(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        mTTSHelper.readNotification(statusBarNotification.getNotification(), statusBarNotification.getPackageName());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
    }
}
