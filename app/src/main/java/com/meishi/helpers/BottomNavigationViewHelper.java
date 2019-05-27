package com.meishi.helpers;


import android.annotation.SuppressLint;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;

import java.lang.reflect.Field;

/**
 * 底部导航栏的助手类
 */
public class BottomNavigationViewHelper {

    //取消底部导航栏的移位效果
    public static void disableShiftMode(BottomNavigationView view){
        BottomNavigationMenuView menuView = (BottomNavigationMenuView)view.getChildAt(0);

        try{
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView,false);
            shiftingMode.setAccessible(false);
            for (int i=0;i<menuView.getChildCount();i++){
                BottomNavigationItemView item = (BottomNavigationItemView)menuView.getChildAt(i);

                item.setShifting(false);

                item.setChecked(item.getItemData().isChecked());
            }

        }catch(NoSuchFieldException e){

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
