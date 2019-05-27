package com.meishi.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.meishi.MyApplication;
import com.meishi.R;
import com.meishi.contract.LoginContact;
import com.meishi.presenter.LoginDealer;


public class LoginLoadingActivity extends BaseActivity implements LoginContact.LoginView {
    Handler handler;
    private String username;
    private String password;
    private int result = 2;//设置登录信息
    private LoginContact.LoginPresenter loginPresenter ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        //搬到presenter
        loginPresenter = new LoginDealer(MyApplication.getContext(),this);
        loginPresenter.Login();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setLoginResult(int result) {
        this.result = result;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void progressUpdate() {
        LoginLoadingActivity.this.setResult(result);
        LoginLoadingActivity.this.finish();
    }

    @Override
    public void logout(String msg) {

    }

    //子线程访问网络
}
