package com.smilehacker.raven.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.Settings;

import com.smilehacker.raven.R;
import com.smilehacker.raven.util.NotificationServiceHelper;

/**
 * Created by kleist on 14-5-21.
 */
public class SettingActivity extends PreferenceActivity {

    private final static int REQUEST_CODE_SERVICE = 100;

    private Preference mPrefService;

    private NotificationServiceHelper mNotificationServiceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.xml.setting_preference);

        mNotificationServiceHelper = new NotificationServiceHelper(this);

        initPreference();
    }

    private void initPreference() {
        mPrefService = findPreference(getString(R.string.setting_key_service));

        setServicePreference();
    }

    private void setServicePreference() {
        setServicePreferenceSummary();
        mPrefService.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), REQUEST_CODE_SERVICE);
                } else {
                    startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), REQUEST_CODE_SERVICE);
                }
                return true;
            }
        });
    }

    private void setServicePreferenceSummary() {
        if (mNotificationServiceHelper.isNotificationServiceEnable()) {
            mPrefService.setSummary(R.string.setting_summary_service_enable);
        } else {
            mPrefService.setSummary(R.string.setting_summary_service_diable);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SERVICE) {
            setServicePreferenceSummary();
        }
    }
}
