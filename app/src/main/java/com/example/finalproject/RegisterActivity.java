package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.util.HttpURLConn;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wunu
 */
@SuppressWarnings("all")
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_register;   // 管理员
    private EditText et_regpwd;     // 密码
    private EditText et_key;        // 密钥

    private Button bt_register;
    private Button bt_returnlog;

    private String userName;
    private String password;
    private String key;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_main);

        et_register = findViewById(R.id.et_register);
        et_regpwd = findViewById(R.id.et_regpwd);
        et_key = findViewById(R.id.et_key);

        bt_register = findViewById(R.id.bt_register);
        bt_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_register) {
            userName = et_register.getText().toString().trim();
            password = et_regpwd.getText().toString().trim();
            key = et_key.getText().toString().trim();
            if (TextUtils.isEmpty(userName)) {
                showToast("用户名不能为空");
            } else if (TextUtils.isEmpty(password)) {
                showToast("密码不能为空");
            } else if (TextUtils.isEmpty(key)) {
                showToast("密钥不能为空");
            } else {
                inviteKey();

            }

        }
    }

    // 接收密钥
    private void inviteKey() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>();
                key = et_key.getText().toString().trim();
                String url = HttpURLConn.BASE_URL + "/invite";
                params.put("key", key);
                String result = HttpURLConn.getContextByHttp(url, params);

                System.out.println("密钥返回值++++++++++++++++++++++++" + result);

                Message msg = new Message();
                msg.what = 0x11;
                Bundle date = new Bundle();
                date.putString("result", result);
                msg.setData(date);
                handler.sendMessage(msg);
            }
        }).start();

    }

    // 进行注册
    private void register() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<>();
                String url = HttpURLConn.BASE_URL + "/register";
                userName = et_register.getText().toString().trim();
                password = et_regpwd.getText().toString().trim();
                System.out.println("=========================================");
                System.out.println(userName);
                System.out.println(password);
                System.out.println("=========================================");
                params.put("userName", userName);
                params.put("password", password);
                String result = HttpURLConn.getContextByHttp(url, params);

                System.out.println("注册返回值==============================" + result);

                Message msg = new Message();
                msg.what = 0x12;
                Bundle date = new Bundle();
                date.putString("result", result);
                msg.setData(date);
                handler.sendMessage(msg);
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x11) {
                Bundle data = msg.getData();
                String result = data.getString("result");
                // 利用JSON进行解析
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    int i = Integer.parseInt(code);
                    if (i == 1) {
                        // 邀请码正确的时候调用 register()方法进行注册
                        register();
                    } else {
                        // 清除文本框内容
                        et_key.getText().clear();
                        showToast("邀请码错误，注册失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 0x12) {
                Bundle data = msg.getData();
                String result = data.getString("result");

                int i = Integer.parseInt(result);
                if (i == 1) {

                    // 对数据进行存储
                    sp = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("checked", true);
                    editor.putString("username", et_register.getText().toString().trim());
                    editor.putString("password", et_regpwd.getText().toString().trim());
                    editor.apply();
                    showToast("注册成功");
                    // 进行页面跳转
                    Intent intent = new Intent();
                    intent.setClass(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    // 清除文本框内容
                    et_key.getText().clear();
                    et_register.getText().clear();
                    et_regpwd.getText().clear();
                    showToast("服务器异常，注册失败");
                }
            }
        }
    };

    private void showToast(String desc) {
        Toast.makeText(RegisterActivity.this, desc, Toast.LENGTH_SHORT).show();
    }

}