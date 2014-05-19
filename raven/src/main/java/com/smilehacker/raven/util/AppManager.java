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

    private Boolean isLaunchable(PackageInfo packageInfo) {
        return  mPackageManager.getLaunchIntentForPackage(packageInfo.packageName) != null;
    }
}
