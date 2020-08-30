package com.example.gohotel.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.example.gohotel.model.HotelForm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static int compareFloat(float a, float b) {
        int ta = Math.round(a * 100000);
        int tb = Math.round(b * 100000);
        if (ta > tb) {
            return 1;
        } else if (ta < tb) {
            return -1;
        } else {
            return 0;
        }
    }

    public static String formatCurrencyK(int price) {
        price /= 1000;
        return String.valueOf(price) + "K";
    }

    public static Date convertStringToDate(String sDate, String format) {
        Date date;
        try {
            date = new SimpleDateFormat(format, Locale.ENGLISH).parse(sDate);
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }

    public static String md5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static int dp2px(Context context, float dpValue) {
        if (context == null || compareFloat(0f, dpValue) == 0) return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void drawNinePath(Canvas canvas, Bitmap bmp, Rect rect) {
        NinePatch patch = new NinePatch(bmp, bmp.getNinePatchChunk(), null);
        patch.draw(canvas, rect);
    }

    public static Bitmap drawableToBitmap(int size, Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && bitmap.getHeight() > 0) {
                Matrix matrix = new Matrix();
                float scaleHeight = size * 1.0f / bitmapDrawable.getIntrinsicHeight();
                matrix.postScale(scaleHeight, scaleHeight);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                return bitmap;
            }
        }
        bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static String formatCurrency(int price) {
        String currency;
        DecimalFormat formatter = new DecimalFormat("#,###");
        currency = formatter.format(price);
        return currency;
    }

    public static float convertDpToPixel(float dp, Context context) {
        if (context != null) {
            return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        } else {
            return 0;
        }
    }

    public static boolean isOpenWifi(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        } else {
            return false;
        }
    }

    //ping Google
    public static boolean isInternetWork() {
        try {
            int timeoutMs = 2000;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);
            sock.connect(sockaddr, timeoutMs);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static Location getLocationFromPref(Context context) {
        Location prefLocation = new Location("gps");
        try {
            prefLocation.setLatitude(Double.parseDouble(PreferenceUtils.getLatLocation(context)));
            prefLocation.setLongitude(Double.parseDouble(PreferenceUtils.getLongLocation(context)));
            return prefLocation;
        } catch (Exception e) {
            return null;

        }
    }

    public static String meterToKm(float distance) {
        String distanceStr;
        if (distance > 1000) {
            distanceStr = String.format(Locale.getDefault(), "%.1f", (distance / 1000)) + " km";
        } else {
            distanceStr = Integer.toString((int) distance) + "m";
        }
        return distanceStr;
    }

    public static float calculateDistance(HotelForm hotel, Context context) {
        float distance = 0;
        if (hotel != null) {
            //get hotel location
            Location hotelLocation = new Location("gps");
            hotelLocation.setLatitude(Double.parseDouble(hotel.getLatitude()));
            hotelLocation.setLongitude(Double.parseDouble(hotel.getLongitude()));
            //get current location
            Location currentLocation = Utils.getLocationFromPref(context);
            //calculate distance
            if (currentLocation != null) {
                distance = currentLocation.distanceTo(hotelLocation);
            }
        }
        return distance;
    }
}
