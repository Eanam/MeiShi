package com.meishi.contract;

import com.meishi.base.BaseView;

public interface AddComContract {
    public interface AddComView extends BaseView {
        float getStar();
        int getShopId();
        int getGoodId();
        String getComDesc();

        void setSubmitResult(int result);

        void startSubmit();
        void finishSubmit();

    }

    public interface AddComPresenter{
        void submitComment();
    }
}
