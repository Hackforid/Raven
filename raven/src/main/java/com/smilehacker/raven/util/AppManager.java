package com.smilehacker.raven.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.smilehacker.raven.model.db.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kleist on 14-5-19.
 */
public class AppManager {

    private Context mContext;
    private PackageManager mPackageManager;

    public AppManager(Context context) {
        mContext = context;
        mPackageManager = context.getPackageManager();
    }

    public List<AppInfo> loadApps() {
        List<AppInfo> appInfos = loadAppsFromSys();
        List<AppInfo> appInDB = loadAppsFromDB();

        for (int i = 0, length = appInfos.size(); i < length; i++) {
            AppInfo appInfo = appInfos.get(i);
            for (AppInfo app: appInDB) {
                if (app.packageName.equals(appInfo.packageName)) {
                    app.appName = appInfo.appName;
                    appInfos.set(i, app);
                }
            }
        }

        sortListByEnableState(appInfos);
        return appInfos;
    }

    private List<AppInfo> loadAppsFromSys() {
        List<AppInfo> list = new ArrayList<AppInfo>();
        List<PackageInfo> packages = mPackageManager.getInstalledPackages(PackageManager.PERMISSION_GRANTED);

        for (PackageInfo pkg : packages) {
            if (!isLaunchable(pkg)) {
                continue;
            }
            AppInfo appInfo = new AppInfo();
            appInfo.appName = mPackageManager.getApplicationLabel(pkg.applicationInfo).toString();
            appInfo.packageName = pkg.packageName;
            list.add(appInfo);
        }

        return list;
    }

    private List<AppInfo> loadAppsFromDB() {
        return AppInfo.getAll();
    }

    private void sortListByEnableState(List<AppInfo> appInfos) {
        int pos = 0;
        AppInfo tmpAppInfo = appInfos.get(pos);
        for (int i = 0, size = appInfos.size(); i < size; i++) {
            AppInfo appInfo = appInfos.get(i);
            if (appInfo.enable) {
                appInfos.set(pos, appInfo);
                appInfos.set(i, tmpAppInfo);
                pos++;
                tmpAppInfo = appInfos.get(pos);
            }
        }
    }


    private Boolean isLaunchable(PackageInfo packageInfo) {
        return  mPackageManager.getLaunchIntentForPackage(packageInfo.packageName) != null;
    }
}
