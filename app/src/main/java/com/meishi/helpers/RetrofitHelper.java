package com.meishi.helpers;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.meishi.AppConstants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitHelper {
    private Retrofit spRetrofit;//加了拦截器的Retrofit
    private Retrofit norRetrofit;//普通拦截器
    private static Context mContext;

    private RetrofitHelper(){
        init();
    }

    private void init(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//记录的是request的Body的内容
        builder.addInterceptor(loggingInterceptor);
        OkHttpClient client = builder.build();


        norRetrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASEURL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//支持RxJava
                .addConverterFactory(ScalarsConverterFactory.create())//支持字符串
                .addConverterFactory(GsonConverterFactory.create())//支持Gson解析
                .build();

        spRetrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASEURL)
                .client(OkHttpClientHelper.getInstance(mContext).getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//支持RxJava
                .addConverterFactory(ScalarsConverterFactory.create())//支持字符串
                .addConverterFactory(GsonConverterFactory.create())//支持Gson解析
                .build();
    }

    public static RetrofitHelper getInstance(Context context){
        mContext = context;
        return RetrofitHolder.instance;
    }


    private static class RetrofitHolder{
        private static RetrofitHelper instance = new RetrofitHelper();
    }

    public Retrofit getSpRetrofit(){
        return spRetrofit;
    }

    public Retrofit getNorRetrofit(){
        return norRetrofit;
    }

    //带缓存的
    public <T> T creatSpService(final Class<T> service){
        return spRetrofit.create(service);
    }

    //不带缓存的
    public <T> T creatNorService(final Class<T> service){
        return norRetrofit.create(service);
    }
}
