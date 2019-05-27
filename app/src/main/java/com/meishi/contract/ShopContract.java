package com.meishi.contract;

import com.meishi.beans.Shop;

import java.util.List;

public interface ShopContract {
    public interface ShopView{
        void update(List<Shop> data);//成功时刷新List

        void failUpdate();//失败是提示
    }

    public interface ShopPresenter{
        void getShopList();

        void connectView(ShopView shopView);
        void destoyView();
    }
}
