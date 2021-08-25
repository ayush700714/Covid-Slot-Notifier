package com.example.covidnotifier;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "hello ayush", Toast.LENGTH_SHORT).show();
//        Log.d("on recieve", "onReceive:");
//
//
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference =firebaseDatabase.getReference().child("Users").child("6306882685").getRef();
//        databaseReference.child("phone").setValue("ayush");
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.CAMPAIGN_DETAILS, null);
//        setAlarm(context);
    }
    public void setAlarm(Context context)
    {
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+1000*3,pendingIntent);
    }
}
