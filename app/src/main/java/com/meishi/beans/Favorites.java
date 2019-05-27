package com.meishi.beans;

/**
 * shop：店铺
 * isFavor：判断是否是收藏的店铺
 */
public class Favorites {

    private Shop shop;
    private boolean isFavor = true;

    public Favorites(){}

    public Favorites(Shop shop) {
        this.shop = shop;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public boolean isFavor() {
        return isFavor;
    }

    public void setFavor(boolean favor) {
        isFavor = favor;
    }
}
