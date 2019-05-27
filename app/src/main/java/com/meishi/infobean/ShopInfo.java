package com.meishi.infobean;

import com.meishi.beans.Shop;

import java.util.Collections;
import java.util.List;

public class ShopInfo {
    private int code;
    private int count;
    private String msg;
    private List<Shop> data;

    public List<Shop> getData(){
        Collections.sort(data);
        return data;
    }

    public int getCode() {
        return code;
    }

    public int getCount() {
        return count;
    }

    public String getMsg() {
        return msg;
    }
}
