package com.meishi.infobean;

import android.util.Log;

public class MeInfo {
    private int code;
    private String msg;
    private int id;//
    private String phone;//
    private String username;//
    private int sex;//
    private String nickname;//
    private String headpicurl;//
    private String description;//
    private long birthdate;//

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public int getSex() {
        return sex;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHeadpicurl() {
        return headpicurl;
    }

    public String getDescription() {
        return description;
    }

    public long getBirthdate() {
        Log.d("MeInfo", "getBirthdate: "+birthdate);
        return birthdate;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
