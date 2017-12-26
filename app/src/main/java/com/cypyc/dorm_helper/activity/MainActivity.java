package com.cypyc.dorm_helper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cypyc.dorm_helper.R;
import com.cypyc.dorm_helper.beans.Student;
import com.cypyc.dorm_helper.util.JSONUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    private Student stu;
    private String stuid;
    private boolean ok = false;

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

    public class SSLTrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    }

    void setListener() {
        Button selectDorm = (Button) findViewById(R.id.select_dorm);
        selectDorm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.select_dorm) {
                    // Intent intent = new Intent(MainActivity.this, )
                }
            }
        });
    }

    private void getStudentInfo() {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getDetail" + "?stuid=" + stuid;
        Log.d("TAG", address);
        ok = false;
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
                    Log.d("TAGG", "22");
                    stu = JSONUtil.parseStudentJSON(content);
                    // Log.d("TAG", loginReturn.getErrcode());
                    ok = true;
                    updateStudentInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
        // while (!ok);
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
