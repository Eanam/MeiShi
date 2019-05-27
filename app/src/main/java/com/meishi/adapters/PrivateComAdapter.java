package com.meishi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.meishi.R;
import com.meishi.beans.PrivateCom;

import java.util.List;

public class PrivateComAdapter extends BaseRecyclerAdapter<PrivateComAdapter.ViewHolder> {
    private Context mContext;
    private View itemview;
    private RecyclerView recyclerView;
    private boolean isCalHeight = false;
    private List<PrivateCom> comList;
    private OnItemClickListener onItemClickListener;

    public PrivateComAdapter(Context context,RecyclerView recyclerView,List<PrivateCom> coms){
        this.mContext = context;
        this.recyclerView = recyclerView;
        this.comList = coms;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        itemview = LayoutInflater.from(mContext).inflate(R.layout.item_private_com,parent,false);
        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, boolean isItem) {
        holder.itemView.setTag(position);

        holder.tv_shopname.setText(comList.get(position).getShop().getShopname());
        Glide.with(mContext)
                .load(comList.get(position).getGood().getPicurl())
                .into(holder.iv_goodPic);

        holder.tv_goodname.setText(comList.get(position).getGood().getGoodname());
        holder.tv_comDesc.setText(comList.get(position).getCom().getComdesc());
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v,position);
                }
            }
        });
    }

    @Override
    public int getAdapterItemCount() {
        return comList.size();
    }

    /**
     * 回调接口
     */
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_shopname;
        private ImageView iv_goodPic;
        private Button bt_delete;
        private TextView tv_goodname;
        private TextView tv_comDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_shopname = itemView.findViewById(R.id.tv_shopName);
            bt_delete = itemView.findViewById(R.id.bt_delete);
            iv_goodPic = itemView.findViewById(R.id.iv_goodPic);
            tv_goodname = itemView.findViewById(R.id.tv_goodName);
            tv_comDesc = itemView.findViewById(R.id.tv_comDesc);
        }
    }
}
