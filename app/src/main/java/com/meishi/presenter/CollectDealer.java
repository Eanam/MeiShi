package com.meishi.presenter;

import android.content.Context;


import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.beans.CollectShop;
import com.meishi.cache.EnhancedCall;
import com.meishi.cache.EnhancedCallback;
import com.meishi.contract.CollectContract;
import com.meishi.helpers.RetrofitHelper;
import com.meishi.infobean.CollectShopInfo;
import com.meishi.infobean.ShopInfo;
import com.meishi.services.InfoService;

import retrofit2.Call;
import retrofit2.Response;

public class CollectDealer implements CollectContract.CollectPresenter {
    private Context mContext;
    private CollectContract.CollectView collectView;
    private InfoService service;

    public CollectDealer(Context context,CollectContract.CollectView collectView){
        this.mContext = context;
        this.collectView = collectView;
        service = RetrofitHelper.getInstance(mContext).creatSpService(InfoService.class);
    }


    @Override
    public void getCollectList() {
        Call<CollectShopInfo> call = service.getCollectShopList(MyApplication.readFromSp(AppConstants.TOKEN));
        EnhancedCall<CollectShopInfo> enhancedCall = new EnhancedCall<>(call);
        enhancedCall.useCache(true)
                .dataClassName(CollectShopInfo.class)
                .enqueue(new EnhancedCallback<CollectShopInfo>() {
                    @Override
                    public void onResponse(Call<CollectShopInfo> call, Response<CollectShopInfo> response) {
                        if (response.body().getCode() == 0){
                            if (collectView != null){
                                collectView.update(response.body().getData());
                            }
                        }else if(response.body().getCode() == 1001){
                            if (collectView != null){
                                collectView.logout(response.body().getMsg());
                            }
                        } else{
                            if (collectView != null){
                                collectView.failUpdate();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CollectShopInfo> call, Throwable t) {
                        if (collectView != null){
                            collectView.failUpdate();
                        }
                    }

                    @Override
                    public void onGetCache(CollectShopInfo collectShopInfo) {
                        if (collectShopInfo.getCode() == 0){
                            if (collectView != null){
                                collectView.update(collectShopInfo.getData());
                            }
                        }
                    }
                });
    }

    @Override
    public void connectView(CollectContract.CollectView collectView) {
        this.collectView = collectView;
    }

    @Override
    public void destoyView() {
        this.collectView = null;
    }
}
