package com.meishi.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.meishi.base.BaseObserver;
import com.meishi.contract.UploadContract;
import com.meishi.helpers.RetrofitHelper;
import com.meishi.infobean.UploadPicInfo;
import com.meishi.services.InfoService;

import java.io.File;
import java.io.FileOutputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadDealer implements UploadContract.UploadPresenter {
    private Context mContext;
    private UploadContract.UploadView uploadView;
    private InfoService service;

    public UploadDealer(Context context, UploadContract.UploadView uploadView){
        this.mContext = context;
        this.uploadView = uploadView;
        service = RetrofitHelper.getInstance(mContext).creatNorService(InfoService.class);
    }


    @Override
    public void connectView(UploadContract.UploadView uploadView) {
        this.uploadView = uploadView;
    }

    @Override
    public void destroyView() {
        this.uploadView = null;
    }

    @Override
    public void uploadPic(Bitmap bitmap) {
        File file = bitmapToFile(bitmap);
        RequestBody img = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part imgPart = MultipartBody.Part.createFormData("files",file.getName(),img);
        service.uploadPic(imgPart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<UploadPicInfo>() {
                    @Override
                    public void success(UploadPicInfo uploadPicInfo) {
                        if(uploadPicInfo.getCode() == 0){
                            if(uploadView != null){
                                Log.d("UploadDealer", "success--url: "+uploadPicInfo.getData());
                                uploadView.successUpload(uploadPicInfo.getMsg(),uploadPicInfo.getData());
                            }
                        }
                    }

                    @Override
                    public void error(Throwable e) {
                        if(uploadView != null){
                            uploadView.failUpload("上传出错");
                        }
                    }
                });
    }

    private File bitmapToFile(Bitmap bitmap){
        String deafultPath = mContext.getApplicationContext().getFilesDir()
                .getAbsolutePath()+ "/defaultGoodInfo";
        File file = new File(deafultPath);
        if(!file.exists()){
            file.mkdirs();
        }
        String defaultImgPath = deafultPath + "/messageImg.jpg";
        file = new File(defaultImgPath);
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,20,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return file;
    }
}
