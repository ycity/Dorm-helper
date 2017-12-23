package com.cypyc.dorm_helper.util;

import android.text.TextUtils;
import android.util.Log;

import com.cypyc.dorm_helper.beans.LoginReturn;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yuncity on 2017/12/10.
 */

public class JSONUtil {

    public static LoginReturn parseJSON(String response) {

        LoginReturn ret = new LoginReturn();

        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jo = new JSONObject(response);
                ret.setErrcode(jo.getString("errcode"));
                JSONObject joo = new JSONObject(jo.getString("data"));
                ret.setErrmsg(joo.getString("errmsg"));
                Log.d("TAG", ret.getErrmsg());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}
