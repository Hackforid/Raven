package com.smilehacker.raven.util;

import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import com.smilehacker.raven.R;
import com.smilehacker.raven.model.db.AppInfo;

/**
 * Created by kleist on 14-5-19.
 */
public class TTSHelper {

    private Context mContext;
    private TextToSpeech mTextToSpeech;

    public TTSHelper(Context context) {
        mContext = context;
    }

    public void readNotification(final Notification notification, final String packgaeName) {
        mTextToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    setTTSCompleteAction();

                    AppInfo appInfo = AppInfo.getAppByPackage(packgaeName);
                    if (appInfo != null && appInfo.enable) {

                        String speechText = makeText(packgaeName, notification.tickerText.toString());
                        mTextToSpeech.speak(speechText, TextToSpeech.QUEUE_FLUSH, null);
                    }

                }
            }
        });
    }

    private void setTTSCompleteAction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            mTextToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String s) {
                    DLog.i("start");
                }

                @Override
                public void onDone(String s) {
                    DLog.i("complete");
                    releaseTTS();
                }

                @Override
                public void onError(String s) {

                }
            });
        } else {
            mTextToSpeech.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                @Override
                public void onUtteranceCompleted(String s) {
                    DLog.i("complete");
                    releaseTTS();
                }
            });
        }

    }

    public void releaseTTS() {
        mTextToSpeech.stop();
        mTextToSpeech.shutdown();
    }

    private String makeText(String packageName, String text) {
        String appName = getPackageLabel(packageName);
        String speechText;
        if (appName != null) {
            speechText = String.format(mContext.getString(R.string.speech_text), appName, text);
        } else {
            speechText = String.format(mContext.getString(R.string.speech_text_without_appname), text);
        }

        return speechText;
    }

    private String getPackageLabel(String packageName) {
        PackageManager packageManager = mContext.getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(packageName, 0);
            return packageManager.getApplicationLabel(info.applicationInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public void checkTTS(final OnTTSCheckedListener listener) {
        mTextToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.ERROR) {
                    listener.onTTSChecked(false);
                } else {
                    final int languageResult = mTextToSpeech.isLanguageAvailable(mContext.getResources().getConfiguration().locale);
                    if (languageResult == TextToSpeech.LANG_MISSING_DATA || languageResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        listener.onTTSChecked(false);
                    } else {
                        listener.onTTSChecked(true);
                    }
                }
            }
        });
    }

    public interface OnTTSCheckedListener {
        public abstract void onTTSChecked(Boolean enable);
    }

}
