package com.meishi.activities;


import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meishi.R;
import com.meishi.base.BaseView;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.subjects.BehaviorSubject;

public class BaseActivity extends RxAppCompatActivity implements BaseView {

    ImageView iv_back,iv_me;
    TextView tv_title;
    //Activity的状态
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();


    /**
     * 设置NavigationBar的显示
     * @param isShowBack 是否显示返回按钮
     * @param title  设置标题内容
     * @param isShowMe 是否显示个人信息按钮
     */
    protected void initNavBar(boolean isShowBack,String title,boolean isShowMe){
        iv_back = (ImageView) this.findViewById(R.id.iv_back);
        iv_me = (ImageView)this.findViewById(R.id.iv_me);
        tv_title = (TextView)this.findViewById(R.id.tv_title);

        iv_back.setVisibility(isShowBack ? View.VISIBLE : View.GONE);
        iv_me.setVisibility(isShowMe ? View.VISIBLE : View.GONE);
        tv_title.setText(title);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    protected void setRightClick(View.OnClickListener listener){
        iv_me.setOnClickListener(listener);
    }

    //返回
    @SuppressWarnings("unchecked")
    @Override
    public final BehaviorSubject getLifeCycleSubject(){
        return lifecycleSubject;
    }

    @Override
    public void logout(String msg) {

    }
}
