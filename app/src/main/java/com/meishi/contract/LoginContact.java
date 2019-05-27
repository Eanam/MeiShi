package com.meishi.contract;


import com.meishi.base.BaseView;
import com.meishi.infobean.InfoBean;

/**
 * 登录接口
 */
public interface LoginContact{
    interface LoginView extends BaseView {

        String getUsername();
        String getPassword();

        void setLoginResult(int result);

        void showProgress();//网络请求时的进度条

        void progressUpdate();//网络请求结束后关闭进度条以及更新结果
    }

    interface LoginPresenter{
        void Login();
    }
}
