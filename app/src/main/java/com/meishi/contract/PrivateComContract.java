package com.meishi.contract;

import com.meishi.base.BaseView;
import com.meishi.beans.PrivateCom;

import java.util.List;

public interface PrivateComContract {
    interface PrivateComView extends BaseView {
        void successGet(List<PrivateCom> coms);

        void successCancel();

        void failGet();

        void failCancel();
    }

    interface PrivateComPresenter{
        void getComHistory();

        void cancelComment(int comid);

        void connectView(PrivateComView privateComView);
        void destroyView();
    }
}
