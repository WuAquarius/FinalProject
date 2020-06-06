package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.bean.Student;
import com.example.finalproject.util.HttpURLConn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wunu
 */
@SuppressWarnings("all")
public class studentList extends AppCompatActivity implements View.OnClickListener {

    private List<Student> allstudent;
    private ListView lv;
    private ImageButton ib_search;
    private Button bt_return;
    private EditText et_find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        lv = findViewById(R.id.lv);
        et_find = findViewById(R.id.et_find);
        allstudent = new ArrayList<>();
        ib_search.findViewById(R.id.ib_search);
        ib_search.setOnClickListener(this);
        bt_return.findViewById(R.id.bt_return);
        bt_return.setOnClickListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = HttpURLConn.BASE_URL + "/studentQuery";
                Map<String, String> params = new HashMap<>();
                // params.put("flag", "1");
                params.put("key", "");
                String resule = HttpURLConn.getContextByHttp(url, params);

                Message message = new Message();
                message.what = 0x11;
                Bundle data = new Bundle();
                data.putString("resule", resule);
                System.out.println(resule);
                message.setData(data);
                handler.sendMessage(message);
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0x11) {
                Bundle data = msg.getData();
                String key = data.getString("result");
                try {
                    JSONObject json = new JSONObject(key);
                    // 获取JSONArray数组在指定位置的JSONObject对象
                    JSONArray listArray = json.getJSONArray("students");
                    for (int i = 0; i < listArray.length(); i++) {
                        // 获得数组中指定下标的JSON对象
                        JSONObject jsonObject = listArray.getJSONObject(i);
                        String id = jsonObject.getString("no");         // 学号
                        String name = jsonObject.getString("name");     // 姓名
                        String major = jsonObject.getString("major");   // 专业
                        String isbn = jsonObject.getString("isbn");     // 书条码
                        Student student = new Student(id, name, major, isbn);
                        allstudent.add(student);
                    }
                    // 显示查询结果
                    lv.setAdapter(new MyAdapter());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 进行查询操作
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_search){
            final String find = et_find.getText().toString().trim();
            if (TextUtils.isEmpty(find)){
                Toast.makeText(this,"查找条件不能为空",Toast.LENGTH_SHORT).show();
            }else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = HttpURLConn.BASE_URL + "/studentQuery";
                        Map<String,String> params = new HashMap<>();
                        // params.put("flag","1");
                        params.put("key",find);
                        String result = HttpURLConn.getContextByHttp(url, params);

                        Message message = new Message();
                        message.what = 0x11;
                        Bundle data = new Bundle();
                        data.putString("result",result);
                        System.out.println(result);
                        message.setData(data);
                        handler.sendMessage(message);
                    }
                }).start();
            }
        }else if (v.getId() == R.id.bt_return){
            Intent intent = new Intent(); // 创建一个新意图
            setResult(Activity.RESULT_OK, intent); // 携带意图返回前一个页面
            finish(); // 关闭当前页面
        }
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return allstudent.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(studentList.this, R.layout.select_item, null);
            TextView tv_id = view.findViewById(R.id.tv_id);
            TextView tv_name = view.findViewById(R.id.tv_name);
            TextView tv_major = view.findViewById(R.id.tv_major);
            TextView tv_isbn = view.findViewById(R.id.tv_isbn);
            tv_id.setText(allstudent.get(position).getId());
            tv_name.setText(allstudent.get(position).getName());
            tv_major.setText(allstudent.get(position).getMajor());
            tv_isbn.setText(allstudent.get(position).getIsbn());
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}