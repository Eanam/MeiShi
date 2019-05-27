package com.meishi.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.RegexUtils;

/**
 * 判断用户输入是否符合要求
 */
public class UserUtils {
    /**
     *验证登录用户输入是否符合要求
     */
    public static boolean validateLogin(Context context,String phone,String password){
        //精准匹配手机号码
        if(!RegexUtils.isMobileExact(phone)){
            Toast.makeText(context,"无效手机号",Toast.LENGTH_SHORT).show();
            return false;
        }

        //判断输入密码是否为空
        if(TextUtils.isEmpty(password)){
            Toast.makeText(context,"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * 验证注册用户输入是否符合要求
     */
    public static boolean validateRegist(Context context,String phone,String password,String _password){

        if(!RegexUtils.isMobileExact(phone)){
            Toast.makeText(context,"请输入正确手机号",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!TextUtils.isEmpty(password)){
            if(TextUtils.isEmpty(_password)){
                Toast.makeText(context,"请再次输入密码",Toast.LENGTH_SHORT).show();
                return false;
            }

            if(!password.equals(_password)){
                Toast.makeText(context,"两次密码不一致",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(context,"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }
}
