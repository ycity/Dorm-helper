package com.cypyc.dorm_helper.beans;

/**
 * Created by yuncity on 2017/12/17.
 */

public class RoomReturn {
    private String errcode;
    private String info5 = "0";
    private String info13 = "0";
    private String info14 = "0";
    private String info8 = "0";
    private String info9 = "0";

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getInfo5() {
        return info5;
    }

    public void setInfo5(String info5) {
        this.info5 = info5;
    }

    public String getInfo13() {
        return info13;
    }

    public void setInfo13(String info13) {
        this.info13 = info13;
    }

    public String getInfo14() {
        return info14;
    }

    public void setInfo14(String info14) {
        this.info14 = info14;
    }

    public String getInfo8() {
        return info8;
    }

    public void setInfo8(String info8) {
        this.info8 = info8;
    }

    public String getInfo9() {
        return info9;
    }

    public void setInfo9(String info9) {
        this.info9 = info9;
    }

    public String getRest(String building) {
        if ("5号楼".equals(building)) {
            return getInfo5();
        } else if ("8号楼".equals(building)) {
            return getInfo8();
        } else if ("9号楼".equals(building)) {
            return getInfo9();
        } else if ("13号楼".equals(building)) {
            return getInfo13();
        } else {
            return getInfo14();
        }
    }

}
