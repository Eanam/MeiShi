package com.meishi.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.meishi.R;
import com.meishi.activities.CommentActivity;
import com.meishi.activities.ShopActivity;
import com.meishi.beans.Good;
import com.meishi.beans.Shop;
import com.meishi.views.StarBar;

import java.util.List;

public class GoodAdapter extends RecyclerView.Adapter<GoodAdapter.ViewHolder> {

    private Context context;
    private View itemView;
    private RecyclerView mRV;
    private boolean isCalHeight = false;
    private List<Good> goodList;
    private OnItemClickListener onItemClickListener;


    public GoodAdapter(Context context,RecyclerView recyclerView,List<Good> list){
        this.context = context;
        this.mRV = recyclerView;
        this.goodList = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        itemView = LayoutInflater.from(context).inflate(R.layout.item_good,viewGroup,false);
        itemView.setOnClickListener(v -> {
            if(onItemClickListener != null){
                onItemClickListener.onItemClick(v, (int) v.getTag());
            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.itemView.setTag(position);
        //设置RecyclerView的高度
        setRecyclerViewHeight();

        viewHolder.goodName.setText(goodList.get(position).getGoodname());

        Glide.with(context)
                .load(goodList.get(position).getPicurl())
                .into(viewHolder.goodPic);

//        viewHolder.goodDesc.setText(goodList.get(position).get);

        viewHolder.starBar.setStarMark(goodList.get(position).getStar());

        viewHolder.price.setText(goodList.get(position).getPrice()+"");
    }

    @Override
    public int getItemCount() {
        return goodList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //List增加时重新计算高度
    public void setCalHeight(boolean reCal){
        this.isCalHeight = !reCal;
    }


    /**
     * 手动计算RecyclerView的高度
     * 步骤：
     * 1.获取ItemView的高度
     * 2.ItemView的数量
     * 3.两者相乘得出RecyclerView的高度
     */
    private void setRecyclerViewHeight(){
        if(isCalHeight){
            return;
        }
        isCalHeight = true;
        //获取item的高度
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)itemView.getLayoutParams();

        //获取items的数量
        int itemCount = getItemCount();

        //设置RecyclerView的高度
        LinearLayout.LayoutParams rvLp = (LinearLayout.LayoutParams)mRV.getLayoutParams();
        rvLp.height = itemCount * layoutParams.height;
        mRV.setLayoutParams(rvLp);
    }

    //点击回调接口
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //图片
        ImageView goodPic;
        //名字
        TextView goodName;
        //评分
        StarBar starBar;
        //描述
        TextView goodDesc;

        //价格
        TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            goodPic = itemView.findViewById(R.id.iv_goodPic);
            goodName = itemView.findViewById(R.id.tv_goodName);
            starBar = itemView.findViewById(R.id.sb_point);
            goodDesc = itemView.findViewById(R.id.tv_goodDesc);
            price = itemView.findViewById(R.id.tv_price);
        }
    }
}
