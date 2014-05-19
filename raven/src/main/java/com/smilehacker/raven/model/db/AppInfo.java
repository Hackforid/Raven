package com.smilehacker.raven.model.db;

import android.app.Notification;

import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.annotations.Column;
import se.emilsjolander.sprinkles.annotations.PrimaryKey;
import se.emilsjolander.sprinkles.annotations.Table;

/**
 * Created by kleist on 14-5-19.
 */
@Table("app")
public class AppInfo extends Model {

    @PrimaryKey
    @Column("package_name")
    public String packageName;

    @Column("enable")
    public Boolean enable = false;

    public String appName;
    public Notification notification;
}
