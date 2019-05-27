package com.meishi.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.R;
import com.meishi.contract.LoginContact;
import com.meishi.contract.RegistContract;
import com.meishi.presenter.LoginDealer;
import com.meishi.presenter.RegistDealer;


public class RegistLoadingActivity extends BaseActivity implements RegistContract.RegistView {

    Handler handler;
    private String username;
    private String password;
    private int result = AppConstants.FAIL_REGIST;//设置注册信息
    private RegistContract.RegistPresenter registPresenter ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_regist);
        registPresenter = new RegistDealer(MyApplication.getContext(),this);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        registPresenter.regist();
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
    public void setRegistResult(int result) {
        this.result = result;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void progressUpdate() {
        RegistLoadingActivity.this.setResult(result);
        RegistLoadingActivity.this.finish();
    }

    @Override
    public void logout(String msg) {

    }
}
