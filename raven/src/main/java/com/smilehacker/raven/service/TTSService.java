package com.smilehacker.raven.service;

import android.app.IntentService;
import android.content.Intent;
import android.speech.tts.TextToSpeech;

import com.smilehacker.raven.app.Constants;
import com.smilehacker.raven.model.event.NotificationEvent;
import com.smilehacker.raven.util.DLog;

/**
 * Created by kleist on 14-5-19.
 */
public class TTSService extends IntentService {


    public TTSService() {
        super("TTS");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationEvent event = intent.getParcelableExtra(Constants.KEY_NOTIFICATION);
        DLog.i("packgae = " + event.packageName);
        DLog.i("text = " + event.notification.tickerText);

        read(event.notification.tickerText.toString());
    }

    private void read(String text) {
        TextToSpeech tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
