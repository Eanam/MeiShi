package com.meishi.presenter;

import android.content.Context;

import com.meishi.AppConstants;
import com.meishi.MyApplication;
import com.meishi.beans.Comment;
import com.meishi.cache.EnhancedCall;
import com.meishi.cache.EnhancedCallback;
import com.meishi.contract.CommentContract;
import com.meishi.helpers.RetrofitHelper;
import com.meishi.infobean.CommentInfo;
import com.meishi.services.InfoService;

import retrofit2.Call;
import retrofit2.Response;

public class CommentDealer implements CommentContract.CommentPresenter {
    private Context mContext;
    private CommentContract.CommentView commentView;
    private InfoService service;

    public CommentDealer(Context context, CommentContract.CommentView commentView){
        this.mContext = context;
        this.commentView = commentView;
        service = RetrofitHelper.getInstance(mContext).creatSpService(InfoService.class);
    }

    @Override
    public void getCommentList() {
        Call<CommentInfo> call = service.getCommentList(MyApplication.readFromSp(AppConstants.TOKEN),commentView.getGoodid());
        EnhancedCall<CommentInfo> enhancedCall = new EnhancedCall<>(call);
        enhancedCall.useCache(true)
                .dataClassName(CommentInfo.class)
                .enqueue(new EnhancedCallback<CommentInfo>() {
                    @Override
                    public void onResponse(Call<CommentInfo> call, Response<CommentInfo> response) {
                        if(commentView != null){
                            if(response.body() != null){
                                if(response.body().getCode() == 1001){
                                    commentView.logout(response.body().getMsg());
                                }else {
                                    commentView.update(response.body().getData());
                                }
                            }else{
                                commentView.failUpdate();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentInfo> call, Throwable t) {
                        if(commentView != null){
                            commentView.failUpdate();
                        }
                    }

                    @Override
                    public void onGetCache(CommentInfo commentInfo) {
                        if(commentView != null){
                            commentView.update(commentInfo.getData());
                        }
                    }
                });
    }

    @Override
    public void connectView(CommentContract.CommentView commentView) {
        this.commentView = commentView;
    }

    @Override
    public void destoyView() {
        this.commentView = null;
    }
}
