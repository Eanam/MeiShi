package com.meishi.beans;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 商店类：
 * id:商店id
 * shopName：商店名
 * pro：省份
 * address：商店地址
 * city：所在城市
 * county：省份
 * area：所在区域
 * status：状态
 */
public class Shop implements Serializable, Comparable<Shop> {
    private int id;
    private String shopname;
    private String shoppic;
    private String address;
    private String area;
    private String city;
    private String county;
    private String pro;
    private float star ;
    private String status;
    private List<Good> goodList;

    public Shop(){ }

//    public Shop(int id, String shopName, String pic, String address, float star, String desc) {
//        this.id = id;
//        this.shopName = shopName;
//        this.pic = pic;
//        this.address = address;
//        this.star = star;
//        this.desc = desc;
//        goodList = new ArrayList<>();
//    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShoppic() {
        return shoppic;
    }

    public void setShoppic(String shoppic) {
        this.shoppic = shoppic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Good> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<Good> goodList) {
        this.goodList = goodList;
    }



    @Override
    public int compareTo(@NonNull Shop o) {
        float f = this.star - o.star;
        if (f == 0){
            return this.id - o.id;
        }else {
            return (f > 0)?-1:1;
        }
    }
}
