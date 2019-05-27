package com.meishi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.meishi.R;
import com.meishi.beans.Comment;
import com.meishi.utils.TimeUtils;
import com.meishi.views.StarBar;

import org.w3c.dom.Text;

import java.util.List;

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private Context context;
    private RecyclerView mRv;
    private View itemView;
    private boolean isCalHeight = false;
    private List<Comment> commentList;

    public CommentAdapter(Context context,RecyclerView recyclerView,List<Comment> list){
        this.context = context;
        this.mRv = recyclerView;
        this.commentList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        itemView = LayoutInflater.from(context).inflate(R.layout.item_comment,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        setRecyclerViewHeight();
        viewHolder.commenter.setText(commentList.get(position).getNickname());
        viewHolder.points.setStarMark(commentList.get(position).getStar());
        viewHolder.commentContent.setText(commentList.get(position).getComdesc());
        viewHolder.commentDate.setText(TimeUtils.getStringDate(commentList.get(position).getComtime()));
    }



    @Override
    public int getItemCount() {
        return commentList.size();
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
        LinearLayout.LayoutParams rvLp = (LinearLayout.LayoutParams)mRv.getLayoutParams();
        rvLp.height = itemCount * layoutParams.height;
        mRv.setLayoutParams(rvLp);
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        //评论人id
        TextView commenter;
        //评分
        StarBar points;
        //评论内容
        TextView commentContent;
        //描述
        TextView commentDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            commenter = itemView.findViewById(R.id.tv_commenter);
            points = itemView.findViewById(R.id.comment_point);
            commentContent = itemView.findViewById(R.id.comment_content);
            commentDate = itemView.findViewById(R.id.comment_date);
        }
    }
}
