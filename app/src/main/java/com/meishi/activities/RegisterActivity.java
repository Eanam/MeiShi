package com.meishi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.R;
import com.meishi.utils.UserUtils;
import com.meishi.views.InputView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity extends BaseActivity {
    @BindView(R.id.input_phone) InputView input_phone;
    @BindView(R.id.input_password)InputView input_password;
    @BindView(R.id.confirm_password)InputView confirm_password;

    private final int REQUEST_REGIST = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regitser);

        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化界面
     */
    private  void initView(){
        initNavBar(true,"注册",false);
    }


    /**
     * 注册按钮点击事件
     */
    public  void onRegisterClick(View view){
        String phone = input_phone.getInputStr();
        String password = input_password.getInputStr();
        String _password = confirm_password.getInputStr();

        //判断输入是否有效
        if(!UserUtils.validateRegist(MyApplication.getContext(),phone,password,_password)){
            return;
        }

        Intent intent = new Intent(RegisterActivity.this,RegistLoadingActivity.class);
        intent.putExtra("username",phone);
        intent.putExtra("password",password);

        startActivityForResult(intent,REQUEST_REGIST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_REGIST){
            switch (resultCode){
                case AppConstants.SUCCESS_REGIST:
                    Toast.makeText(this,"注册成功,即将跳转登录",Toast.LENGTH_SHORT).show();
                    RegisterActivity.this.finish();
                    break;

                case AppConstants.FAIL_REGIST:
                    Toast.makeText(this,"注册失败",Toast.LENGTH_SHORT).show();
                    break;

                default:

                    break;
            }
        }
    }
}
