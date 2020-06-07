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

    private Button bt_createe;
    private Button bt_updatee;
    private Button bt_deletee;
    private Button bt_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        bt_createe = findViewById(R.id.bt_createe);
        bt_createe.setOnClickListener(this);

        bt_updatee = findViewById(R.id.bt_updatee);
        bt_updatee.setOnClickListener(this);

        bt_deletee = findViewById(R.id.bt_deletee);
        bt_deletee.setOnClickListener(this);

        bt_read = findViewById(R.id.bt_read);
        bt_read.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_createe){
            // 准备跳到下一个活动页面，studentList
            Intent intent = new Intent(CRUDActivity.this, CreateActivity.class);
            startActivityForResult(intent, 0);
        }else if (v.getId() == R.id.bt_updatee){
            // 准备跳到下一个活动页面，studentList
            Intent intent = new Intent(CRUDActivity.this, UpdateActivity.class);
            startActivityForResult(intent, 1);
        }else if (v.getId() == R.id.bt_deletee){
            // 准备跳到下一个活动页面，studentList
            Intent intent = new Intent(CRUDActivity.this, DeleteActivity.class);
            startActivityForResult(intent, 2);
        }else if (v.getId() == R.id.bt_read){
            // 准备跳到下一个活动页面，studentList
            Intent intent = new Intent(CRUDActivity.this, studentList.class);
            startActivityForResult(intent, 3);
        }
    }
}