package com.meishi.helpers;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.meishi.AppConstants;
import com.meishi.cache.EnhancedCacheInterceptor;
import com.meishi.utils.FileUtils;
import com.meishi.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * retrofit配置的client
 * 主要是添加拦截器，来实现缓存：
 *      有网时从网络获取数据，没网时调用本地缓存
 * 设计模式：单例模式
 */
public class OkHttpClientHelper {
    private OkHttpClient okHttpClient;
    private static Context sContext;

    //网络拦截器：用于更新缓存
    private final Interceptor REWRITE_RESPONSE_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();//原始的请求
            Response response = chain.proceed(request);//原始的响应
            String serverCache = response.header("Cache-Control");//获取服务器对响应的缓存设置
            if(TextUtils.isEmpty(serverCache)){//如果未设置响应缓存机制，则拦截器帮我们设置
                String cacheControl = request.cacheControl().toString();//获取我们请求中对缓存的要求
                if(TextUtils.isEmpty(cacheControl)){//如果未设置，则拦截器帮我们设置
                    return response.newBuilder()
                            .removeHeader("Pragma")//清除头信息
                            .removeHeader("Cache-Control")
                            .header("Cache-Control","public,max-age="+ AppConstants.MAXAGE)
                            .build();

                }else{//否则，直接使用request中设置好的缓存机制既可
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .addHeader("Cache-Control",cacheControl)
                            .build();
                }
            }
            return response;
        }
    };

    //自定义拦截器：在断网时，从缓存中读取数据
    private final Interceptor REWRITE_RESPONSE_INTERCEPTOR_OFFLINE = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();//原始请求
            if(!NetworkUtils.isNetwork(sContext)){//系统处于断网状态,构建新的request
                Log.d("OFFLINE", "intercept: OFFLINE");
                int maxStale = AppConstants.MAXSTALE;
                CacheControl tempCacheControl = new CacheControl.Builder()
                        .onlyIfCached()//设置从缓存中读取数据
                        .maxStale(maxStale, TimeUnit.SECONDS)
                        .build();
                request = request.newBuilder()
                        .cacheControl(tempCacheControl)
                        .build();//构建新的request

            }
            return chain.proceed(request);
        }
    };

    private OkHttpClientHelper(){
        init();
    }


    /**
     *OkHttpClient基本设置
     */
    private void init(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //添加HttpLogging拦截器，方便观察，上传和返回json
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//记录的是request的Body的内容
        builder.addInterceptor(loggingInterceptor);

        //申请缓存空间
        File cacheFile = FileUtils.getInstance().getCacheFolder();
        Cache cache = new Cache(cacheFile,AppConstants.CACHE_CAPACITY);
//        builder.cache(cache)//设置缓存
//                .addInterceptor(REWRITE_RESPONSE_INTERCEPTOR_OFFLINE)//离线拦截器
//                .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)//网络拦截器
//                .build();
        builder.addInterceptor(new EnhancedCacheInterceptor());


        okHttpClient = builder.build();
    }

    public static OkHttpClientHelper getInstance(Context context){
        sContext = context;
        return OkHttpClientHolder.instance;
    }



    private static class OkHttpClientHolder{
        private static OkHttpClientHelper instance = new OkHttpClientHelper();

    }

    public OkHttpClient getOkHttpClient(){
        return okHttpClient;
    }
}
