package com.meishi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;
import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.R;
import com.meishi.adapters.PrivateComAdapter;
import com.meishi.beans.PrivateCom;
import com.meishi.contract.PrivateComContract;
import com.meishi.presenter.PrivateComDealer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivateComActivity extends BaseActivity implements PrivateComContract.PrivateComView {
    private PrivateComAdapter privateComAdapter;
    @BindView(R.id.rv_content) public RecyclerView recyclerView;
    private List<PrivateCom> comList = new ArrayList<>();
    private PrivateComContract.PrivateComPresenter privateComPresenter;
    @BindView(R.id.xrv_privatecom) public XRefreshView xRefreshView;
    private boolean isRefresh = false;


    private PrivateComAdapter.OnItemClickListener onItemClickListener = new PrivateComAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            //删除评论
            privateComPresenter.cancelComment(comList.get(position).getCom().getComid());
        }
    };

    private XRefreshView.XRefreshViewListener myXrVListener = new XRefreshView.XRefreshViewListener() {
        @Override
        public void onRefresh() {
            isRefresh = true;
            privateComPresenter.getComHistory();
        }

        @Override
        public void onRefresh(boolean isPullDown) {

        }

        @Override
        public void onLoadMore(boolean isSilence) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    xRefreshView.stopLoadMore();
                }
            },2000);
        }

        @Override
        public void onRelease(float direction) {

        }

        @Override
        public void onHeaderMove(double headerMovePercent, int offsetY) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_com);
        ButterKnife.bind(this);
        privateComPresenter = new PrivateComDealer(MyApplication.getContext(),this);
        init();//初始化
    }

    @Override
    protected void onResume() {
        super.onResume();
        privateComPresenter.connectView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        privateComPresenter.destroyView();
    }

    private void init(){
        initView();
        initData();
        initXRefresh();
    }

    /**
     * 初始化视图
     */
    private void initView(){
        initNavBar(true,"历史评论",false);

        //线性布局
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //时间冲突
        recyclerView.setNestedScrollingEnabled(false);

        //装饰
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        privateComAdapter = new PrivateComAdapter(this,recyclerView,comList);
        privateComAdapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(privateComAdapter);

    }

    /**
     * 初始化数据
     */
    private void initData(){
        privateComPresenter.getComHistory();
    }

    private void initXRefresh(){
        xRefreshView.setPullLoadEnable(true);//运行上拉下载数据
        xRefreshView.setAutoLoadMore(false);
        xRefreshView.enableReleaseToLoadMore(true);
        xRefreshView.enableRecyclerViewPullUp(true);
        xRefreshView.enablePullUpWhenLoadCompleted(true);

        //自定义动画

        xRefreshView.setMoveForHorizontal(true);
        //监听器
        xRefreshView.setXRefreshViewListener(myXrVListener);
    }



    @Override
    public void successGet(List<PrivateCom> coms) {
        if (isRefresh){
            isRefresh = false;
            xRefreshView.stopRefresh();
        }
        comList.clear();
        comList.addAll(coms);
        privateComAdapter.notifyDataSetChanged();
    }

    @Override
    public void successCancel() {
        Toast.makeText(this,"删除成功",Toast.LENGTH_SHORT).show();
        initData();
    }

    @Override
    public void failGet() {
        if (isRefresh){
            isRefresh = false;
            xRefreshView.stopRefresh();
        }
        Toast.makeText(this,"获取历史评论失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failCancel() {
        Toast.makeText(this,"删除失败",Toast.LENGTH_SHORT).show();
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
