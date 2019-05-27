package com.meishi.presenter;

import android.content.Context;
import android.util.Log;

import com.meishi.base.BaseObserver;
import com.meishi.cache.EnhancedCall;
import com.meishi.cache.EnhancedCallback;
import com.meishi.contract.ShopContract;
import com.meishi.helpers.RetrofitHelper;
import com.meishi.infobean.ShopInfo;
import com.meishi.services.InfoService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class ShopListDealer implements ShopContract.ShopPresenter {
    private ShopContract.ShopView mView;
    private Context mContext;
    private InfoService service;

    public ShopListDealer(Context mContext,ShopContract.ShopView mView){
        this.mView = mView;
        this.mContext = mContext;
        service = RetrofitHelper.getInstance(mContext).creatSpService(InfoService.class);
    }

    @Override
    public void getShopList() {
        Call<ShopInfo> call = service.getShopList();
        EnhancedCall<ShopInfo> enhancedCall = new EnhancedCall<>(call);
        enhancedCall.useCache(true)
                .dataClassName(ShopInfo.class)
                .enqueue(new EnhancedCallback<ShopInfo>() {
                    @Override
                    public void onResponse(Call<ShopInfo> call, Response<ShopInfo> response) {
                        if(mView != null && response != null){
                            mView.update(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<ShopInfo> call, Throwable t) {
                        if(mView != null){
                            mView.failUpdate();
                        }
                    }

                    @Override
                    public void onGetCache(ShopInfo shopInfo) {
                        if(mView != null){
                            mView.update(shopInfo.getData());
                        }
                    }
                });

    }

    @Override
    public void connectView(ShopContract.ShopView shopView) {
        mView = shopView;
        Log.d("ShopListDealer", "connectView: "+(mView != null));
    }

    @Override
    public void destoyView() {
        mView = null;
        Log.d("ShopListDealer", "destoyView: "+(mView == null));
    }
}
