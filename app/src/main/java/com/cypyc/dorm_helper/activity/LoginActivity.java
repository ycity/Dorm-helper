package com.cypyc.dorm_helper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cypyc.dorm_helper.R;
import com.cypyc.dorm_helper.beans.LoginReturn;
import com.cypyc.dorm_helper.beans.Student;
import com.cypyc.dorm_helper.util.JSONUtil;
import com.cypyc.dorm_helper.util.NetUtil;
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

/**
 * Created by yuncity on 2017/11/13.
 */

public class LoginActivity extends AppCompatActivity {

    final int PWD_ERR = 0;
    final int LOGIN = 1;
    final int CHECK_STUID = 2;

    private Button loginBtn;
    private EditText stuidText, pwdText;
    private String stuid, pwd;

    private Student stu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initView();
        setListener();
        checkNetStat();
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
                case CHECK_STUID:
                    if (stuid.equals(stu.getStuid())) {
                        pwdCheck();
                    } else {
                        Toast.makeText(LoginActivity.this, "用户名不存在！", Toast.LENGTH_LONG).show();
                    }
                    break;
                case LOGIN:
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("stuid", stuid);
                    startActivity(intent);
                    break;
                case PWD_ERR:
                    Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };
    /* 创建一个Handler实例并重写其handleMessage函数 END */

    void initView() {
        stuidText = (EditText) findViewById(R.id.stu_id);
        pwdText = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login_btn);
    }

    void setListener() {
        stuidText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                stuid = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pwdText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.indexOf("\r")>=0 || str.indexOf("\n")>=0) {
                    stuid = stuidText.getText().toString();
                    pwd = pwdText.getText().toString().replace("\n","").replace("\r","");
                    stuidCheck();
                }
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.login_btn) {
                    stuid = stuidText.getText().toString();
                    pwd = pwdText.getText().toString().replace("\n","").replace("\r","");
                    stuidCheck();
                }
            }
        });
    }

    void stuidCheck() {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getDetail" + "?stuid=" + stuid;
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
                        msg.what = CHECK_STUID;
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

    void pwdCheck() {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/Login" + "?username=" + stuid + "&password=" + pwd;
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

                    loginReturn = JSONUtil.parseLoginJSON(content);
                    if (loginReturn != null) {
                        Message msg = new Message();
                        if ("0".equals(loginReturn.getErrcode())) {
                            msg.what = LOGIN;
                        } else {
                            msg.what = PWD_ERR;
                        }
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
