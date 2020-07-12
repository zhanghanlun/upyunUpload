package com.zhanghanlun.upyun.Entity;

public class Result {

    private boolean succeed;
    private int code;
    private String msg;

    public Result() {
    }

    public boolean isSucceed() {
        return this.succeed;
    }

    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return "Result{succeed=" + this.succeed + ", code=" + this.code + ", msg='" + this.msg + '\'' + '}';
    }
}
