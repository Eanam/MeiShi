package com.meishi.beans;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * 评论类：
 * comdesc：评论内容
 * star：星级
 * shop_id：商店id
 * good_id：商品id
 */
public class Comment implements Comparable<Comment> {
    private String comdesc;
    private int comid;
    private long comtime;
    private int goodid;
    private String nickname;
    private int  shopid;
    private float star;
    private int status;
    private int userid;

    public String getComdesc() {
        return comdesc;
    }

    public int getComid() {
        return comid;
    }

    public long getComtime() {
        return comtime;
    }

    public int getGoodid() {
        return goodid;
    }

    public String getNickname() {
        return nickname;
    }

    public int getShopid() {
        return shopid;
    }

    public float getStar() {
        return star;
    }

    public int getStatus() {
        return status;
    }

    public int getUserid() {
        return userid;
    }


    @Override
    public int compareTo(@NonNull Comment o) {
        float f = this.star - o.star;
        if (f == 0){
            return this.comid - o.comid;
        }else {
            return (f > 0)?-1:1;
        }
    }
}
