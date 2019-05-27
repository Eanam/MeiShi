package com.meishi.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.meishi.R;

public class TextBar extends FrameLayout {
    //声明与自定义属性相对应的变量\
    private String intro;
    private String content;
    private Boolean enableEdit;

    //控件
    private View mView;
    private TextView introText;
    private TextView contentText;
    private ImageView edit;

    public TextBar(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public TextBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public TextBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TextBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    /**
     * 初始化方法
     * @param context 上下文组件
     * @param attrs  自定义变脸
     */
    private void init(Context context,AttributeSet attrs){
        if(attrs == null){
            return;
        }

        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.textBar);
        intro = typedArray.getString(R.styleable.textBar_inroText);
        content = typedArray.getString(R.styleable.textBar_contentText);
        enableEdit = typedArray.getBoolean(R.styleable.textBar_enableEdit,true);//默认可编辑
        typedArray.recycle();//方便后期再次调用，相当于清空的功能


        //绑定layout布局
        mView = LayoutInflater.from(context).inflate(R.layout.text_bar,this,false);
        introText = mView.findViewById(R.id.tv_introText);
        contentText = mView.findViewById(R.id.tv_contentText);
        edit = mView.findViewById(R.id.right_arrow);

        //布局关联属性
        introText.setText(intro);
        contentText.setText(content);
        edit.setVisibility(enableEdit ? View.VISIBLE : View.INVISIBLE);

        //添加视图
        addView(mView);
    }

    public boolean isEnableEdit(){
        return enableEdit;
    }

    public void setIntroText(String intro){
        introText.setText(intro);
    }

    public void setContentText(String content){
        contentText.setText(content);
    }

    public String getIntroText(){
        return introText.getText().toString();
    }

    public String getContentText(){
        return contentText.getText().toString();
    }
}
