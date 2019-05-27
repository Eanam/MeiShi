package com.meishi.presenter;

import android.content.Context;

import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.base.BaseObserver;
import com.meishi.contract.EditProFileContract;
import com.meishi.helpers.RetrofitHelper;
import com.meishi.infobean.SubmitInfo;
import com.meishi.services.InfoService;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class EditProfileDealer implements EditProFileContract.EditProfilePresenter {
    private Context mContext;
    private EditProFileContract.EditProfileView editProfileView;
    private InfoService service;

    public EditProfileDealer(Context context, EditProFileContract.EditProfileView editProfileView){
        this.mContext = context;
        this.editProfileView = editProfileView;
        service = RetrofitHelper.getInstance(mContext).creatNorService(InfoService.class);
    }


    @Override
    public void connectView(EditProFileContract.EditProfileView editProfileView) {
        this.editProfileView = editProfileView;
    }

    @Override
    public void destroyView() {
        this.editProfileView = null;
    }

    @Override
    public void editPrifile(int type, String editContent) {
        Observable<SubmitInfo> observable = getEditType(type,editContent);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<SubmitInfo>() {
                    @Override
                    public void success(SubmitInfo submitInfo) {
                        if(submitInfo.getCode() == 0 && editProfileView != null){//成功
                            editProfileView.successEdit();
                        }else if(submitInfo.getCode() == 1 && editProfileView != null){
                            editProfileView.failEdit();
                        }else if(submitInfo.getCode() == 1001 && editProfileView != null){
                            editProfileView.logout(submitInfo.getMsg());
                        }
                    }

                    @Override
                    public void error(Throwable e) {
                        if (editProfileView != null){
                            editProfileView.failEdit();
                        }
                    }
                });
    }

    /**
     * 根据修改类型获取对应接口
     */
    private Observable getEditType(int type,String editContent){
        Observable<SubmitInfo> observable = null;
        switch (type){
            case AppConstants
                    .EDIT_HEADPIC:
                    observable = service.editHeadPic(MyApplication.readFromSp(AppConstants.TOKEN),editContent);
                break;

            case AppConstants
                    .EDIT_NICKNAME:
                    observable = service.editNickname(MyApplication.readFromSp(AppConstants.TOKEN),editContent);
                break;

            case AppConstants
                    .EDIT_SEX:
                observable = service.editSex(MyApplication.readFromSp(AppConstants.TOKEN),Integer.parseInt(editContent));
                break;

            case AppConstants
                    .EDIT_BIRTHDATE:
                observable = service.editBirthdate(MyApplication.readFromSp(AppConstants.TOKEN),editContent);
                break;

            case AppConstants
                    .EDIT_DESC:
                observable = service.editDesc(MyApplication.readFromSp(AppConstants.TOKEN),editContent);
                break;

            default:

                break;
        }
        return observable;
    }
}
