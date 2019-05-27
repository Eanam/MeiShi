package com.meishi.contract;

import android.graphics.Bitmap;

public interface UploadContract {
    public interface UploadView{
        void successUpload(String msg,String url);

        void failUpload(String msg);
    }
    public interface UploadPresenter{

        void connectView(UploadView uploadView);
        void destroyView();

        void uploadPic(Bitmap bitmap);
    }
}
