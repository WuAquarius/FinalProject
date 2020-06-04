package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.util.HttpURLConn;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wunu
 */
@SuppressWarnings("all")
public class CreateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_idcreate;                   // 学号
    private EditText et_namecreat;                  // 姓名
    private AutoCompleteTextView et_majorcreate;    // 专业
    private EditText et_isbncreate;                 // 书条码

    private Button bt_create;       // 添加
    private Button bt_returncreate; // 退出

    // 定义自动完成的提示文本数组
    private String[] majorArray = {"软件工程", "计算机科学与技术", "网络工程", "物联网"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        et_idcreate = findViewById(R.id.et_idcreate);
        et_namecreat = findViewById(R.id.et_namecreat);
        et_majorcreate = findViewById(R.id.et_majorcreate);
        et_isbncreate = findViewById(R.id.et_isbncreate);

        bt_create.findViewById(R.id.bt_create).setOnClickListener(this);
        bt_returncreate.findViewById(R.id.bt_returncreate).setOnClickListener(this);

        // 声明一个自动完成时下拉展示的数组适配器
        ArrayAdapter<String> majorAdapter = new ArrayAdapter<String>(this, R.layout.item_dropdown, majorArray);
        // 设置自动完成编辑框的数组适配器
        et_majorcreate.setAdapter(majorAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_create) {
            final String id = et_idcreate.getText().toString().trim();
            final String name = et_namecreat.getText().toString().trim();
            final String major = et_majorcreate.getText().toString().trim();
            final String isbn = et_isbncreate.getText().toString().trim();
            if (TextUtils.isEmpty(id)) {
                showToast("学号不能为空");
            } else if (TextUtils.isEmpty(name)) {
                showToast("名字不能为空");
            } else if (TextUtils.isEmpty(major)) {
                showToast("专业不能为空");
            } else if (TextUtils.isEmpty(isbn)) {
                showToast("书条码不能为空");
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = HttpURLConn.BASE_URL + "/studentInsertServlet";
                        Map<String, String> params = new HashMap<>();
                        params.put("id", id);
                        params.put("name", name);
                        params.put("major", major);
                        params.put("isbn", isbn);
                        String resule = HttpURLConn.getContextByHttp(url, params);
                        System.out.println(resule);
                    }
                }).start();

                showToast("添加成功");
                // 清除文本框
                et_idcreate.getText().clear();
                et_namecreat.getText().clear();
                et_majorcreate.getText().clear();
                et_isbncreate.getText().clear();
            }
        } else if (v.getId() == R.id.bt_returncreate) {
            Intent intent = new Intent(); // 创建一个新意图
            setResult(Activity.RESULT_OK, intent); // 携带意图返回前一个页面
            finish(); // 关闭当前页面
        }
    }

    private void showToast(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_SHORT).show();
    }
}