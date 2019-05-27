package com.meishi.infobean;

import com.meishi.beans.CollectShop;
import com.meishi.beans.Shop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectShopInfo {
    private int code;
    private int count;
    private String msg;
    private List<CollectShop> data;

    public List<Shop> getData(){
        List<Shop> shopList = new ArrayList<>();
        for (CollectShop collectShop : data){
            Shop shop = collectShop.getShop();
            shop.setStar(collectShop.getStar());
            shopList.add(shop);
        }
        Collections.sort(shopList);
        return shopList;
    }

    public List<Integer> getIds(){
        List<Integer> idList = new ArrayList<>();
        for (CollectShop collectShop : data){
            idList.add(collectShop.getShop().getId());
        }
        return idList;
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
