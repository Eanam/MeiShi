package com.meishi.contract;

import com.meishi.base.BaseView;

public interface RegistContract {
    public interface RegistView extends BaseView {
        String getUsername();
        String getPassword();

        void setRegistResult(int result);

        void showProgress();//网络请求时的进度条

        void progressUpdate();//网络请求结束后关闭进度条以及更新结果
    }

    public interface RegistPresenter {
        void regist();
    }
}
