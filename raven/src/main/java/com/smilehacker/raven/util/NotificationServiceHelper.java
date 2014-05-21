package com.smilehacker.raven.util;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import com.smilehacker.raven.service.NotificationAccessibilityService;

import java.util.List;

/**
 * Created by kleist on 14-5-21.
 */
public class NotificationServiceHelper {

    private Context mContext;

    public NotificationServiceHelper(Context context) {
        mContext = context;
    }

    public Boolean isNotificationServiceEnable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return isNotificationListenerServiceEnable();
        } else {
            return isNotificationAccessibilityServiceEnable();
        }
    }


    public Boolean isNotificationAccessibilityServiceEnable() {
        String serviceName = String.format("$1%s/$2%s", mContext.getPackageName(), NotificationAccessibilityService.class.getName());
        return isAccessibilityEnabled(mContext, serviceName);
    }

    public Boolean isNotificationListenerServiceEnable() {
        return  isNotificationListenerServiceEnable(mContext);
    }

    private boolean isAccessibilityEnabled(Context context, String id) {

        AccessibilityManager am = (AccessibilityManager) context
                .getSystemService(Context.ACCESSIBILITY_SERVICE);

        List<AccessibilityServiceInfo> runningServices = am
                .getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
        for (AccessibilityServiceInfo service : runningServices) {
            if (id.equals(service.getId())) {
                return true;
            }
        }

        return false;
    }

    private boolean isNotificationListenerServiceEnable(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = context.getPackageName();

        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
    }
}
