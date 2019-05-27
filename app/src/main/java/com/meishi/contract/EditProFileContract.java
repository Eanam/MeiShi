package com.meishi.contract;

import com.meishi.base.IView;

public interface EditProFileContract {
    public interface EditProfileView extends IView {
        void successEdit();

        void failEdit();
    }
    public interface EditProfilePresenter{

        void connectView(EditProfileView editProfileView);
        void destroyView();

        void editPrifile(int type,String editContent);
    }
}
