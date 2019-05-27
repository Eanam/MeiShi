package com.meishi.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyViewPagerAdapters extends FragmentPagerAdapter {

    //接收传入的多个fragment
    private List<Fragment> mFragmentList;


    public MyViewPagerAdapters(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);

        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
