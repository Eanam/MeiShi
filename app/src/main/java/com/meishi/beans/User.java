package com.meishi.beans;

import java.sql.Date;

/**
 * 用户类
 * id：用户id
 * username：用户名(用户登录)
 * nickname：用户使用名
 * phone：用户手机号
 * token：用户通证
 * desc：用户描述
 * birthday：生日
 * headPicUrl：头像链接
 * sex：性别 0是男，1是女
 */
public class User {
    private String username;
//    private String password;
    private String phone;
    private String token = null;
    private String desc;
    private Date birthday;
    private String headPicUrl;
    private int sex;

    public User(){}

    public User(String username, String phone, String desc, Date birthday, String headPicUrl, int sex) {
        this.username = username;
        this.phone = phone;
        this.desc = desc;
        this.birthday = birthday;
        this.headPicUrl = headPicUrl;
        this.sex = sex;
    }
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getHeadPicUrl() {
        return headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.headPicUrl = headPicUrl;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
