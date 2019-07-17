package com.example.myfirestore;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TrackingService extends Service {

    public static final String TAG = TrackingService.class.getSimpleName();
    //============
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> userLatlng = new HashMap<>();

    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildNotification();
        requestLocationUpdates();
    }


    private void buildNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, AppController.CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name)).setContentText("service is running")
                .setContentIntent(pendingIntent).setWhen(System.currentTimeMillis()).setOngoing(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            notification.setSmallIcon(R.drawable.ic_notification_icon).setColor(getResources().getColor(R.color.colorPrimaryDark));
        else notification.setSmallIcon(R.drawable.ic_notification_icon);

        startForeground(1, notification.build());
    }

    private void requestLocationUpdates() {

        LocationRequest request = new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(AppController.UPDATES_INTERVAL);
        request.setFastestInterval(AppController.FASTEST_INTERVAL);

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        client.requestLocationUpdates(request, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {

                    Calendar cal = Calendar.getInstance();
                    Date date=cal.getTime();
                    String formattedDate=dateFormat.format(date);

                    userLatlng.clear();
                    userLatlng.put("Time", formattedDate);
                    userLatlng.put("LatLng", location.getLatitude() + "," + location.getLongitude());
                    db.collection("ehtzm_database").document("latlng").collection("users").add(userLatlng);
                }
            }
        }, null);


    }


}
