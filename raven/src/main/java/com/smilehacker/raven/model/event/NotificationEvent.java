package com.smilehacker.raven.model.event;

import android.app.Notification;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kleist on 14-5-18.
 */
public class NotificationEvent implements Parcelable {

    public Notification notification;
    public String packageName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.notification, 0);
        dest.writeString(this.packageName);
    }

    public NotificationEvent() {
    }

    private NotificationEvent(Parcel in) {
        this.notification = in.readParcelable(Notification.class.getClassLoader());
        this.packageName = in.readString();
    }

    public static Parcelable.Creator<NotificationEvent> CREATOR = new Parcelable.Creator<NotificationEvent>() {
        public NotificationEvent createFromParcel(Parcel source) {
            return new NotificationEvent(source);
        }

        public NotificationEvent[] newArray(int size) {
            return new NotificationEvent[size];
        }
    };
}
