package com.meishi.presenter;

import android.content.Context;
import android.util.Log;

import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.base.BaseObserver;
import com.meishi.contract.SubCollectContract;
import com.meishi.helpers.RetrofitHelper;
import com.meishi.infobean.LoginAndRegistInfo;
import com.meishi.infobean.SubmitInfo;
import com.meishi.services.InfoService;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SubCollectDealer implements SubCollectContract.SubCollectPresenter {
    private Context mContext;
    private InfoService service;
    private SubCollectContract.SubCollectView subCollectView;

    public SubCollectDealer(Context context, SubCollectContract.SubCollectView subCollectView){
        this.mContext = context;
        this.subCollectView = subCollectView;
        service = RetrofitHelper.getInstance(mContext).creatNorService(InfoService.class);//不需要缓存
    }


    @Override
    public void submitCollect() {
        service.submitCollection(MyApplication.readFromSp(AppConstants.TOKEN),subCollectView.getShopId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.bindUntilEvent(subCollectView.getLifeCycleSubject(), ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<SubmitInfo>() {
                    @Override
                    public void success(SubmitInfo submitInfo) {
                        if(submitInfo.getCode() == 0){
                            //提交成功
                            subCollectView.successCollect(submitInfo.getMsg());
                        }else if (submitInfo.getCode() == 1001){
                            subCollectView.logout(submitInfo.getMsg());
                        }else{
                            subCollectView.failCollect(submitInfo.getMsg());
                        }
                    }

                    @Override
                    public void error(Throwable e) {
                        subCollectView.failCollect("收藏失败");
                    }
                });


    }

    @Override
    public void cancelCollect() {
        Log.d("SubCollectDealer", "cancelCollect: -------------");
        service.cancelCollect(MyApplication.readFromSp(AppConstants.TOKEN),subCollectView.getShopId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.bindUntilEvent(subCollectView.getLifeCycleSubject(), ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<SubmitInfo>() {
                    @Override
                    public void success(SubmitInfo submitInfo) {
                        if(submitInfo.getCode() == 0){
                            //提交成功
                            subCollectView.successCancel(submitInfo.getMsg());
                        }else{
                            subCollectView.failCancel(submitInfo.getMsg());
                        }
                    }

                    @Override
                    public void error(Throwable e) {
                        subCollectView.failCancel("操作失败");
                    }
                });
    }
}
