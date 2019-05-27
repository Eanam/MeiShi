package com.meishi.presenter;

import android.content.Context;

import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.base.BaseObserver;
import com.meishi.contract.LoginContact;
import com.meishi.helpers.RetrofitHelper;
import com.meishi.infobean.InfoBean;
import com.meishi.infobean.LoginAndRegistInfo;
import com.meishi.services.InfoService;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import static com.meishi.AppConstants.WRONG_PASSWORD;

public class LoginDealer implements LoginContact.LoginPresenter {



    private InfoService service;
    private LoginContact.LoginView loginView;
    private Context mContext;

    public LoginDealer(Context context,LoginContact.LoginView loginView){
        this.loginView = loginView;
        this.mContext = context;
        service = RetrofitHelper.getInstance(mContext).creatNorService(InfoService.class);
    }

    @Override
    public void Login() {
        service.login(loginView.getUsername(),loginView.getPassword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.bindUntilEvent(loginView.getLifeCycleSubject(), ActivityEvent.DESTROY))
                .doOnSubscribe(disposable -> loginView.showProgress())
                .doFinally(() -> loginView.progressUpdate())
                .subscribe(new BaseObserver<LoginAndRegistInfo>() {
                    @Override
                    public void success(LoginAndRegistInfo loginAndRegistInfo) {
                        switch (loginAndRegistInfo.getCode()){
                            case AppConstants.SUCCESS_LOGIN://登陆成功
                                //将token写入SharePrefence
                                MyApplication.writeToSP(AppConstants.TOKEN,loginAndRegistInfo.getData().getAccess_token());
                                break;

                            case AppConstants.WRONG_PASSWORD://密码错误

                                break;

                            case AppConstants.UNREGIST://用户名不存在

                                break;

                            default:
                                break;
                        }
                        loginView.setLoginResult(loginAndRegistInfo.getCode());
                    }

                    @Override
                    public void error(Throwable e) {

                    }
                });
    }
}
