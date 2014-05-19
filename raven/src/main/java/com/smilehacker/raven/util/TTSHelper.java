package com.smilehacker.raven.util;

import android.app.Notification;
import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.smilehacker.raven.model.db.AppInfo;

/**
 * Created by kleist on 14-5-19.
 */
public class TTSHelper {

    private Context mContext;
    private TextToSpeech mTextToSpeech;

    public TTSHelper(Context context) {
        mContext = context;

        mTextToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });
    }

    public void readNotification(Notification notification, String packgaeName) {
        AppInfo appInfo = AppInfo.getAppByPackage(packgaeName);
        if (appInfo != null && appInfo.enable) {
            mTextToSpeech.speak(notification.tickerText.toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
