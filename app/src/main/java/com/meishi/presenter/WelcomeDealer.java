package com.meishi.presenter;

import android.content.Context;
import android.util.Log;

import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.base.BaseObserver;
import com.meishi.cache.EnhancedCall;
import com.meishi.cache.EnhancedCallback;
import com.meishi.contract.WelcomeContract;
import com.meishi.helpers.RetrofitHelper;
import com.meishi.infobean.CollectShopInfo;
import com.meishi.services.InfoService;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class WelcomeDealer implements WelcomeContract.WelcomePresnter {
    private Context mContext;
    private WelcomeContract.WelcomeView welcomeView;
    private InfoService service;

    public WelcomeDealer(Context context, WelcomeContract.WelcomeView welcomeView){
        this.mContext = context;
        this.welcomeView = welcomeView;
        service = RetrofitHelper.getInstance(mContext).creatSpService(InfoService.class);
    }


    @Override
    public void getCollect() {
        Observable<CollectShopInfo> observable = service.getCollectShop(MyApplication.readFromSp(AppConstants.TOKEN));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.bindUntilEvent(welcomeView.getLifeCycleSubject(), ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<CollectShopInfo>() {
                    @Override
                    public void success(CollectShopInfo collectShopInfo) {
                        if (collectShopInfo.getCode() == 1001){//登录过期
                            Log.d("WelcomeActivity", "init: tryMain5: "+MyApplication.readFromSp(AppConstants.TOKEN)+"  "+collectShopInfo.getCode());

                            welcomeView.failUpdate();
                        }else if(collectShopInfo.getCode() == 0){//成功获取
                            Log.d("WelcomeActivity", "init: tryMain4");
                            List<Integer> list = new ArrayList<>();
                            if (collectShopInfo.getIds() != null){
                                list = collectShopInfo.getIds();
                            }
                            welcomeView.update(list);
                        }
                    }

                    @Override
                    public void error(Throwable e) {
                        Log.d("WelcomeActivity", "init: tryMain3");
                        welcomeView.failUpdate();
                    }
                });

    }

    @Override
    public void getCollectForWelcome() {
        Call<CollectShopInfo> call = service.getCollectShopList(MyApplication.readFromSp(AppConstants.TOKEN));
        EnhancedCall<CollectShopInfo> enhancedCall = new EnhancedCall<>(call);
        enhancedCall.useCache(true)
                .dataClassName(CollectShopInfo.class)
                .enqueue(new EnhancedCallback<CollectShopInfo>() {
                    @Override
                    public void onResponse(Call<CollectShopInfo> call, Response<CollectShopInfo> response) {
                        if (response.body().getCode() == 0){
                            if (welcomeView != null){
                                List<Integer> list = new ArrayList<>();
                                if (response.body().getIds() != null){
                                    list = response.body().getIds();
                                }
                                welcomeView.update(list);
                            }
                        }else if(response.body().getCode() == 1001){
                            if (welcomeView != null){
                                welcomeView.failUpdate();
                            }
                        } else{
                            if (welcomeView != null){
                                welcomeView.failUpdate();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CollectShopInfo> call, Throwable t) {
                        if (welcomeView != null){
                            welcomeView.failUpdate();
                        }
                    }

                    @Override
                    public void onGetCache(CollectShopInfo collectShopInfo) {
                        if (collectShopInfo.getCode() == 0){
                            if (welcomeView != null){
                                welcomeView.update(collectShopInfo.getIds());
                            }
                        }
                    }
                });
    }

    @Override
    public void connectView(WelcomeContract.WelcomeView welcomeView) {
        this.welcomeView = welcomeView;
    }

    @Override
    public void destroyView() {
        this.welcomeView = null;
    }


}
