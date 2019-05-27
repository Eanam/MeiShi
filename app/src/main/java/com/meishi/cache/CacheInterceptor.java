package com.meishi.cache;

import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created on 2016/12/4.
 *
 * @author WangYi
 */

public class CacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        boolean netAvailable = NetworkUtils.isNetwork(MyApplication.getContext());

        if (netAvailable) {
            request = request.newBuilder()
                    //网络可用 强制从网络获取数据
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();
        } else {
            request = request.newBuilder()
                    //网络不可用 从缓存获取
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (netAvailable) {
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    // 有网络时 设置缓存超时时间
                    .header("Cache-Control", "public, max-age=" + AppConstants.MAXAGE)
                    .build();
        } else {
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    // 无网络时，设置超时
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + AppConstants.MAXSTALE)
                    .build();
        }
        return response;
    }
}
