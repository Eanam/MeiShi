package com.meishi.beans;

import java.io.Serializable;
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
public class CollectShop implements Serializable {
    private float star;
    private Shop shop;

    public CollectShop(){ }

    public float getStar() {
        return star;
    }

    public Shop getShop() {
        return shop;
    }
}
