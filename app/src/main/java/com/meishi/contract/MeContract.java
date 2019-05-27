package com.meishi.contract;

import com.meishi.base.BaseView;
import com.meishi.base.IView;
import com.meishi.infobean.MeInfo;

public interface MeContract {
    public interface MeView extends IView {
        void update(MeInfo data);//成功时刷新List

        void failUpdate();//失败是提示
    }

    public interface MePresenter{
        void getProfile();//获取个人资料

        void connectView(MeView meView);
        void destoyView();
    }
}
