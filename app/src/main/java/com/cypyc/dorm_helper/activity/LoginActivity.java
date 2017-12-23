package com.cypyc.dorm_helper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cypyc.dorm_helper.R;
import com.cypyc.dorm_helper.beans.LoginReturn;
import com.cypyc.dorm_helper.util.JSONUtil;
import com.cypyc.dorm_helper.util.NetUtil;

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

/**
 * Created by yuncity on 2017/11/13.
 */

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    EditText stuidText, pwdText;
    String stuid, pwd;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        setListener();
        checkNetStat();
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
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("errcode", "Click");
                if (v.getId() == R.id.login_btn) {

                    stuidText = (EditText) findViewById(R.id.stu_id);
                    pwdText = (EditText) findViewById(R.id.password);
                    stuid = stuidText.getText().toString();
                    pwd = pwdText.getText().toString();
                    if (loginCheck()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    } else {
                        Toast.makeText(LoginActivity.this, "账号或密码输入错误！", Toast.LENGTH_LONG).show();
                    }
                    //
                }
            }
        });
    }

    boolean loginCheck() {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/Login" + "?username=" + stuid + "&password=" + pwd;
        flag = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection con = null;
                LoginReturn loginReturn = new LoginReturn();
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

                    loginReturn = JSONUtil.parseJSON(content);
                    Log.d("TAG", loginReturn.getErrcode());
                    if ("0".equals(loginReturn.getErrcode())) {
                        Log.d("TAG", "true");
                        flag = true;
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

        if (flag)  {
            return true;
        }
        return false;
    }

    /**
     * methodName: checkNetStat
     * description: 检查当前网络状态，并弹出提示
     * parameters: void
     * return: void
     */
    public void checkNetStat() {
        /* 检查网络状态 start */
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Toast.makeText(LoginActivity.this, "网络OK！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(LoginActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }
        /* 检查网络状态 end */
    }


}
