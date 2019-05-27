package com.meishi;

public class AppConstants {
    public static final String BASEURL = "http://47.103.44.83/meishi/";
    public static final int READ_TIME = 5 * 1000;
    public static final int WRITE_TIME = 5 * 1000;
    public static final int CONNECT_TIME = 5 * 1000;
    public static final int MAXAGE = 1 * 60;//自定义响应的可读时间为1分钟
    public static final int MAXSTALE =60 * 60 * 24;//cache保存时间
    public static final int CACHE_CAPACITY = 1024*1024*100;//缓存空间设置为100M
    public static final String PROVIDER = "com.meishi.fileprovider";


    //登陆注册常量
    public static final int SUCCESS_LOGIN = 0;
    public static final int WRONG_PASSWORD = 2;
    public static final int UNREGIST = 4;
    public static final int SUCCESS_REGIST = 0;
    public static final int FAIL_REGIST = 1;
    public static final int SUBMIT_SUCCESS = 0;
    public static final int SUBMIT_FAILURE = 1;

    //标志符
    public static final int FLAG_LOGIN = 0;//让LoadingActivity执行登录
    public static final int FLAG_REGIST = 1;//让LoadingActivity执行注册


    //Intent或者Bundle传递过程中的一些key常量
    public static final String SHOP_TO_GET_GOOD = "shop";
    public static final String GOOD_TO_GET_COMMENT_1 = "good";
    public static final String GOOD_TO_GET_COMMENT_2 = "shop_id";
    public static final String TOKEN = "access_token";
    public static final String SHOP_ID = "shop_id";
    public static final String GOOD_ID = "good_id";
    public static final String COLLECT_FLAG = "collect_flag";


    //修改类型
    public static final int EDIT_HEADPIC = 0;
    public static final int EDIT_NICKNAME = 1;
    public static final int EDIT_SEX = 2;
    public static final int EDIT_BIRTHDATE = 3;
    public static final int EDIT_DESC = 4;
    public static final String EDIT_CONTENT = "content";


}
