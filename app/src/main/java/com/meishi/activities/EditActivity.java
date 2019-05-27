package com.meishi.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.meishi.AppConstants;
import com.meishi.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 修改资料界面
 */
public class EditActivity extends BaseActivity {
    private String title;
    private String profle;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.ed_oldprofile) EditText ed_old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);
        title = getIntent().getStringExtra("title");
        profle =  getIntent().getStringExtra("profile");
        this.setResult(1);//预先设置不保存
        init();
    }

    private void init(){
        initView();
    }

    private void initView(){
        initNavBar(true,"更改"+title,true);
        ed_old.setText(profle);
        setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存
                Intent intent = new Intent();
                intent.putExtra(AppConstants.EDIT_CONTENT,ed_old.getText().toString());
                EditActivity.this.setResult(0,intent);
                EditActivity.this.finish();
            }
        });
    }
}
