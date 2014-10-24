package com.smilehacker.raven.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smilehacker.raven.R;
import com.smilehacker.raven.adapter.AppGridViewAdapter;
import com.smilehacker.raven.model.db.AppInfo;
import com.smilehacker.raven.util.AppManager;
import com.smilehacker.raven.util.NotificationServiceHelper;
import com.smilehacker.raven.util.SharedPreferenceManager;
import com.smilehacker.raven.util.TTSHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.actionbar)
    Toolbar mActionbar;
    @InjectView(R.id.gv_apps)
    GridView mGvApps;
    @InjectView(R.id.pb_loading)
    ProgressBar mPbLoading;

    private Switch mSwEnable;
    private SharedPreferenceManager mSharedPreferenceManager;

    private AppGridViewAdapter mAppGridViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.primary_red));
        }

        setSupportActionBar(mActionbar);
        mSharedPreferenceManager = new SharedPreferenceManager(this);

        initView();
        initActionBar();
        checkService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mAppGridViewAdapter = new AppGridViewAdapter(this, new ArrayList<AppInfo>());
        mGvApps.setAdapter(mAppGridViewAdapter);
    }

    private void initActionBar() {
        mActionbar.setTitle("Raven");
        mSwEnable = new Switch(this);


//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setCustomView(R.layout.v_home_actionbar);
//        actionBar.setDisplayShowCustomEnabled(true);
//        mSwEnable = (Switch) actionBar.getCustomView().findViewById(R.id.sw_enable);
        mSwEnable.setChecked(mSharedPreferenceManager.getEnable());
        mSwEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mSharedPreferenceManager.setEnable(b);
            }
        });

        mActionbar.addView(mSwEnable);
    }

    private void checkService() {
        NotificationServiceHelper helper = new NotificationServiceHelper(this);
        if (helper.isNotificationServiceEnable()) {
            checkTTS();
        } else {
            showEnableServiceDialog();
        }
    }

    private void checkTTS() {
        TTSHelper helper = TTSHelper.getInstance(this);
        helper.checkTTS(new TTSHelper.OnTTSCheckedListener() {
            @Override
            public void onTTSChecked(Boolean enable) {
                if (enable) {
                    showApps();
                } else {
                    showSetTTSDialog();
                }
            }
        });
    }

    private void showSetTTSDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_set_tts_title);
        builder.setMessage(R.string.dialog_set_tts_msg);
        builder.setPositiveButton(R.string.dialog_set_tts_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent();
                intent.setAction("com.android.settings.TTS_SETTINGS");
                startActivity(intent);
                showApps();
            }
        });
        builder.setNegativeButton(R.string.dialog_set_tts_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                showApps();
            }
        });
        builder.create().show();
    }

    private void showEnableServiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_enable_service_title);
        builder.setMessage(R.string.dialog_enable_service_msg);
        builder.setPositiveButton(R.string.dialog_enable_service_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                } else {
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                }

                dialogInterface.dismiss();
                checkTTS();
            }
        });

        builder.setNegativeButton(R.string.dialog_enable_service_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                checkTTS();
            }
        });

        builder.create().show();
    }

    private void showApps() {
        AsyncTask<Void, Void, List<AppInfo>> loadAppTask = new AsyncTask<Void, Void, List<AppInfo>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mGvApps.setVisibility(View.GONE);
                mPbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<AppInfo> doInBackground(Void... voids) {
                AppManager appManager = new AppManager(MainActivity.this);
                return appManager.loadApps();
            }

            @Override
            protected void onPostExecute(List<AppInfo> appInfos) {
                super.onPostExecute(appInfos);
                mAppGridViewAdapter.flush(appInfos);
                mPbLoading.setVisibility(View.GONE);
                mGvApps.setVisibility(View.VISIBLE);
            }
        };

        loadAppTask.execute();
    }

}
