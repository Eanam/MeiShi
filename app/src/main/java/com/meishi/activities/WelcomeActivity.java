package com.meishi.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.R;
import com.meishi.contract.CollectContract;
import com.meishi.contract.WelcomeContract;
import com.meishi.helpers.DatabaseHelper;
import com.meishi.presenter.WelcomeDealer;

import java.util.List;

public class WelcomeActivity extends BaseActivity implements WelcomeContract.WelcomeView {

    private Handler handler;
    Intent intent_main = new Intent();
    Intent intent_login = new Intent();
    WelcomeContract.WelcomePresnter welcomePresnter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                View.SYSTEM_UI_FLAG_VISIBLE,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        welcomePresnter = new WelcomeDealer(MyApplication.getContext(),this);
        databaseHelper = new DatabaseHelper(MyApplication.getContext());
        handler = new Handler();
        intent_login = new Intent();
        intent_main = new Intent();
        init();
    }


    @Override
    protected void onResume() {
        super.onResume();
        welcomePresnter.connectView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        welcomePresnter.destroyView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }

    private void init() {
        intent_login.setClass(this,LoginActivity.class);
        if( MyApplication.readFromSp(AppConstants.TOKEN) != null && (!MyApplication.readFromSp(AppConstants.TOKEN).equals(""))){
            intent_main.setClass(this,MainActivity.class);
            welcomePresnter.getCollectForWelcome();
        }else{
            failUpdate();
        }

    }


    @Override
    public void update(List<Integer> data) {
        Log.d("WelcomeActivity", "init: tryMain2");
        databaseHelper.deleteDatas();
        databaseHelper.insertDatas(data);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                WelcomeActivity.this.startActivity(intent_main);
                WelcomeActivity.this.finish();
            }
        },1000);
    }

    @Override
    public void failUpdate() {
        Log.d("LOGouT", "failUpdate: -----");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                WelcomeActivity.this.startActivity(intent_login);
                WelcomeActivity.this.finish();
            }
        },1000);
    }

    @Override
    public void logout(String msg) {

    }
}
