package com.cypyc.dorm_helper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cypyc.dorm_helper.R;
import com.cypyc.dorm_helper.beans.RoomReturn;
import com.cypyc.dorm_helper.beans.Student;
import com.cypyc.dorm_helper.util.JSONUtil;
import com.cypyc.dorm_helper.util.SSLTrustAllManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

/**
 * Created by yuncity on 2017/12/16.
 */

public class DormActivity extends AppCompatActivity {

    final int UPDATE_REST = 0;
    final int CHECK_STU1 = 1;
    final int CHECK_STU2 = 2;
    final int CHECK_STU3 = 3;

    private String stuid = "";
    private String stuName = "";
    private String stuGender = "";
    private String stuVcode = "";
    private String stuid1 = "";
    private String stuid2 = "";
    private String stuid3 = "";
    private String vcode1 = "";
    private String vcode2 = "";
    private String vcode3 = "";

    private Spinner spinner;
    private String choseBuilding;
    private List<String> buildings;
    private ArrayAdapter<String> arr_adapter;
    private RoomReturn roomRet = new RoomReturn();
    private Student stu = new Student();

    private TextView stuidText, stuNameText, stuGenderText, stuVcodeText;
    private EditText stuidEditText1, stuidEditText2, stuidEditText3, vcodeEditText1, vcodeEditText2, vcodeEditText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dorm_activity);

        initView();

        setBuildingSpinner();
        setListener();

        getRoomInfo();

    }

    /* 创建一个Handler实例并重写其handleMessage函数 START */
    private Handler handler = new Handler() {
        /**
         * methodName: handleMessage
         * description: 通过message中保存的数值来进行处理
         * parameters: @msg 一个Message
         * return: void
         */
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_REST:
                    updateRoomInfo();
                    break;
                case CHECK_STU1:
                    if (!"".equals(stuid1)) {
                        if ("40001".equals(stu.getErrcode())) {
                            Toast.makeText(DormActivity.this, "第1名室友信息填写错误！该学号不存在！", Toast.LENGTH_LONG).show();
                            break;
                        }
                        if (stuid.equals(stuid1)) {
                            Toast.makeText(DormActivity.this, "第1名室友信息填写错误！不能选自己当室友！", Toast.LENGTH_LONG).show();
                            break;
                        }
                        if (!vcode1.equals(stu.getVcode())) {
                            Toast.makeText(DormActivity.this, "第1名室友信息填写错误！校验码不正确！", Toast.LENGTH_LONG).show();
                            break;
                        }
                        if (!(stu.getRoom()==null || "".equals(stu.getRoom()))) {
                            Toast.makeText(DormActivity.this, "第1名室友已经选过宿舍！", Toast.LENGTH_LONG).show();
                            break;
                        }
                        if (!stuGender.equals(stu.getGender())) {
                            Toast.makeText(DormActivity.this, "第1名室友信息填写错误！室友不能为异性！", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                    getStu2();
                    break;
                case CHECK_STU2:
                    if (!"".equals(stuid2)) {
                        if ("40001".equals(stu.getErrcode())) {
                            Toast.makeText(DormActivity.this, "第2名室友信息填写错误！该学号不存在！", Toast.LENGTH_LONG).show();
                            break;
                        }
                        if (stuid.equals(stuid2)) {
                            Toast.makeText(DormActivity.this, "第2名室友信息填写错误！不能选自己当室友！", Toast.LENGTH_LONG).show();
                            break;
                        }
                        if (!vcode2.equals(stu.getVcode())) {
                            Toast.makeText(DormActivity.this, "第2名室友信息填写错误！校验码不正确！", Toast.LENGTH_LONG).show();
                            break;
                        }
                        if (!(stu.getRoom()==null || "".equals(stu.getRoom()))) {
                            Toast.makeText(DormActivity.this, "第2名室友已经选过宿舍！", Toast.LENGTH_LONG).show();
                            break;
                        }
                        if (!stuGender.equals(stu.getGender())) {
                            Toast.makeText(DormActivity.this, "第2名室友信息填写错误！室友不能为异性！", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                    getStu3();
                    break;
                case CHECK_STU3:
                    if (!"".equals(stuid3)) {
                        if ("40001".equals(stu.getErrcode())) {
                            Toast.makeText(DormActivity.this, "第3名室友信息填写错误！该学号不存在！", Toast.LENGTH_LONG).show();
                            break;
                        }
                        if (stuid.equals(stuid3)) {
                            Toast.makeText(DormActivity.this, "第3名室友信息填写错误！不能选自己当室友！", Toast.LENGTH_LONG).show();
                            break;
                        }
                        if (!vcode3.equals(stu.getVcode())) {
                            Toast.makeText(DormActivity.this, "第3名室友信息填写错误！校验码不正确！", Toast.LENGTH_LONG).show();
                            break;
                        }
                        if (!(stu.getRoom()==null || "".equals(stu.getRoom()))) {
                            Toast.makeText(DormActivity.this, "第3名室友已经选过宿舍！", Toast.LENGTH_LONG).show();
                            break;
                        }
                        if (!stuGender.equals(stu.getGender())) {
                            Toast.makeText(DormActivity.this, "第3名室友信息填写错误！室友不能为异性！", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                    Intent intent = new Intent(DormActivity.this, SuccessActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
    /* 创建一个Handler实例并重写其handleMessage函数 END */

    private void initView() {
        stuidText = (TextView) findViewById(R.id.dorm_stu_id);
        stuNameText = (TextView) findViewById(R.id.dorm__name);
        stuGenderText = (TextView) findViewById(R.id.dorm_gender);
        stuVcodeText = (TextView) findViewById(R.id.dorm_vcode);
        stuidEditText1 = (EditText) findViewById(R.id.stuid1);
        stuidEditText2 = (EditText) findViewById(R.id.stuid2);
        stuidEditText3 = (EditText) findViewById(R.id.stuid3);
        vcodeEditText1 = (EditText) findViewById(R.id.vcode1);
        vcodeEditText2 = (EditText) findViewById(R.id.vcode2);
        vcodeEditText3 = (EditText) findViewById(R.id.vcode3);

        getIntentExtras();

        stuidText.setText(stuid);
        stuNameText.setText(stuName);
        stuGenderText.setText(stuGender);
        stuVcodeText.setText(stuVcode);
    }

    private void setBuildingSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner);
        //数据
        buildings = new ArrayList<String>();
        buildings.add("5号楼");
        buildings.add("8号楼");
        buildings.add("9号楼");
        buildings.add("13号楼");
        buildings.add("14号楼");
        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, buildings);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
    }

    private void setListener() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choseBuilding = buildings.get(position);
                // Toast.makeText(DormActivity.this, choseBuilding, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        Button update = (Button) findViewById(R.id.dorm_update_btn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.dorm_update_btn) {
                    updateRoomInfo();
                }
            }
        });
        Button selectDorm = (Button) findViewById(R.id.select_dorm);
        selectDorm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.select_dorm) {
                    int stuNum = 1;
                    int bedNum = Integer.parseInt(roomRet.getRest(choseBuilding));

                    stuid1 = stuidEditText1.getText().toString();
                    stuid2 = stuidEditText2.getText().toString();
                    stuid3 = stuidEditText3.getText().toString();
                    vcode1 = vcodeEditText1.getText().toString();
                    vcode2 = vcodeEditText2.getText().toString();
                    vcode3 = vcodeEditText3.getText().toString();

                    if (!("".equals(stuid1))) {
                        ++stuNum;
                    }
                    if (!("".equals(stuid2))) {
                        ++stuNum;
                    }
                    if (!("".equals(stuid3))) {
                        ++stuNum;
                    }
                    getRoomInfo();
                    if (stuNum > bedNum) {
                        Toast.makeText(DormActivity.this, choseBuilding+"已没有空位！", Toast.LENGTH_LONG).show();
                        return ;
                    }
                    getStu1();
                }
            }
        });
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        stuid = intent.getStringExtra("stuid");
        stuName = intent.getStringExtra("name");
        stuGender = intent.getStringExtra("gender");
        stuVcode = intent.getStringExtra("vcode");
    }

    private void getRoomInfo() {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getRoom" + "?gender=" + stuGender;
        Log.d("TAG", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection con = null;
                try {
                    /* 设置连接参数 START */
                    URL url = new URL(address);
                    SSLContext context  = SSLContext.getInstance("TLS");
                    context.init(null, new TrustManager[] { new SSLTrustAllManager() }, null);
                    HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
                    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String arg0, SSLSession arg1) {
                            return true;
                        }
                    });
                    // Log.d("TAG", "11");
                    con = (HttpsURLConnection) url.openConnection();
                    con.setDoInput(true);
                    con.setDoOutput(false);
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    /* 设置连接参数 END */

                    /* 创建输入流，并逐行读取站点中的信息，最终保存在content字符串中 START */
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder buffer = new StringBuilder();
                    String line, content;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    content = buffer.toString();
                    /* 创建输入流，并逐行读取站点中的信息，最终保存在content字符串中 END */

                    roomRet = JSONUtil.parseRoomJSON(content);
                    if (roomRet != null) {
                        Message msg = new Message();
                        msg.what = UPDATE_REST;
                        handler.sendMessage(msg);
//                      handler.handleMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

    private void updateRoomInfo() {
        TextView info5 = (TextView) findViewById(R.id.info_b5);
        info5.setText(roomRet.getInfo5());
        TextView info8 = (TextView) findViewById(R.id.info_b8);
        Log.d("TAGG", "33");
        info8.setText(roomRet.getInfo8());
        TextView info9 = (TextView) findViewById(R.id.info_b9);
        info9.setText(roomRet.getInfo9());
        TextView info13 = (TextView) findViewById(R.id.info_b13);
        info13.setText(roomRet.getInfo13());
        TextView info14 = (TextView) findViewById(R.id.info_b14);
        info14.setText(roomRet.getInfo14());
    }

    private void getStu1() {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getDetail" + "?stuid=" + stuid1;
        if (!"".equals(stuid1)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpsURLConnection con = null;
                    try {
                        /* 设置连接参数 START */
                        URL url = new URL(address);
                        SSLContext context = SSLContext.getInstance("TLS");
                        context.init(null, new TrustManager[]{new SSLTrustAllManager()}, null);
                        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
                        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String arg0, SSLSession arg1) {
                                return true;
                            }
                        });
                        // Log.d("TAG", "11");
                        con = (HttpsURLConnection) url.openConnection();
                        con.setDoInput(true);
                        con.setDoOutput(false);
                        con.setRequestMethod("GET");
                        con.setConnectTimeout(8000);
                        con.setReadTimeout(8000);
                        /* 设置连接参数 END */
                        Log.d("TAGG", "11");
                        /* 创建输入流，并逐行读取站点中的信息，最终保存在content字符串中 START */
                        InputStream in = con.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder buffer = new StringBuilder();
                        String line, content;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line);
                        }
                        content = buffer.toString();
                        /* 创建输入流，并逐行读取站点中的信息，最终保存在content字符串中 END */

                        stu = JSONUtil.parseStudentJSON(content);
                        if (stu != null) {
                            Message msg = new Message();
                            msg.what = CHECK_STU1;
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (con != null) {
                            con.disconnect();
                        }
                    }
                }
            }).start();
        } else {
            Message msg = new Message();
            msg.what = CHECK_STU1;
            handler.sendMessage(msg);
        }
    }

    private void getStu2() {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getDetail" + "?stuid=" + stuid2;
        if (!"".equals(stuid2)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpsURLConnection con = null;
                    try {
                        /* 设置连接参数 START */
                        URL url = new URL(address);
                        SSLContext context = SSLContext.getInstance("TLS");
                        context.init(null, new TrustManager[]{new SSLTrustAllManager()}, null);
                        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
                        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String arg0, SSLSession arg1) {
                                return true;
                            }
                        });
                        // Log.d("TAG", "11");
                        con = (HttpsURLConnection) url.openConnection();
                        con.setDoInput(true);
                        con.setDoOutput(false);
                        con.setRequestMethod("GET");
                        con.setConnectTimeout(8000);
                        con.setReadTimeout(8000);
                        /* 设置连接参数 END */
                        Log.d("TAGG", "11");
                        /* 创建输入流，并逐行读取站点中的信息，最终保存在content字符串中 START */
                        InputStream in = con.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder buffer = new StringBuilder();
                        String line, content;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line);
                        }
                        content = buffer.toString();
                        /* 创建输入流，并逐行读取站点中的信息，最终保存在content字符串中 END */

                        stu = JSONUtil.parseStudentJSON(content);
                        if (stu != null) {
                            Message msg = new Message();
                            msg.what = CHECK_STU2;
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (con != null) {
                            con.disconnect();
                        }
                    }
                }
            }).start();
        } else {
            Message msg = new Message();
            msg.what = CHECK_STU2;
            handler.sendMessage(msg);
        }
    }

    private void getStu3() {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getDetail" + "?stuid=" + stuid3;
        if (!"".equals(stuid3)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpsURLConnection con = null;
                    try {
                        /* 设置连接参数 START */
                        URL url = new URL(address);
                        SSLContext context = SSLContext.getInstance("TLS");
                        context.init(null, new TrustManager[]{new SSLTrustAllManager()}, null);
                        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
                        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String arg0, SSLSession arg1) {
                                return true;
                            }
                        });
                        con = (HttpsURLConnection) url.openConnection();
                        con.setDoInput(true);
                        con.setDoOutput(false);
                        con.setRequestMethod("GET");
                        con.setConnectTimeout(8000);
                        con.setReadTimeout(8000);
                        /* 设置连接参数 END */

                        /* 创建输入流，并逐行读取站点中的信息，最终保存在content字符串中 START */
                        InputStream in = con.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder buffer = new StringBuilder();
                        String line, content;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line);
                        }
                        content = buffer.toString();
                        /* 创建输入流，并逐行读取站点中的信息，最终保存在content字符串中 END */

                        stu = JSONUtil.parseStudentJSON(content);
                        if (stu != null) {
                            Message msg = new Message();
                            msg.what = CHECK_STU3;
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (con != null) {
                            con.disconnect();
                        }
                    }
                }
            }).start();
        } else {
            Message msg = new Message();
            msg.what = CHECK_STU3;
            handler.sendMessage(msg);
        }
    }

}
