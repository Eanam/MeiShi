package com.meishi.infobean;

import com.meishi.beans.Comment;

import java.util.Collections;
import java.util.List;

public class CommentInfo {
    private int code;
    private int count;
    private String msg;
    private List<Comment> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Comment> getData() {
        Collections.sort(data);
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }
}
