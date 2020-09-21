package com.example.gohotel.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppTimeUtils {
    public final static String ddMMyyyy2 = "dd/MM/yyyy";
    public final static String yyyyMMddHHmmss = "yyyy-MM-dd hh:mm:ss";

    public static String getTomorrowDay(SimpleDateFormat dateFormatter) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        return dateFormatter.format(cal.getTime());
    }

    public static String getSystemDay(SimpleDateFormat dateFormatter) {
        Date date = new Date();
        return dateFormatter.format(date.getTime());
    }

    public static String changeDateUpToServer(String date) {
        try {
            String[] time = date.split("/");
            return time[2] + "-" + time[1] + "-" + time[0];
        } catch (Exception e) {
            return date;
        }
    }

    public static String changeDateShowClient(String date) {
        try {
            String[] time = date.split("-");
            return time[0] + "/" + time[1] + "/" + time[2];
        } catch (Exception e) {
            return date;
        }
    }

    public static String changeTimeShowClient(String date) {
        try {
            String[] time = date.split("-");
            String[] day=time[2].split(" ");
            return time[0] + "/" + time[1] + "/" + day[0]+" "+day[1];
        } catch (Exception e) {
            return date;
        }
    }
}
