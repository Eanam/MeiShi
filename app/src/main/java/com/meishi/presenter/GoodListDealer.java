package com.meishi.presenter;

import android.content.Context;


import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.cache.EnhancedCall;
import com.meishi.cache.EnhancedCallback;
import com.meishi.contract.GoodContract;
import com.meishi.helpers.RetrofitHelper;
import com.meishi.infobean.GoodInfo;
import com.meishi.services.InfoService;

import retrofit2.Call;
import retrofit2.Response;

public class GoodListDealer implements GoodContract.GoodPresenter {
    private InfoService service;
    private GoodContract.GoodView goodView;
    private Context mContext;
    private int shopId;
    private String token;

    public GoodListDealer(Context context, GoodContract.GoodView goodView){
        this.mContext = context;
        this.goodView = goodView;
        service = RetrofitHelper.getInstance(mContext).creatSpService(InfoService.class);
        init();
    }

    private void init() {
        shopId = goodView.getShopid();
        token = MyApplication.readFromSp(AppConstants.TOKEN);
    }

    @Override
    public void getGoodList() {
        Call<GoodInfo> call = service.getGoodList(token,shopId);
        EnhancedCall<GoodInfo> enhancedCall = new EnhancedCall<>(call);
        enhancedCall.useCache(true)
                .dataClassName(GoodInfo.class)
                .enqueue(new EnhancedCallback<GoodInfo>() {
                    @Override
                    public void onResponse(Call<GoodInfo> call, Response<GoodInfo> response) {
                        if(goodView != null){
                            if (response.body().getCode() == 1001){
                                goodView.logout(response.body().getMsg());
                            }else {
                                goodView.update(response.body().getData());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GoodInfo> call, Throwable t) {
                        if(goodView != null){
                            goodView.failUpdate();
                        }
                    }

                    @Override
                    public void onGetCache(GoodInfo goodInfo) {
                        if(goodView != null){
                            goodView.update(goodInfo.getData());
                        }
                    }
                });

    }

    @Override
    public void connectView(GoodContract.GoodView goodView) {
        this.goodView = goodView;
    }

    @Override
    public void destoyView() {
        goodView = null;
    }
}
