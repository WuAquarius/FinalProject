package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
public class DeleteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_iddrop;
    private EditText et_isbndrop;
    private Button bt_delete;
    private Button bt_returndrop;
    private String result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        et_iddrop = findViewById(R.id.et_iddrop);
        et_isbndrop = findViewById(R.id.et_isbndrop);
        bt_delete = findViewById(R.id.bt_delete);
        bt_returndrop = findViewById(R.id.bt_returndrop);

        bt_delete.setOnClickListener(this);
        bt_returndrop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_iddrop) {
            final String iddel = et_iddrop.getText().toString().trim();
            final String isbndel = et_isbndrop.getText().toString().trim();
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
                            String url = HttpURLConn.BASE_URL + "/studentDelServlet";
                            Map<String, String> params = new HashMap<>();
                            params.put("iddel", iddel);
                            params.put("isbndel", isbndel);
                            result = HttpURLConn.getContextByHttp(url, params);
                            System.out.println(result);
                        }
                    }).start();
                }
            });

            if (result == null) {
                Toast.makeText(DeleteActivity.this, "不存在该记录，删除失败。", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DeleteActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            }
            // 取消删除
            builder.setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(DeleteActivity.this, "哈哈哈，你怂了！！", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (v.getId() == R.id.bt_returndrop) {
            Intent intent = new Intent(); // 创建一个新意图
            setResult(Activity.RESULT_OK, intent); // 携带意图返回前一个页面
            finish(); // 关闭当前页面
        }
    }
}