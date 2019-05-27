package com.meishi.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
    public static String getStringTimeNow(){
        Calendar calendar = Calendar.getInstance();
        Long millis = calendar.getTimeInMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区

        return simpleDateFormat.format(new Date(millis));
    }

    public static String getStringTime(long millis){
        Date date = new Date(millis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区
        System.out.println(simpleDateFormat.format(date));
        Log.d("TimeUtils", "getStringDate: "+date+" "+simpleDateFormat.format(date)+" "+millis);
        return simpleDateFormat.format(date);
    }

    public static String getStringDate(long millis){
        Date date = new Date(millis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区
        System.out.println(simpleDateFormat.format(date));

        return simpleDateFormat.format(date);
    }
}
