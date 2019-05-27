package com.meishi.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.meishi.AppConstants;
import com.meishi.R;
import com.meishi.activities.ShopActivity;
import com.meishi.beans.Shop;
import com.meishi.views.StarBar;

import java.util.List;

public class ShopAdapter extends BaseRecyclerAdapter<ShopAdapter.ViewHolder> {

//    private int largeCardHeight, smallCardHeight;

    private Context context;
    private View itemView;
    private RecyclerView mRV;
    private boolean isCalHeight = false;
    private List<Shop> shopList;
    private OnItemClickListener onItemClickListener;

    public ShopAdapter(Context context,RecyclerView recyclerView,List<Shop> list){
        this.context = context;
        this.mRV = recyclerView;
        this.shopList = list;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        itemView = LayoutInflater.from(context).inflate(R.layout.item_shop,parent,false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v, (int) v.getTag());
                }
            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, boolean isItem) {
        holder.itemView.setTag(position);
        holder.shopName.setText(shopList.get(position).getShopname());
//        holder.shopDesc.setText(shopList.get(position).getDesc());
        holder.starBar.setStarMark(shopList.get(position).getStar());
        //加载图片
        Glide.with(context)//传入上下文
                .load(shopList.get(position).getShoppic())//传入图片链接
                .into(holder.shopPic);//传入显示图片的ImageView


        holder.shopDesc.setText(getShopLocation(shopList.get(position)));

    }

    @Override
    public int getAdapterItemCount() {
        return shopList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //店铺图片
        ImageView shopPic;
        //店铺名字
        TextView shopName;
        //店铺评分
        StarBar starBar;
        //店铺描述
        TextView shopDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shopPic = itemView.findViewById(R.id.iv_shopPic);
            shopName = itemView.findViewById(R.id.tv_shopName);
            starBar = itemView.findViewById(R.id.sb_point);
            shopDesc = itemView.findViewById(R.id.tv_shopDesc);
        }
    }

    /**
     * 获取地址
     * @param shop
     * @return
     */
    private String getShopLocation(Shop shop){
        if (shop == null){
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (shop.getCity() != null){
            stringBuffer.append(shop.getCity());
        }
        if (shop.getCounty() != null){
            stringBuffer.append(shop.getCounty());
        }
        if (shop.getArea() != null){
            stringBuffer.append(shop.getArea());
        }
        if (shop.getAddress() != null){
            stringBuffer.append(shop.getAddress());
        }
        return stringBuffer.toString();
    }
}
