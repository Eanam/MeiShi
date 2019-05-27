package com.meishi.presenter;

import android.content.Context;


import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.base.BaseObserver;
import com.meishi.cache.EnhancedCall;
import com.meishi.cache.EnhancedCallback;
import com.meishi.contract.PrivateComContract;
import com.meishi.helpers.RetrofitHelper;
import com.meishi.infobean.PrivateComInfo;
import com.meishi.infobean.SubmitInfo;
import com.meishi.services.InfoService;
import com.meishi.utils.TimeUtils;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class PrivateComDealer implements PrivateComContract.PrivateComPresenter {

    private Context mContext;
    private PrivateComContract.PrivateComView privateComView;
    private InfoService service;

    public PrivateComDealer(Context context, PrivateComContract.PrivateComView privateComView){
        this.mContext = context;
        this.privateComView = privateComView;
        service = RetrofitHelper.getInstance(mContext).creatSpService(InfoService.class);
    }

    @Override
    public void getComHistory() {
        Call<PrivateComInfo> call = service.getPrivateCom(MyApplication.readFromSp(AppConstants.TOKEN));
        EnhancedCall<PrivateComInfo> enhancedCall = new EnhancedCall<>(call);
        enhancedCall.useCache(true)
                .dataClassName(PrivateComInfo.class)
                .enqueue(new EnhancedCallback<PrivateComInfo>() {
                    @Override
                    public void onResponse(Call<PrivateComInfo> call, Response<PrivateComInfo> response) {
                        if(response.body().getCode() == 1001){
                            if (privateComView != null){
                                privateComView.logout(response.body().getMsg());
                            }
                        }else if(response.body().getCode() == 0){
                            if (privateComView != null){
                                privateComView.successGet(response.body().getData());
                            }
                        }else {
                            if (privateComView != null){
                                privateComView.failGet();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PrivateComInfo> call, Throwable t) {
                        if (privateComView != null){
                            privateComView.failGet();
                        }
                    }

                    @Override
                    public void onGetCache(PrivateComInfo privateComInfo) {
                        if(privateComInfo.getCode() == 1001){
                            if (privateComView != null){
                                privateComView.logout(privateComInfo.getMsg());
                            }
                        }else if(privateComInfo.getCode() == 0){
                            if (privateComView != null){
                                privateComView.successGet(privateComInfo.getData());
                            }
                        }else {
                            if (privateComView != null){
                                privateComView.failGet();
                            }
                        }
                    }
                });
    }

    @Override
    public void cancelComment(int comid) {
        service.cancelCom(MyApplication.readFromSp(AppConstants.TOKEN),comid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.bindUntilEvent(privateComView.getLifeCycleSubject(), ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<SubmitInfo>() {
                    @Override
                    public void success(SubmitInfo submitInfo) {
                        if(submitInfo.getCode() == 1001){
                            privateComView.logout(submitInfo.getMsg());
                        }else if(submitInfo.getCode() == 0){
                            privateComView.successCancel();
                        }else {
                            privateComView.failCancel();
                        }
                    }

                    @Override
                    public void error(Throwable e) {
                        privateComView.failCancel();
                    }
                });
    }

    @Override
    public void connectView(PrivateComContract.PrivateComView privateComView) {
        this.privateComView = privateComView;
    }

    @Override
    public void destroyView() {
        this.privateComView = null;
    }
}
