package com.meishi.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;

import com.meishi.R;
import com.meishi.adapters.MyViewPagerAdapters;
import com.meishi.fragments.CollectionFrag;
import com.meishi.fragments.MeFrag;
import com.meishi.fragments.ShopFrag;
import com.meishi.helpers.BottomNavigationViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    //请求识别码分别为（本地相册、相机、图片裁剪)
    private static final int CODE_PHOTO_REQUEST = 1;
    private static final int CODE_CAMERA_REQUEST = 2;
    private static final int CODE_PHOTO_CLIP = 3;

    @BindView(R.id.navigation)BottomNavigationView navigationView;//底部导航栏
    @BindView(R.id.viewPager) ViewPager viewPager;//中间容器

    private MenuItem menuItem;//导航栏指定栏目

    private List<Fragment> mFragmentList = new ArrayList<>();//存放三个fragments
    private MyViewPagerAdapters pagerAdapters;
    private ShopFrag shopFrag;
    private CollectionFrag collectionFrag;
    private MeFrag meFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();


        //底部导航栏取消移位动画
//        BottomNavigationViewHelper.disableShiftMode(navigationView);

        //给viewpager和底部导航栏设置监听器
        navigationView.setOnNavigationItemSelectedListener(myNavvigationItemListener);
        viewPager.addOnPageChangeListener(myViewPagerListener);


        //添加fragments
        shopFrag = new ShopFrag();
        collectionFrag = new CollectionFrag();
        meFrag = new MeFrag();
        mFragmentList.add(shopFrag);
        mFragmentList.add(collectionFrag);
        mFragmentList.add(meFrag);

        //为viewpager添加adapters
        pagerAdapters = new MyViewPagerAdapters(getSupportFragmentManager(),mFragmentList);
        viewPager.setAdapter(pagerAdapters);
    }

    private void initView(){
        //设置NavigationBar
        initNavBar(false , "美食点评", false);
    }


    //处理返回后的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //导航栏监听器
    private BottomNavigationView.OnNavigationItemSelectedListener myNavvigationItemListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    menuItem = item;
                    int position = menuItem.getItemId();
                    switch (position){
                        case R.id.navigation_shop:
                            viewPager.setCurrentItem(0);
                            return true;

                        case R.id.navigation_collection:
                            viewPager.setCurrentItem(1);
                            return true;

                        case R.id.navigation_me:
                            viewPager.setCurrentItem(2);
                            return true;
                    }

                    return false;
                }
            };


    //ViewPager监听器
    private ViewPager.OnPageChangeListener myViewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(menuItem != null){
                menuItem.setChecked(false);
            }else{
                //默认打开的是第一个
                navigationView.getMenu().getItem(0).setChecked(false);
            }
            menuItem = navigationView.getMenu().getItem(position);//获取要打开的item，方便后续操作
            menuItem.setChecked(true);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}

