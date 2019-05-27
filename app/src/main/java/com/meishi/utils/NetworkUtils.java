package com.meishi.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 获取网络状态的封装类
 */
public class NetworkUtils {

    public static boolean isNetwork(Context context){
        ConnectivityManager manager = (ConnectivityManager)context
                .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);//获取系统服务

        if(manager == null){
            return false;
        }

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return !(networkInfo == null || !networkInfo.isAvailable());
    }

    /**
     * 获取网络连接类型
     * @param context
     * @return
     */
    public static int getConnectedType(Context context){
        if(context != null){
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if(mNetworkInfo != null && mNetworkInfo.isAvailable()){
                return mNetworkInfo.getType();
            }
        }

        return -1;
    }

    /**
     * 判断是否时移动数据上网
     */
    public static boolean isMobileConnected(Context context){
        if(context != null){
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null){
                return mMobileNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    public static boolean isWifiConnected(Context context){
        if(context != null){
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(mWifiNetworkInfo != null){
                return mWifiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isNetworkConnected(Context context){
        if(context != null){
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (networkInfo != null){
                return networkInfo.isAvailable();
            }
        }
        return false;
    }
}
