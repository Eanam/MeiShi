package com.meishi.contract;

import com.meishi.base.BaseView;
import com.meishi.beans.Comment;
import com.meishi.beans.Good;

import java.util.List;

public interface CommentContract {
    public interface CommentView extends BaseView {
        int getGoodid();
        void update(List<Comment> data);//成功时刷新List

        void failUpdate();//失败是提示
    }

    public interface CommentPresenter{
        void getCommentList();

        void connectView(CommentView commentView);
        void destoyView();
    }
}
