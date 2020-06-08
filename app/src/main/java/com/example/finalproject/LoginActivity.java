package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_password;
    private Button bt_login;
    private Button bt_qq;
    private Button bt_wechat;
    private Button bt_zhihu;
    private CheckBox cb_remember;

    // 用来存储记住的用户名和密码
    private SharedPreferences sp;

    // 注册跳转
    private TextView tv_register;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        cb_remember = findViewById(R.id.cb_remember);

        // 登录
        bt_login = findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);

        // 微信，QQ，知乎按钮控件
        bt_qq = findViewById(R.id.bt_qq);
        bt_qq.setOnClickListener(this);
        bt_wechat = findViewById(R.id.bt_wechat);
        bt_wechat.setOnClickListener(this);
        bt_zhihu = findViewById(R.id.bt_zhihu);
        bt_zhihu.setOnClickListener(this);

        // 注册控件
        tv_register = findViewById(R.id.tv_register);
        // 点击注册事件
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        // 共享参数对象
        sp = getSharedPreferences("login",MODE_PRIVATE);
        boolean checked = sp.getBoolean("checked", false);
        if (checked){
            // 记住密码
            String username = sp.getString("username","");
            String password = sp.getString("password","");
            et_username.setText(username);
            et_password.setText(password);
            cb_remember.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_login){
            String uesrname = et_username.getText().toString().trim();
            String password = et_password.getText().toString().trim();
            if (TextUtils.isEmpty(uesrname)){
                showToast("用户名不能为空");
            }else if (TextUtils.isEmpty(password)){
                showToast("密码不能为空");
            }else {
                configLoginInfo(cb_remember.isChecked());
                login();
            }
        }else if (v.getId() == R.id.bt_qq || v.getId() == R.id.bt_wechat || v.getId() == R.id.bt_zhihu){
            showToast("你想多了！！");
        }
    }

    /**
     * 是否保存密码
     * @param checked
     */
    public void configLoginInfo(boolean checked){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("checked",cb_remember.isChecked());
        if (checked){
            editor.putString("username",et_username.getText().toString().trim());
            editor.putString("password",et_password.getText().toString().trim());
        }else {
            editor.remove("username");
            editor.remove("password");
        }
        editor.apply();
    }

    /**
     * 读取登录信息
     */
    @SuppressWarnings("all")
    private void login(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> params = new HashMap<>();
                String uesrname = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String url = HttpURLConn.BASE_URL + "/login";
                params.put("username",uesrname);
                params.put("password",password);
                String result = HttpURLConn.getContextByHttp(url, params);

                Message msg = new Message();
                msg.what = 0x11;
                Bundle data = new Bundle();
                data.putString("result",result);
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 处理数据
     */
    @SuppressWarnings("all")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x11){
                Bundle data = msg.getData();
                String key = data.getString("result");
                // 输出用户名密码
                System.out.println("输出用户名密码" + key);

                // 利用安卓自带的JSON解析器进行解析
                try {
                    JSONObject json = new JSONObject(key);
                    int code = Integer.parseInt(json.getString("code"));
                    if (code == 1){
                        showToast("登录成功");
                        // 进行页面跳转
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this,CRUDActivity.class);
                        startActivity(intent);
                    }else {
                        showToast("用户名或者密码错误");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void showToast(String desc) {
        Toast.makeText(LoginActivity.this, desc, Toast.LENGTH_SHORT).show();
    }
}