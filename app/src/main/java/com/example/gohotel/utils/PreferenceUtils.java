package com.example.gohotel.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

    private static String HOTEL_PREFERENCE = "HOTEL_PREFERENCE";

    public static String getLatLocation(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(HOTEL_PREFERENCE, Context.MODE_PRIVATE);
        return prefs.getString("LAT_LOCATION", "");
    }

    public static String getLongLocation(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(HOTEL_PREFERENCE, Context.MODE_PRIVATE);
        return prefs.getString("LONG_LOCATION", "");
    }

    public static void setLatLocation(Context context, String strLocation) {
        final SharedPreferences prefs = context.getSharedPreferences(HOTEL_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("LAT_LOCATION", strLocation);
        editor.apply();
    }

    public static void setLongLocation(Context context, String strLocation) {
        final SharedPreferences prefs = context.getSharedPreferences(HOTEL_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("LONG_LOCATION", strLocation);
        editor.apply();
    }

    public static void setToken(Context context, String token) {
        final SharedPreferences prefs = context.getSharedPreferences(HOTEL_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static String getToken(Context context) {
        try {
            final SharedPreferences prefs = context.getSharedPreferences(HOTEL_PREFERENCE, Context.MODE_PRIVATE);
            if (prefs != null) {
                return prefs.getString("token", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setUserInfo(Context context, String userInfo) {
        final SharedPreferences prefs = context.getSharedPreferences(HOTEL_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userInfo", userInfo);
        editor.apply();
    }

    public static String getUserInfo(Context context) {
        try {
            final SharedPreferences prefs = context.getSharedPreferences(HOTEL_PREFERENCE, Context.MODE_PRIVATE);
            if (prefs != null) {
                return prefs.getString("userInfo", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
