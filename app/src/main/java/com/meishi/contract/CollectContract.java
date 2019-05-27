package com.meishi.contract;

import com.meishi.base.BaseView;
import com.meishi.base.IView;
import com.meishi.beans.Shop;

import java.util.List;

public interface CollectContract {
    public interface CollectView extends IView {
        void update(List<Shop> data);//成功时刷新List

        void failUpdate();//失败是提示
    }

    public interface CollectPresenter{

        void getCollectList();//获取收藏店铺

        void connectView(CollectView collectView);
        void destoyView();
    }
}
