package com.smilehacker.raven.util;

import android.app.Notification;
import android.content.Context;
import android.speech.tts.TextToSpeech;

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
//        NotificationEvent event = new NotificationEvent();
//        event.notification = notification;
//        event.packageName = packgaeName;
//        Intent intent = new Intent(mContext, TTSService.class);
//        intent.putExtra(Constants.KEY_NOTIFICATION, event);
//        mContext.startService(intent);
        mTextToSpeech.speak(notification.tickerText.toString(), TextToSpeech.QUEUE_FLUSH, null);

    }
}
