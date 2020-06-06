package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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
public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_idupdate;       // 学号
    private EditText et_oisbnupdate;    // 旧书条码
    private EditText et_nisbnupdate;    // 新书条码

    private Button bt_update;
    private Button bt_returnupdate;

    private String result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        et_idupdate = findViewById(R.id.et_idcreate);
        et_oisbnupdate = findViewById(R.id.et_oisbnupdate);
        et_nisbnupdate = findViewById(R.id.et_nisbnupdate);

        bt_update.findViewById(R.id.bt_update).setOnClickListener(this);
        bt_returnupdate.findViewById(R.id.bt_returnupdate).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_update) {
            final String id = et_idupdate.getText().toString().trim();
            final String oisbn = et_oisbnupdate.getText().toString().trim();
            final String nisbn = et_nisbnupdate.getText().toString().trim();

            if (TextUtils.isEmpty(id)) {
                showToast("学号不能为空");
            } else if (TextUtils.isEmpty(oisbn)) {
                showToast("旧书条码不能为空");
            } else if (TextUtils.isEmpty(nisbn)) {
                showToast("新条码不能为空");
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = HttpURLConn.BASE_URL + "/studentUpdate";
                        Map<String, String> params = new HashMap<>();
                        params.put("id", id);
                        params.put("oisbn", oisbn);
                        params.put("nisbn", nisbn);
                        result = HttpURLConn.getContextByHttp(url, params);
                        System.out.println(result);

                        JSONObject jsonObject = null;
                        int code = 0;
                        try {
                            jsonObject = new JSONObject(result);
                            code = Integer.parseInt(jsonObject.getString("code"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (code == 1){
                            Toast.makeText(UpdateActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(UpdateActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                        }

                    }
                }).start();

                // 清除文本框
                et_idupdate.getText().clear();
                et_oisbnupdate.getText().clear();
                et_nisbnupdate.getText().clear();

            }
        } else if (v.getId() == R.id.bt_returnupdate) {
            Intent intent = new Intent(); // 创建一个新意图
            setResult(Activity.RESULT_OK, intent); // 携带意图返回前一个页面
            finish(); // 关闭当前页面
        }
    }

    private void showToast(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }
}