package com.example.covidnotifier.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class LoginState {

    static public void savePrefsData(Context context,String phone) {
        SharedPreferences pref = context.getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isUserVerified",true);
        editor.putString("phone",phone);
        editor.apply();
    }

     static public boolean restorePrefData(Context context) {
        SharedPreferences pref = context.getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isUserVerified = pref.getBoolean("isUserVerified",false);
        return  isUserVerified;
    }
    public static void deletePrefsData(Context context) {
        SharedPreferences pref = context.getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isUserVerified",false);
        editor.putString("phone","1223");
        editor.apply();
    }
    static public String getPhone(Context context) {
        SharedPreferences pref = context.getSharedPreferences("myPrefs",MODE_PRIVATE);
        String phone = pref.getString("phone","0123");
        return  phone;
    }

    static public void setAlarm(Context context,boolean value)
    {
        SharedPreferences pref = context.getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isAlarmStarted",value);
        editor.apply();
    }
    static public boolean getAlarm(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences("myPrefs",MODE_PRIVATE);
        boolean isAlarmStarted = pref.getBoolean("isAlarmStarted",false);
        return isAlarmStarted;
    }
}
