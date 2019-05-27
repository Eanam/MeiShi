package com.meishi.beans;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 商品类
 * id：商品id
 * shop_id：商店id
 * goodName：商品名字
 * desc：商品描述
 * price：商品价格
 */
public class Good implements Serializable, Comparable<Good> {
    private int id;
//    private int shop_id;
    private String goodname;
//    private String desc;
    private double price;
    private float star;
    private String picurl;
    private List<Comment> comments;


    public Good(){}

//    public Good(int id, int shop_id, String goodName, String desc, double price, float star, String goodPic) {
//        this.id = id;
//        this.shop_id = shop_id;
//        this.goodName = goodName;
//        this.desc = desc;
//        this.price = price;
//        this.star = star;
//        this.goodPic = goodPic;
//        comments = new ArrayList<>();
//    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }



    @Override
    public int compareTo(@NonNull Good o) {
        float f = this.star - o.star;
        if (f == 0){
            return this.id - o.id;
        }else {
            return (f > 0)?-1:1;
        }
    }
}
