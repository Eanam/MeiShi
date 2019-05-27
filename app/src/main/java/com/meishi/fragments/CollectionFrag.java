package com.meishi.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.R;
import com.meishi.activities.LoginActivity;
import com.meishi.activities.ShopActivity;
import com.meishi.adapters.ShopAdapter;
import com.meishi.beans.Shop;
import com.meishi.contract.CollectContract;
import com.meishi.presenter.CollectDealer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionFrag extends Fragment implements CollectContract.CollectView {
    @BindView(R.id.xrv_content)
    XRefreshView mXrv;//上下拉刷新框架
    @BindView(R.id.rv_content)
    RecyclerView mRv_shopList;
    private ShopAdapter shopAdapter;
    private ShopAdapter.OnItemClickListener onItemClickListener;
    private List<Shop> shops = new ArrayList<>();
    private CollectContract.CollectPresenter collectPresenter;
    private Context mContext;

    private  boolean isRefresh = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection,container,false);
        ButterKnife.bind(this,view);
        collectPresenter = new CollectDealer(MyApplication.getContext(),this);
        mContext = this.getContext();
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        collectPresenter.connectView(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        collectPresenter.destoyView();
    }

    /**
     * 初始化视图
     */
    private void initView(){
        initXRefreshView();
        initData();
        //配置RecycleView

        //线性布局
        mRv_shopList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        //时间冲突
        mRv_shopList.setNestedScrollingEnabled(false);

        //装饰
        mRv_shopList.addItemDecoration(new DividerItemDecoration(this.getContext(),DividerItemDecoration.VERTICAL));

        //自定义点击事件
        onItemClickListener = new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, ShopActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstants.SHOP_TO_GET_GOOD,shops.get(position));
                bundle.putBoolean(AppConstants.COLLECT_FLAG,false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };

        //绑定adapter
        shopAdapter = new ShopAdapter(getContext(),mRv_shopList,shops);
        shopAdapter.setOnItemClickListener(onItemClickListener);
        shopAdapter.setCustomLoadMoreView(new XRefreshViewFooter(getActivity()));
        mRv_shopList.setAdapter(shopAdapter);

    }

    /**
     *     上下拉刷新框架的初始化
     */

    private void initXRefreshView(){
        //默认只能下拉刷新

        mXrv.setPullLoadEnable(true);//运行上拉下载数据
        mXrv.setAutoLoadMore(false);
        mXrv.enableReleaseToLoadMore(true);
        mXrv.enableRecyclerViewPullUp(true);
        mXrv.enablePullUpWhenLoadCompleted(true);

        //自定义动画

        mXrv.setMoveForHorizontal(true);
        //监听器
        mXrv.setXRefreshViewListener(myXrVListener);
    }



    //自定义XRefreshView的监控器
    private XRefreshView.XRefreshViewListener myXrVListener = new XRefreshView.XRefreshViewListener() {
        @Override
        public void onRefresh() {
            isRefresh = true;
            initData();
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

                    mXrv.stopLoadMore();
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



    //初始化数据，用于测试
    private void initData(){
        Log.d("CFG", "initData: ----------"+isRefresh);
        collectPresenter.getCollectList();
//        shops.add(new Shop(1,"黄焖鸡","这是一家黄焖鸡","https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1628498222,3505106306&fm=58&bpow=800&bpoh=533","Nanjing","China","qixia","1"));
//        shops.add(new Shop(2,"黄焖鸭","这是一家黄焖鸭","https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1440037894,4084043816&fm=27&gp=0.jpg","Nanjing","China","qixia","2"));
    }

    @Override
    public void update(List<Shop> data) {
        if(isRefresh){
            isRefresh = false;
            mXrv.stopRefresh();
        }
        shops.clear();
        shops.addAll(data);
        shopAdapter.notifyDataSetChanged();
    }

    @Override
    public void failUpdate() {
        if(isRefresh){
            isRefresh = false;
            mXrv.stopRefresh();
        }
        Toast.makeText(this.getContext(),"获取失败",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void logout(String msg) {
        Toast.makeText(this.getContext(),msg,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this.getContext(), LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.writeToSP(AppConstants.TOKEN,"");
        startActivity(intent);

        ((Activity)getContext()).overridePendingTransition(R.anim.open_enter,R.anim.open_exit);
    }
}
