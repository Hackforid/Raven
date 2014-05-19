package com.smilehacker.raven.app;

import android.app.Application;

import com.smilehacker.raven.model.db.AppInfo;

import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Sprinkles;

/**
 * Created by kleist on 14-5-19.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        initDB();
    }

    private void initDB() {
        Sprinkles sprinkles = Sprinkles.init(this);
        Migration migration = new Migration();
        migration.createTable(AppInfo.class);
        sprinkles.addMigration(migration);
    }
}
