package com.example.covidnotifier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidnotifier.utils.LoginState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity  implements VolleyJsonRespondsListener{

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if(!LoginState.restorePrefData(getApplicationContext()))
//        {
//            sendUserToLogin();
//        }

//        boolean alarmUp = (PendingIntent.getBroadcast(this, 0,
//                new Intent(this,AlarmReceiver.class),
//                PendingIntent.FLAG_NO_CREATE) != null);

//        if (alarmUp)
//        {
//            Log.d("myTag", "Alarm is already active");
//        }
//        else
//        {
//            Log.d("myTag", "Alarm is not active");
//            alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//            Intent intent = new Intent(this,AlarmReceiver.class);
//            pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(System.currentTimeMillis());
//            calendar.add(Calendar.MINUTE, 1);
//
////            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000 * 60, pendingIntent);
//            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + (3 * 1000), pendingIntent);
//        }


//            if(!LoginState.getAlarm(getApplicationContext()))
//            {
//                Log.d("myTag", "active alarm");
//                alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//                Intent intent = new Intent(this,AlarmReceiver.class);
//                pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP,1000*3,pendingIntent);
//                LoginState.setAlarm(getApplicationContext(),true);
//            }

        Button buttonTimePicker = findViewById(R.id.button_timepicker);
        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                new generatePictureStyleNotification(MainActivity.this,"title","hey i am ayush","https://api.androidhive.info/images/sample.jpg","hello","12").execute();
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                        (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                Log.d("check", response.toString());
//
//                                try {
//                                    JSONArray sessions =   response.getJSONArray("sessions");
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//
//                            }
//                        }, new Response.ErrorListener() {
//
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                // TODO: Handle error
//                                Log.d("check", error.toString()+" "+error.getMessage());
//
//                            }
//                        }){
//                };

                // Add the request to the RequestQueue.

            }
        });







//        mTextView = findViewById(R.id.textView);
//        Button buttonTimePicker = findViewById(R.id.button_timepicker);
//        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment timePicker = new TimePickerFragment();
//                timePicker.show(getSupportFragmentManager(), "time picker");
//            }
//        });
//        Button buttonCancelAlarm = findViewById(R.id.button_cancel);
//        buttonCancelAlarm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cancelAlarm();
//            }
//        });


    }
    private void sendUserToLogin() {
        Intent homeIntent = new Intent(MainActivity.this, LoginActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }
    public void logOut()
    {
        LoginState.deletePrefsData(getApplicationContext());
        alarmManager.cancel(pendingIntent);
        LoginState.setAlarm(getApplicationContext(),false);
    }

    @Override
    public void onSuccessJson(JSONObject result, String type) {
        switch (type) {
            case "Submit":
                try {
                  JSONArray jsonArray = result.getJSONArray("sessions");
                  for(int i=0;i<jsonArray.length();i++)
                  {
                      JSONObject jsonObject = jsonArray.getJSONObject(i);
                      Log.d("available_capacity",String.valueOf(jsonObject.getString("available_capacity")));
                  }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "AccessData":
                try {
                    JSONArray jsonArray = result.getJSONArray("sessions");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.d("pincode",String.valueOf(jsonObject.getString("pincode")));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailureJson(int responseCode, String responseMessage) {

    }

    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String title, message, imageUrl,click_action,noti_id;

        public generatePictureStyleNotification(Context context, String title, String message, String imageUrl,String click_action,String noti_id ){
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
            this.click_action=click_action;
            this.noti_id=noti_id;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if(result==null)
            {
                Toast.makeText(MainActivity.this,"null",Toast.LENGTH_SHORT).show();
            }

            NotificationManagerCompat notificationManagerCompat;
            notificationManagerCompat = NotificationManagerCompat.from(mContext);

            Intent shareIntent = new Intent(MainActivity.this,AlarmReceiver.class);
            shareIntent.setAction(click_action);
            shareIntent.putExtra("noti_id",noti_id);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0 /* Request code */, shareIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { //after kitkat
                NotificationChannel channel1 = new NotificationChannel(
                        "channel1", "channel1", NotificationManager.IMPORTANCE_HIGH
                );

                channel1.setDescription("ch1");
                NotificationManager manager = getSystemService(NotificationManager.class);
                assert manager != null;
                manager.createNotificationChannel(channel1);


                Notification notificationBuilder = new NotificationCompat.Builder(mContext,"channel1")
                        .setSmallIcon(R.drawable.ic_android_black_24dp)
                        .setColor(getResources().getColor(R.color.colorPrimary))
                        .setContentTitle(title)
                        .setContentText(message)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setAutoCancel(true)
                        .setChannelId("channel1")
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setLargeIcon(result)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(result)
                                .bigLargeIcon(null))
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result))
                        .build();
                notificationBuilder.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManagerCompat.notify(0 /* ID of notification */, notificationBuilder);

                //  sendNotification(title, body, click_action, _id);
            }


        }
    }
}

// https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode=841101&date=17-07-2021