package com.meishi.infobean;

public class LoginAndRegistInfo {
    private int code;
    private int count;
    private String msg;
    private Data data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public class Data{
        private String access_token;

        public String getAccess_token() {
            return access_token;
        }

    }

    public int getCount() {
        return count;
    }

    public String getMsg() {
        return msg;
    }
}
