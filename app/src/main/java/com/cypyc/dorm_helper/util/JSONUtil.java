package com.cypyc.dorm_helper.util;

import android.text.TextUtils;
import android.util.Log;

import com.cypyc.dorm_helper.beans.LoginReturn;
import com.cypyc.dorm_helper.beans.RoomReturn;
import com.cypyc.dorm_helper.beans.Student;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yuncity on 2017/12/10.
 */

public class JSONUtil {

    public static LoginReturn parseLoginJSON(String response) {

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

    public static Student parseStudentJSON(String response) {

        Student ret = new Student();

        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jo = new JSONObject(response);
                ret.setErrcode(jo.getString("errcode"));
                JSONObject joo = new JSONObject(jo.getString("data"));
                ret.setStuid(joo.getString("studentid"));
                ret.setName(joo.getString("name"));
                ret.setGender(joo.getString("gender"));
                ret.setVcode(joo.getString("vcode"));
                ret.setRoom(joo.getString("room"));
                ret.setBuilding(joo.getString("building"));
                ret.setLocation(joo.getString("location"));
                ret.setGrade(joo.getString("grade"));
                Log.d("TAG", ret.getStuid());
                Log.d("TAG", ret.getName());
                Log.d("TAG", ret.getGender());
                Log.d("TAG", ret.getVcode());
                Log.d("TAG", ret.getRoom());
                Log.d("TAG", ret.getBuilding());
                Log.d("TAG", ret.getLocation());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static RoomReturn parseRoomJSON(String response) {

        RoomReturn ret = new RoomReturn();

        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jo = new JSONObject(response);
                ret.setErrcode(jo.getString("errcode"));
                JSONObject joo = new JSONObject(jo.getString("data"));
                ret.setInfo5(joo.getString("5"));
                ret.setInfo8(joo.getString("8"));
                ret.setInfo9(joo.getString("9"));
                ret.setInfo13(joo.getString("13"));
                ret.setInfo14(joo.getString("14"));
                Log.d("TAG", ret.getInfo5());
                Log.d("TAG", ret.getInfo8());
                Log.d("TAG", ret.getInfo9());
                Log.d("TAG", ret.getInfo13());
                Log.d("TAG", ret.getInfo14());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

}
