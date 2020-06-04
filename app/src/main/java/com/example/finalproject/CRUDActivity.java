package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;

/**
 * @author wunu
 * 选择操作页面
 */
public class CRUDActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_create;
    private Button bt_update;
    private Button bt_delete;
    private Button bt_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        bt_create = findViewById(R.id.bt_create);
        bt_create.setOnClickListener(this);

        bt_update = findViewById(R.id.bt_update);
        bt_update.setOnClickListener(this);

        bt_delete = findViewById(R.id.bt_delete);
        bt_delete.setOnClickListener(this);

        bt_read = findViewById(R.id.bt_read);
        bt_read.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_create){
            jumpPage(CreateActivity.class);
        }else if (v.getId() == R.id.bt_update){
            jumpPage(UpdateActivity.class);
        }else if (v.getId() == R.id.bt_delete){
            jumpPage(DeleteActivity.class);
        }else if (v.getId() == R.id.bt_read){
            jumpPage(studentList.class);
        }
    }

    private void jumpPage(Class<?> cls){
        // 准备跳到下一个活动页面，studentList
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, 0);
    }
}