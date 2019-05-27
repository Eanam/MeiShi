package com.meishi.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.meishi.R;

import java.util.ArrayList;
import java.util.List;

public class PermissionAvtivity extends Activity {
    AlertDialog mPermissionDialog;

    //需要申请的权限
    private String[] permissions = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA
    };

    //存放为申请到的权限
    private List<String> mPermission = new ArrayList<>();

    //申明申请码
    private final int mRequsetCode = 100;

    private void initPermission(){
        mPermission.clear();
        //判断是否还有未申请的权限
        for (int i=0;i<permissions.length;i++){
            if(ContextCompat.checkSelfPermission(this,permissions[i])
                    != PackageManager.PERMISSION_GRANTED){
                mPermission.add(permissions[i]);
            }
        }

        //申请未通过的权限
        if (mPermission.size() > 0){
            ActivityCompat.requestPermissions(this,permissions,mRequsetCode);
        }else{
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//true表示有权限没有通过
        if(mRequsetCode == requestCode){
            for (int i=0;i<grantResults.length;i++){
                if(grantResults[i] == -1){//有权限申请未通过
                    hasPermissionDismiss = true;
                    break;
                }
            }
        }

        if(hasPermissionDismiss){
            showPermissionDialog();
        }else{
            //权限都申请通过，则程序可以继续
            init();
        }
    }

    //展示对话框
    private void showPermissionDialog(){
        if(mPermissionDialog == null){
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();

                            Uri packageURI = Uri.parse("package:"+getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();
                            PermissionAvtivity.this.finish();
                        }
                    })
                    .create();

        }

        mPermissionDialog.show();
    }

    private void cancelPermissionDialog(){
        mPermissionDialog.cancel();
    }

    public void init(){
        Intent intent = new Intent(this,WelcomeActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_avtivity);

        initPermission();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        initPermission();//在设置权限返回此界面后悔自动再次监测权限，不用重新启动
    }
}
