package com.meishi.contract;

import com.meishi.base.BaseView;
import com.meishi.beans.Good;

import java.util.List;

public interface GoodContract {
    public interface GoodView extends BaseView{
        int getShopid();//获取店铺id来获取该店铺的菜品

        void update(List<Good> data);//成功时刷新List

        void failUpdate();//失败是提示
    }

    public interface GoodPresenter{
        void getGoodList();

        void connectView(GoodView goodView);
        void destoyView();
    }
}
