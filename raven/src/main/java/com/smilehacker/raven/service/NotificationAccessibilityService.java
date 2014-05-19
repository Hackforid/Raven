package com.smilehacker.raven.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.os.Parcelable;
import android.view.accessibility.AccessibilityEvent;

import com.smilehacker.raven.util.TTSHelper;

/**
 * Created by kleist on 14-5-18.
 */
public class NotificationAccessibilityService extends AccessibilityService {

    private TTSHelper mTTSHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mTTSHelper = new TTSHelper(this);
    }

    protected void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_AUDIBLE;
        info.notificationTimeout = 100;
        setServiceInfo(info);
    }

    public void onAccessibilityEvent(AccessibilityEvent e) {
        if (e.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            Parcelable data = e.getParcelableData();
            if (data instanceof Notification) {
                mTTSHelper.readNotification((Notification) data, e.getPackageName().toString());
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
