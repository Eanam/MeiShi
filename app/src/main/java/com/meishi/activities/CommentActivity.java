package com.meishi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;
import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.R;
import com.meishi.adapters.CommentAdapter;
import com.meishi.beans.Comment;
import com.meishi.beans.Good;
import com.meishi.contract.CommentContract;
import com.meishi.presenter.CommentDealer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentActivity extends BaseActivity implements CommentContract.CommentView {
    private static final int REQUEST_CODE = 0;

    private List<Comment> commentList = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private int shop_id;
    private Good good;
    private CommentContract.CommentPresenter commentPresenter;
    private boolean isRefresh = false;
    private boolean isNeedRefresh = false;
    @BindView(R.id.rv_content) RecyclerView mRv_commentList;
    @BindView(R.id.iv_goodPic) ImageView iv_goodPic;
    @BindView(R.id.tv_name) TextView goodName;
    @BindView(R.id.xrv_comment) XRefreshView xRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            this.finish();
        }
        shop_id = bundle.getInt(AppConstants.GOOD_TO_GET_COMMENT_2);
        good = (Good) bundle.getSerializable(AppConstants.GOOD_TO_GET_COMMENT_1);
        commentPresenter = new CommentDealer(MyApplication.getContext(),this);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        commentPresenter.connectView(this);
        if(isNeedRefresh){
            isRefresh = false;
            xRv.startRefresh();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        commentPresenter.destoyView();
    }

    private void initView(){

        initNavBar(true,good.getGoodname(),true);
        Glide.with(this)
                .load(good.getPicurl())
                .into(iv_goodPic);
        goodName.setText(good.getGoodname());
        setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentActivity.this,AddComActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstants.SHOP_ID,shop_id);
                bundle.putInt(AppConstants.GOOD_ID,good.getId());
                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

    private void initXRefreshView(){
        //默认只能下拉刷新

        xRv.setPullLoadEnable(true);//运行上拉下载数据
        xRv.setAutoLoadMore(false);
        xRv.enableReleaseToLoadMore(true);
        xRv.enableRecyclerViewPullUp(true);
        xRv.enablePullUpWhenLoadCompleted(true);

        //自定义动画

        xRv.setMoveForHorizontal(true);
        //监听器
        xRv.setXRefreshViewListener(xRefreshViewListener);
    }
    private void init(){

        initView();
        initData();
        initXRefreshView();

        mRv_commentList.setLayoutManager(new LinearLayoutManager(this));
        mRv_commentList.setNestedScrollingEnabled(false);
        mRv_commentList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        commentAdapter = new CommentAdapter(this, mRv_commentList,commentList);
        mRv_commentList.setAdapter(commentAdapter);

    }

    private void initData(){
        commentPresenter.getCommentList();
//        commentList.add(new Comment("这是评论1这是评论1这是评论1这是评论1这是评论1",4.3f,2,1,"爱吃番茄的主","2019-5-10 19:00"));
//        commentList.add(new Comment("这是评论2这是评论2这是评论2这是评论2这是评论1",3.0f,2,1,"爱吃番茄的主","2019-5-11 12:00"));
//        commentList.add(new Comment("这是评论1这是评论1这是评论1这是评论1这是评论1",2.7f,2,1,"爱吃番茄的主","2019-5-10 19:00"));
//        commentList.add(new Comment("这是评论1这是评论1这是评论1这是评论1这是评论1",1.1f,2,1,"爱吃番茄的主","2019-5-10 19:00"));
//        commentList.add(new Comment("这是评论1这是评论1这是评论1这是评论1这是评论1",2.9f,2,1,"爱吃番茄的主","2019-5-10 19:00"));
    }


    @Override
    public int getGoodid() {
        return good.getId();
    }

    @Override
    public void update(List<Comment> data) {
        if (isRefresh){
            isRefresh = false;
            xRv.stopRefresh();
        }
        commentList.clear();
        commentList.addAll(data);
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void failUpdate() {
        if (isRefresh){
            isRefresh = false;
            xRv.stopRefresh();
        }
        Toast.makeText(this,"获取评论失败",Toast.LENGTH_SHORT).show();

    }

    //刷新框架的监听器
    private XRefreshView.XRefreshViewListener xRefreshViewListener = new XRefreshView.XRefreshViewListener() {
        @Override
        public void onRefresh() {
            isRefresh = true;
            commentPresenter.getCommentList();
        }

        @Override
        public void onRefresh(boolean isPullDown) {

        }

        @Override
        public void onLoadMore(boolean isSilence) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //下载数据的网络请求

                    xRv.stopLoadMore();
                }
            },1000);//刷新延时，使得动画效果能显示出来
        }

        @Override
        public void onRelease(float direction) {

        }

        @Override
        public void onHeaderMove(double headerMovePercent, int offsetY) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            //评论成功即可立即刷新
            if (resultCode == AppConstants.SUBMIT_SUCCESS){
                isNeedRefresh = true;
            }
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
