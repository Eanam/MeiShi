package com.meishi.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.R;
import com.meishi.contract.AddComContract;
import com.meishi.presenter.AddComDealer;
import com.meishi.views.StarBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddComActivity extends BaseActivity implements AddComContract.AddComView {
    @BindView(R.id.ed_comment) EditText commentDesc;
    @BindView(R.id.com_point) StarBar comPoints;
//    @BindView(R.id.iv_me) ImageView comSubmit;
    private int subResult = AppConstants.SUBMIT_FAILURE;//提交结果,0表示成功，1表示失败
    private AddComContract.AddComPresenter addComPresenter;
    private ProgressDialog progressDialog;
    private int shop_id;
    private int good_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_com);
        progressDialog = new ProgressDialog(this);
        Bundle bundle = getIntent().getExtras();
        shop_id = bundle.getInt(AppConstants.SHOP_ID);
        good_id = bundle.getInt(AppConstants.GOOD_ID);
        addComPresenter = new AddComDealer(MyApplication.getContext(),this);
        ButterKnife.bind(this);
        init();
    }

    private void initView(){
        initNavBar(true,"添加评论",true);

        //提交操作
        setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交评论
                addComPresenter.submitComment();
            }
        });

        //初始化对话框
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在提交评论...");
        progressDialog.setCancelable(true);
    }

    private void init(){
        initView();
        comPoints.setClickAble(true);
    }


    @Override
    public float getStar() {
        return comPoints.getStarMark();
    }

    @Override
    public int getShopId() {
        return shop_id;
    }

    @Override
    public int getGoodId() {
        return good_id;
    }

    @Override
    public String getComDesc() {
        return commentDesc.getText().toString();
    }

    @Override
    public void setSubmitResult(int result) {
        this.subResult = result;
    }

    @Override
    public void startSubmit() {
        progressDialog.show();
    }

    @Override
    public void finishSubmit() {
        progressDialog.dismiss();
        if(subResult == AppConstants.SUBMIT_FAILURE){
            Toast.makeText(this,"提交失败",Toast.LENGTH_SHORT).show();
            this.setResult(AppConstants.SUBMIT_FAILURE);
        }else{
            Toast.makeText(this,"提交成功",Toast.LENGTH_SHORT).show();
            this.setResult(AppConstants.SUBMIT_SUCCESS);
            this.finish();
        }
    }

    @Override
    public void logout(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.writeToSP(AppConstants.TOKEN,"");
        startActivity(intent);

        ((Activity)this).overridePendingTransition(R.anim.open_enter,R.anim.open_exit);
    }
}
