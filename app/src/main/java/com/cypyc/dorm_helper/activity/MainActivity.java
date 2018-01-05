package com.cypyc.dorm_helper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cypyc.dorm_helper.R;
import com.cypyc.dorm_helper.beans.Student;
import com.cypyc.dorm_helper.util.JSONUtil;
import com.cypyc.dorm_helper.util.SSLTrustAllManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

public class MainActivity extends AppCompatActivity {

    private final int UPDATE_INFO = 1;

    private String stuid;
    private Student stu;

    private Button selectDorm, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Intent intent = getIntent();
        stuid = intent.getStringExtra("stuid");
        setListener();
        getStudentInfo();
        // updateStudentInfo();
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
                case UPDATE_INFO:
                    updateStudentInfo();
                    if (stu.getRoom()==null || stu.getRoom().length()<3) {
                        selectDorm.setEnabled(true);
                    } else {
                        selectDorm.setEnabled(false);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    /* 创建一个Handler实例并重写其handleMessage函数 END */

    void setListener() {
        selectDorm = (Button) findViewById(R.id.select_dorm);
        selectDorm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.select_dorm) {
                    Intent intent = new Intent(MainActivity.this, DormActivity.class);
                    intent.putExtra("stuid", stu.getStuid());
                    intent.putExtra("name", stu.getName());
                    intent.putExtra("gender", stu.getGender());
                    intent.putExtra("vcode", stu.getVcode());
                    startActivity(intent);
                }
            }
        });
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.logout) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void getStudentInfo() {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getDetail" + "?stuid=" + stuid;
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
                        msg.what = UPDATE_INFO;
                        handler.sendMessage(msg);
                        handler.handleMessage(msg);
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

    private void updateStudentInfo() {
        TextView stuidView = (TextView) findViewById(R.id.info_stu_id);
        stuidView.setText(stu.getStuid());
        TextView nameView = (TextView) findViewById(R.id.info_name);
        Log.d("TAGG", "33");
        nameView.setText(stu.getName());
        TextView genderView = (TextView) findViewById(R.id.info_gender);
        genderView.setText(stu.getGender());
        TextView vcodeView = (TextView) findViewById(R.id.info_vcode);
        vcodeView.setText(stu.getVcode());
        TextView roomView = (TextView) findViewById(R.id.info_room);
        roomView.setText(stu.getRoom());
        TextView buildingView = (TextView) findViewById(R.id.info_building);
        buildingView.setText(stu.getBuilding());
        TextView locationView = (TextView) findViewById(R.id.info_location);
        locationView.setText(stu.getLocation());
        TextView gradeView = (TextView) findViewById(R.id.info_grade);
        gradeView.setText(stu.getGrade());
    }

}
