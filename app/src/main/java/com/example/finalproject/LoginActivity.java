package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_password;
    private Button bt_login;
    private CheckBox cb_remember;

    // 用来存储记住的用户名和密码
    private SharedPreferences sp;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        cb_remember = findViewById(R.id.cb_remember);

        bt_login = findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);

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
            configLoginInfo(cb_remember.isChecked());
            login();
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
                System.out.println("登录成功");
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
                        Toast.makeText(LoginActivity.this,"登录成功！",Toast.LENGTH_SHORT).show();
                        // 进行页面跳转
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this,CRUDActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(LoginActivity.this,"用户名或者密码错误!",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}