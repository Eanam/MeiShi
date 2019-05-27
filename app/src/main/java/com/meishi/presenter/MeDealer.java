package com.meishi.presenter;

import android.content.Context;


import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.cache.EnhancedCall;
import com.meishi.cache.EnhancedCallback;
import com.meishi.contract.MeContract;
import com.meishi.helpers.RetrofitHelper;
import com.meishi.infobean.MeInfo;
import com.meishi.services.InfoService;

import retrofit2.Call;
import retrofit2.Response;

public class MeDealer implements MeContract.MePresenter {
    private Context mContext;
    private MeContract.MeView meView;
    private InfoService service;

    public MeDealer(Context context, MeContract.MeView meView){
        this.mContext = context;
        this.meView = meView;
        service = RetrofitHelper.getInstance(mContext).creatSpService(InfoService.class);
    }

    @Override
    public void getProfile() {
        Call<MeInfo> call = service.getProfile(MyApplication.readFromSp(AppConstants.TOKEN));
        EnhancedCall<MeInfo> enhancedCall = new EnhancedCall<>(call);
        enhancedCall.useCache(true)
                .dataClassName(MeInfo.class)
                .enqueue(new EnhancedCallback<MeInfo>() {
                    @Override
                    public void onResponse(Call<MeInfo> call, Response<MeInfo> response) {
                        if(meView != null){
                            if (response.body().getCode() == 1001){
                                meView.logout(response.body().getMsg());
                            }else {
                                meView.update(response.body());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MeInfo> call, Throwable t) {
                        if(meView != null){
                            meView.failUpdate();
                        }
                    }

                    @Override
                    public void onGetCache(MeInfo meInfo) {
                        if(meView != null){
                            meView.update(meInfo);
                        }
                    }
                });
    }

    @Override
    public void connectView(MeContract.MeView meView) {
        this.meView = meView;
    }

    @Override
    public void destoyView() {
        this.meView = null;
    }
}
