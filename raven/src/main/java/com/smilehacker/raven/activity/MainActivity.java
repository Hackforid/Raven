package com.smilehacker.raven.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.smilehacker.raven.R;
import com.smilehacker.raven.model.event.NotificationEvent;
import com.smilehacker.raven.util.DLog;

import de.greenrobot.event.EventBus;


public class MainActivity extends Activity {

    private Button mSettingButton;

    private EventBus mEventBus;
    private TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEventBus = EventBus.getDefault();
        mEventBus.register(this);

        mSettingButton = (Button) findViewById(R.id.btn_setting);

        mSettingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                } else {
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                }
            }
        });

    }

    private void initTTS() {
        mTTS = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    //mTTS.setLanguage(Locale.CHINA);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }


    @SuppressWarnings("UnusedDeclaration")
    public void onEvent(NotificationEvent event) {
        DLog.i("receive notification");
        DLog.i("from: " + event.packageName);
        DLog.i("ticker text: " + event.notification.tickerText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void speech(final String text) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                return null;
            }
        }.execute();
    }

}
