package com.smilehacker.raven.model.db;

import java.util.List;

import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Model;
import se.emilsjolander.sprinkles.Query;
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


    public static List<AppInfo> getAll() {
        CursorList<AppInfo> appInfos = Query.all(AppInfo.class).get();
        List<AppInfo> list = appInfos.asList();
        appInfos.close();
        return  list;
    }

    public static AppInfo getAppByPackage(String packageName) {
        return Query.one(AppInfo.class, "SELECT * FROM app WHERE package_name = ?", packageName).get();
    }
}
