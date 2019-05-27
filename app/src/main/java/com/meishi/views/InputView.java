package com.meishi.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.meishi.R;


/**自定义控件
 * 以下是自定义的属性：
 * input_icon：输入框前的提示图标
 * input_hint：输入框内的提示文字
 * is_password：确定输入框的内容需不需要以密文的形式展示
 */
public class InputView extends FrameLayout {
    //声明与自定义属性相对应的变量\
    private int inputIcon;
    private String inputHint;
    private boolean isPassword;

    //控件
    private View mView;
    private ImageView iv_icon;
    private EditText ed_input;

    public InputView(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public InputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public InputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.inputView);
        inputIcon = typedArray.getResourceId(R.styleable.inputView_input_icon,R.mipmap.logo);
        inputHint = typedArray.getString(R.styleable.inputView_input_hint);
        isPassword = typedArray.getBoolean(R.styleable.inputView_is_password,false);
        typedArray.recycle();//方便后期再次调用，相当于清空的功能


        //绑定layout布局
        mView = LayoutInflater.from(context).inflate(R.layout.input_view,this,false);
        iv_icon = mView.findViewById(R.id.iv_icon);
        ed_input = mView.findViewById(R.id.ed_input);

        //布局关联属性
        iv_icon.setImageResource(inputIcon);
        ed_input.setHint(inputHint);
        ed_input.setInputType(isPassword ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_PHONE);

        //添加视图
        addView(mView);

    }


    //获取自定义View的输入内容
    public String getInputStr(){
        return ed_input.getText().toString().trim();
    }
}
