package com.smilehacker.raven.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.smilehacker.raven.R;
import com.smilehacker.raven.adapter.AppGridViewAdapter;
import com.smilehacker.raven.model.db.AppInfo;
import com.smilehacker.raven.util.AppManager;
import com.smilehacker.raven.util.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private GridView mGvApps;
    private ProgressBar mPbLoading;
    private Switch mSwEnable;
    private SharedPreferenceManager mSharedPreferenceManager;

    private AppGridViewAdapter mAppGridViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mSettingButton = (Button) findViewById(R.id.btn_setting);
//
//        mSettingButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
//                } else {
//                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
//                }
//            }
//        });

        mSharedPreferenceManager = new SharedPreferenceManager(this);

        initView();
        initActionBar();
        showApps();
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

    private void initView() {
        mGvApps = (GridView) findViewById(R.id.gv_apps);
        mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);

        mAppGridViewAdapter = new AppGridViewAdapter(this, new ArrayList<AppInfo>());
        mGvApps.setAdapter(mAppGridViewAdapter);
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.v_home_actionbar);
        actionBar.setDisplayShowCustomEnabled(true);
        mSwEnable = (Switch) actionBar.getCustomView().findViewById(R.id.sw_enable);
        mSwEnable.setChecked(mSharedPreferenceManager.getEnable());
        mSwEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mSharedPreferenceManager.setEnable(b);
            }
        });
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
