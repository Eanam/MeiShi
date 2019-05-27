package com.meishi.contract;

import com.meishi.base.BaseView;

public interface SubCollectContract {
    public interface SubCollectView extends BaseView{
        int getShopId();

        void successCollect(String msg);//成功收藏

        void failCollect(String msg);//收藏失败

        void successCancel(String msg);

        void failCancel(String msg);
    }

    public interface SubCollectPresenter{
        void submitCollect();//提交收藏店铺

        void cancelCollect();//取消收藏
    }
}
