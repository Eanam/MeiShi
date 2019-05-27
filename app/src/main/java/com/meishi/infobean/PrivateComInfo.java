package com.meishi.infobean;

import android.util.Log;

import com.meishi.beans.Comment;
import com.meishi.beans.Good;
import com.meishi.beans.PrivateCom;
import com.meishi.beans.Shop;

import java.util.List;

public class PrivateComInfo {
    private int code;
    private int count;
    private String msg;
    List<PrivateCom> data;

    public int getCode() {
        return code;
    }

    public int getCount() {
        return count;
    }

    public String getMsg() {
        return msg;
    }

    public List<PrivateCom> getData() {
        Log.d("PrivateComInfo", "getData: "+data.size()+" ");
        return data;
    }

}
