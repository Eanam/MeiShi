package com.meishi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.R;
import com.meishi.contract.WelcomeContract;
import com.meishi.helpers.DatabaseHelper;
import com.meishi.presenter.WelcomeDealer;
import com.meishi.utils.UserUtils;
import com.meishi.views.InputView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements WelcomeContract.WelcomeView {

    @BindView(R.id.input_phone)InputView mInputPhone;
    @BindView(R.id.input_password)InputView mInputPassword;
    private Intent intent;
    private final int REQUEST_LOGIN = 1;
    private WelcomeContract.WelcomePresnter welcomePresnter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        welcomePresnter = new WelcomeDealer(MyApplication.getContext(),this);
        databaseHelper = new DatabaseHelper(MyApplication.getContext());
        initView();
    }

    //初始化UI
    private void initView(){
        //设置NavigationBar
        initNavBar(false , "登录", false);
    }

    /**
     * 跳转注册页面注册点击事件
     */
    public void onRegisterClick(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * 登录按钮的点击事件
     * @param view
     */
    @OnClick(R.id.bt_commit)
    public void onCommitClick(View view){
        //获取手机号
        String phone = mInputPhone.getInputStr();

        //获取密码
        String password = mInputPassword.getInputStr();

//        if(UserUtils.validateLogin(this,phone,password)){
//            //执行网络传输验证是否手机号密码匹配
//        }
        if(!UserUtils.validateLogin(MyApplication.getContext(),phone,password)){
            return;
        }

        intent = new Intent();
        intent.putExtra("username",phone);
        intent.putExtra("password",password);

        intent.setClass(LoginActivity.this, LoginLoadingActivity.class);//跳转到加载界面
        startActivityForResult(intent,REQUEST_LOGIN);
        //跳转到应用主页
//        Intent intent = new Intent(this,MainActivity.class);
//        startActivity(intent);
//        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_LOGIN) {
            switch (resultCode){
                case AppConstants.SUCCESS_LOGIN://登陆成功
                    //将token写入SharePrefence
//                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //获取收藏列表
                    welcomePresnter.getCollect();

                    break;

                case AppConstants.WRONG_PASSWORD://密码错误
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();

                    break;

                case AppConstants.UNREGIST://用户名不存在
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();

                    break;

                default:
                        break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }

    @Override
    public void update(List<Integer> data) {
        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        databaseHelper.deleteDatas();
        databaseHelper.insertDatas(data);
        //跳转到主界面
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void failUpdate() {
        Toast.makeText(LoginActivity.this, "获取数据出错，请重新登录", Toast.LENGTH_SHORT).show();

    }
}
