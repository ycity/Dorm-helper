package com.cypyc.dorm_helper.beans;

/**
 * Created by yuncity on 2017/12/10.
 */

public class LoginReturn {
    private String errcode;
    private String errmsg;

    public LoginReturn() {
        errcode = "";
        errmsg = "";
    }

    public LoginReturn(String errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
