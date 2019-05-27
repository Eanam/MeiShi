package com.meishi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bumptech.glide.Glide;
import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.R;
import com.meishi.adapters.GoodAdapter;
import com.meishi.adapters.ShopAdapter;
import com.meishi.beans.Good;
import com.meishi.beans.Shop;
import com.meishi.contract.GoodContract;
import com.meishi.contract.SubCollectContract;
import com.meishi.helpers.DatabaseHelper;
import com.meishi.presenter.GoodListDealer;
import com.meishi.presenter.SubCollectDealer;
import com.meishi.views.StarBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopActivity extends BaseActivity implements GoodContract.GoodView, SubCollectContract.SubCollectView {

    private List<Good> goodList = new ArrayList<>();
    private GoodAdapter goodAdapter;
    private Shop shop ;
    private GoodContract.GoodPresenter goodPresenter;
    private SubCollectContract.SubCollectPresenter subCollectPresenter;
    private GoodAdapter.OnItemClickListener onItemClickListener;
    @BindView(R.id.rv_content) RecyclerView mRv_goodList;
    @BindView(R.id.iv_shopPic) ImageView shopPic;
    @BindView(R.id.tv_name) TextView shopName;
    @BindView(R.id.shop_points) StarBar shopPoint;
    @BindView(R.id.shop_location) TextView shopLocation;
    @BindView(R.id.iv_me) ImageView iv_collect;
    @BindView(R.id.xrv_shop)XRefreshView xRefreshView;
    private boolean isNotCollect = true;
    private boolean isRefresh = false;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        shop = (Shop)getIntent().getExtras().getSerializable(AppConstants.SHOP_TO_GET_GOOD);
        ButterKnife.bind(this);
        goodPresenter = new GoodListDealer(MyApplication.getContext(),this);
        subCollectPresenter = new SubCollectDealer(MyApplication.getContext(),this);
        databaseHelper = new DatabaseHelper(MyApplication.getContext());
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        goodPresenter.connectView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        goodPresenter.destoyView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }

    //初始化UI
    private void initView(){
        //设置NavigationBar
        initNavBar(true , shop.getShopname(), true);
        //提交操作
        setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShopActivity", "onClick: "+isNotCollect);
                if (isNotCollect) {
                    //提交收藏
                    subCollectPresenter.submitCollect();
                }else {
                    //取消收藏
                    subCollectPresenter.cancelCollect();
                }
            }
        });
        shopName.setText(shop.getShopname());
        Glide.with(this)
                .load(shop.getShoppic())
                .into(shopPic);
        shopPoint.setStarMark(shop.getStar());
        String location = getShopLocation(shop);
        shopLocation.setText(location);

        //判断是否是收藏的店铺
        isNotCollect = databaseHelper.queryData(getShopId());
        iv_collect.setActivated(isNotCollect);

    }

    /**
     * 获取地址
     * @param shop
     * @return
     */
    private String getShopLocation(Shop shop){
        if (shop == null){
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (shop.getCity() != null){
            stringBuffer.append(shop.getCity());
        }
        if (shop.getCounty() != null){
            stringBuffer.append(shop.getCounty());
        }
        if (shop.getArea() != null){
            stringBuffer.append(shop.getArea());
        }
        if (shop.getAddress() != null){
            stringBuffer.append(shop.getAddress());
        }
        return stringBuffer.toString();
    }

    /**
     * 初始化视图
     */
    private void init(){
        initView();
        intXRefreshView();
        initData();
        //配置RecycleView

        //线性布局
        mRv_goodList.setLayoutManager(new LinearLayoutManager(this));

        //时间冲突
        mRv_goodList.setNestedScrollingEnabled(false);

        //装饰
        mRv_goodList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        onItemClickListener = new GoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(ShopActivity.this,CommentActivity.class);
                Bundle bundle = new Bundle();
                //菜品的具体信息
                bundle.putInt(AppConstants.GOOD_TO_GET_COMMENT_2,shop.getId());
                bundle.putSerializable(AppConstants.GOOD_TO_GET_COMMENT_1,goodList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };

        //绑定adapter
        goodAdapter = new GoodAdapter(this, mRv_goodList,goodList);
        goodAdapter.setOnItemClickListener(onItemClickListener);
        mRv_goodList.setAdapter(goodAdapter);

    }

    private void intXRefreshView() {

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

                    xRefreshView.stopLoadMore();
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


    private void initData() {
        //获取网络数据
        goodPresenter.getGoodList();
//        goodList.add(new Good(1,1,"菠菜","好吃的菠菜",12.0,(float)2.7,"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1203702746,2129121430&fm=85&app=52&f=JPEG?w=121&h=75&s=48E89C525D916BC40FB4BA640300A064"));
//        goodList.add(new Good(2,2,"可乐","可口可乐",2.0,(float)4.2,"https://paimgcdn.baidu.com/968A8C017505E5D6?src=http%3A%2F%2Fms.bdimg.com%2Fdsp-image%2F1949500860.jpg&rz=urar_2_968_600&v=0"));
//        goodList.add(new Good(3,1,"番茄","好吃的菠菜",5.0,(float)2.1,"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1203702746,2129121430&fm=85&app=52&f=JPEG?w=121&h=75&s=48E89C525D916BC40FB4BA640300A064"));
//        goodList.add(new Good(4,1,"苹果","好吃的菠菜",5.0,(float)3.2,"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1203702746,2129121430&fm=85&app=52&f=JPEG?w=121&h=75&s=48E89C525D916BC40FB4BA640300A064"));
//        goodList.add(new Good(5,1,"梨","好吃的菠菜",8.0,(float)4.2,"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1203702746,2129121430&fm=85&app=52&f=JPEG?w=121&h=75&s=48E89C525D916BC40FB4BA640300A064"));


    }


    @Override
    public int getShopid() {
        return shop.getId();
    }

    @Override
    public void update(List<Good> data) {
        if(isRefresh){
            isRefresh = false;
            xRefreshView.stopRefresh();
        }
        this.goodList.clear();
        this.goodList.addAll(data);
        this.goodAdapter.notifyDataSetChanged();
    }

    @Override
    public void failUpdate() {
        if(isRefresh){
            isRefresh = false;
            xRefreshView.stopRefresh();
        }
        Toast.makeText(this,"获取商品失败",Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getShopId() {
        return shop.getId();
    }

    @Override
    public void successCollect(String msg) {
        //成功收藏后的操作
        isNotCollect = false;
        iv_collect.setActivated(isNotCollect);
        databaseHelper.insertData(getShopId());
        Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failCollect(String msg) {
        //提示收藏失败
        Toast.makeText(this,"收藏失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successCancel(String msg) {
        //成功收藏后的操作
        isNotCollect = true;
        iv_collect.setActivated(isNotCollect);
        databaseHelper.deleteData(getShopId());
        Toast.makeText(this,"取消成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failCancel(String msg) {
        //提示收藏失败
        Toast.makeText(this,"取消失败",Toast.LENGTH_SHORT).show();
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
