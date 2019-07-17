package com.example.myfirestore;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.multidex.MultiDexApplication;

public class AppController extends MultiDexApplication {

    public static final String CHANNEL_ID = "serviceChannel";

    public static final long UPDATES_INTERVAL = 60 * 1000 * 1; // 60 secs
    public static final long FASTEST_INTERVAL = 30 * 1000 * 1; // 30 secs

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    /*============================================================================================*/

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}
