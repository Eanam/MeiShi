package com.meishi.contract;

import com.meishi.base.BaseView;
import com.meishi.beans.Shop;

import java.util.List;

public interface WelcomeContract {
    public interface WelcomeView extends BaseView {
        void update(List<Integer> data);//成功时刷新List

        void failUpdate();//失败是提示
    }

    public interface WelcomePresnter{
        void getCollect();//获取收藏店铺,for login

        void getCollectForWelcome();

        void connectView(WelcomeView welcomeView);
        void destroyView();
    }
}
