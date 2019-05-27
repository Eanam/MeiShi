package com.meishi.presenter;

import android.content.Context;
import android.util.Log;


import com.meishi.base.BaseObserver;
import com.meishi.contract.RegistContract;
import com.meishi.helpers.RetrofitHelper;
import com.meishi.infobean.LoginAndRegistInfo;
import com.meishi.services.InfoService;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegistDealer implements RegistContract.RegistPresenter {
    private InfoService service;
    private RegistContract.RegistView registView;
    private Context mContext;

    public RegistDealer(Context context, RegistContract.RegistView registView){
        mContext = context;
        this.registView = registView;
        service = RetrofitHelper.getInstance(mContext).creatNorService(InfoService.class);
    }
    @Override
    public void regist() {
        service.regist(registView.getUsername(),registView.getPassword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.bindUntilEvent(registView.getLifeCycleSubject(), ActivityEvent.DESTROY))
                .doOnSubscribe(disposable -> registView.showProgress())
                .doFinally(() -> registView.progressUpdate())
                .subscribe(new BaseObserver<LoginAndRegistInfo>() {
                    @Override
                    public void success(LoginAndRegistInfo loginAndRegistInfo) {
                        registView.setRegistResult(loginAndRegistInfo.getCode());
                    }

                    @Override
                    public void error(Throwable e) {
                        Log.d("regist", "error: "+e);
                    }
                });

    }
}
