package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.util.HideInputMethod;
import com.example.finalproject.util.HttpURLConn;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wunu
 */
@SuppressWarnings("all")
public class DeleteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_iddrop;
    private EditText et_isbndrop;
    private Button bt_delete;
    private Button bt_returndrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        et_iddrop = findViewById(R.id.et_iddrop);
        et_isbndrop = findViewById(R.id.et_isbndrop);

        bt_delete = findViewById(R.id.bt_delete);
        bt_delete.setOnClickListener(this);


        bt_returndrop = findViewById(R.id.bt_returndrop);
        bt_returndrop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_delete) {

            // 点击按钮后会自动隐藏软键盘
            HideInputMethod.hideAllInputMethod(this);

            final String iddel = et_iddrop.getText().toString().trim();
            final String isbndel = et_isbndrop.getText().toString().trim();
            if (TextUtils.isEmpty(iddel)) {
                showToast("学号不能为空");
            } else if (TextUtils.isEmpty(isbndel)) {
                showToast("书条码不能为空");
            } else {
                // 创建提示对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // 设置标题文本
                builder.setTitle("尊敬的管理员");
                // 设置对话框文本
                builder.setMessage("您确定要删除该条记录吗？");
                // 确认删除
                builder.setPositiveButton("残忍删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String url = HttpURLConn.BASE_URL + "/studentDelete";
                                Map<String, String> params = new HashMap<>();
                                params.put("iddel", iddel);
                                params.put("isbndel", isbndel);
                                String result = HttpURLConn.getContextByHttp(url, params);

                                System.out.println("==================" + result);

                                Message msg = new Message();
                                msg.what = 0x11;
                                Bundle data = new Bundle();
                                data.putString("result",result);
                                msg.setData(data);
                                handler.sendMessage(msg);
                            }
                        }).start();

                        // 清除文本框内容
                        et_iddrop.getText().clear();
                        et_isbndrop.getText().clear();
                    }
                });

                // 取消删除
                builder.setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("哈哈哈，你怂了！！");
                    }
                });

                // 根据建造器完成提醒对话框对象的构建
                AlertDialog alert = builder.create();
                // 在界面上显示提醒对话框
                alert.show();
            }

        } else if (v.getId() == R.id.bt_returndrop) {
            Intent intent = new Intent(); // 创建一个新意图
            setResult(Activity.RESULT_OK, intent); // 携带意图返回前一个页面
            finish(); // 关闭当前页面
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x11){
                Bundle data = msg.getData();
                String result = data.getString("result");

                // 用JSON解析器进行解析
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    int i = Integer.parseInt(code);
                    if (i == 1){
                        showToast("删除成功");
                    }else {
                        showToast("删除失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void showToast(String desc) {
        Toast.makeText(DeleteActivity.this, desc, Toast.LENGTH_SHORT).show();
    }
}