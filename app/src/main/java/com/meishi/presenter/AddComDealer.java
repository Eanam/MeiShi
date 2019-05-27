package com.meishi.presenter;

import android.content.Context;

import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.base.BaseObserver;
import com.meishi.contract.AddComContract;
import com.meishi.helpers.RetrofitHelper;
import com.meishi.infobean.SubmitInfo;
import com.meishi.services.InfoService;
import com.meishi.utils.TimeUtils;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddComDealer implements AddComContract.AddComPresenter {
    private Context mContext;
    private AddComContract.AddComView addComView;
    private InfoService service;

    public AddComDealer(Context context, AddComContract.AddComView addComView){
        this.mContext = context;
        this.addComView = addComView;
        this.service = RetrofitHelper.getInstance(mContext).creatNorService(InfoService.class);
    }

    @Override
    public void submitComment() {
        service.submitComment(MyApplication.readFromSp(AppConstants.TOKEN), TimeUtils.getStringTimeNow(),
                addComView.getShopId(),addComView.getGoodId(),addComView.getStar(),addComView.getComDesc())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.bindUntilEvent(addComView.getLifeCycleSubject(), ActivityEvent.DESTROY))
                .doOnSubscribe(disposable -> addComView.startSubmit())
                .doFinally(() -> addComView.finishSubmit())
                .subscribe(new BaseObserver<SubmitInfo>() {
                    @Override
                    public void success(SubmitInfo submitInfo) {
                        if (submitInfo.getCode() == 1001){
                            addComView.logout(submitInfo.getMsg());
                        }else{
                            addComView.setSubmitResult(submitInfo.getCode());
                        }
                    }

                    @Override
                    public void error(Throwable e) {

                    }
                });


    }
}
