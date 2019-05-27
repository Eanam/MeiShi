package com.meishi.fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.R;
import com.meishi.activities.ShopActivity;
import com.meishi.adapters.ShopAdapter;
import com.meishi.beans.Shop;
import com.meishi.contract.ShopContract;
import com.meishi.presenter.ShopListDealer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopFrag extends Fragment implements ShopContract.ShopView {

    @BindView(R.id.xrv_content) XRefreshView mXrv;//上下拉刷新框架
    @BindView(R.id.rv_content) RecyclerView mRv_shopList;
    private ShopAdapter shopAdapter;
    private List<Shop> shops = new ArrayList<>();
    private ShopAdapter.OnItemClickListener onItemClickListener;
    private Context mContext;
    private boolean isRefresh = false;

    ShopContract.ShopPresenter mPresenter ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.xrv_shop,container,false);
        ButterKnife.bind(this,view);
        mPresenter = new ShopListDealer(MyApplication.getContext(),this);
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
        mPresenter.connectView(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.destoyView();
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
        mRv_shopList.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));

        //自定义点击事件
        onItemClickListener = new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, ShopActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstants.SHOP_TO_GET_GOOD,shops.get(position));
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
        mPresenter.getShopList();
//        shops.add(new Shop(1,"黄焖鸡","https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1628498222,3505106306&fm=58&bpow=800&bpoh=533","仙林大学城",1,"这是一家黄焖鸡"));
//        shops.add(new Shop(1,"黄焖鸡","https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1628498222,3505106306&fm=58&bpow=800&bpoh=533","仙林大学城", (float) 2.7,"这是一家黄焖鸡"));
//        shops.add(new Shop(1,"黄焖鸡","https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1628498222,3505106306&fm=58&bpow=800&bpoh=533","仙林大学城",(float) 2.7,"这是一家黄焖鸡"));
//        shops.add(new Shop(1,"黄焖鸡","https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1628498222,3505106306&fm=58&bpow=800&bpoh=533","仙林大学城",(float) 2.7,"这是一家黄焖鸡"));
//        shops.add(new Shop(1,"黄焖鸡","https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1628498222,3505106306&fm=58&bpow=800&bpoh=533","仙林大学城",(float) 2.7,"这是一家黄焖鸡"));
//        shops.add(new Shop(1,"黄焖鸡","https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1628498222,3505106306&fm=58&bpow=800&bpoh=533","仙林大学城",(float) 4.5,"这是一家黄焖鸡"));
    }

    @Override
    public void update(List<Shop> data) {
        if(isRefresh){
            isRefresh = false;
            mXrv.stopRefresh();
        }
        shops.addAll(data);
        shopAdapter.notifyDataSetChanged();
    }

    @Override
    public void failUpdate() {
        if(isRefresh){
            isRefresh = false;
            mXrv.stopRefresh();
        }
        Toast.makeText(this.getContext(),"获取数据失败",Toast.LENGTH_SHORT).show();
    }
}
