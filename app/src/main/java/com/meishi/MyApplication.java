package com.meishi;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();

    }


    public static Context getContext(){
        return context;
    }

    public static void writeToSP(String key,String value){
        SharedPreferences sp = context.getSharedPreferences("meishi",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        boolean result = editor.commit();
        Log.d("MyApplication", "writeToSP: "+result+"  value:"+value);
    }

    public static String readFromSp(String key){
        SharedPreferences sp = context.getSharedPreferences("meishi",Context.MODE_PRIVATE);
        String token = sp.getString(AppConstants.TOKEN,null);
        return token;
    }

}
