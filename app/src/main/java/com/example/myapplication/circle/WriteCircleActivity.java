package com.example.myapplication.circle;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.SqlHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteCircleActivity extends AppCompatActivity {

    private EditText input_comment;
    private Button btn_add;
    private int Id;
    private String LoginUserName, reply_people_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //窗口对齐屏幕宽度
        Window win = this.getWindow();
        win.getDecorView().setPadding(80, 0, 80, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        setFinishOnTouchOutside(true);
        setContentView(R.layout.activity_write_circle);

        SharedPreferences sp = getSharedPreferences("CurrentLogin", MODE_PRIVATE);
        LoginUserName = sp.getString("username", "none");

        input_comment = findViewById(R.id.input_comment);

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = input_comment.getText().toString().trim();
                if (comment.length() == 0)
                    Toast.makeText(getApplicationContext(), "内容不能为空", Toast.LENGTH_LONG).show();
                else {
                    //塞数据
                    SqlHelper sqlHelper = new SqlHelper(getApplicationContext());
                    SQLiteDatabase db = sqlHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("username", LoginUserName);
                    values.put("time", LongToString(System.currentTimeMillis()));
                    values.put("content", comment);
                    values.put("discuss_number", 0);
                    db.insert("comment", null, values);

                    Toast.makeText(getApplicationContext(), "发表成功！", Toast.LENGTH_LONG).show();

                    //发送广播，更新成绩单数据
                    Intent intent = new Intent();
                    intent.setAction("AddComment");
                    sendBroadcast(intent);

                    db.close();
                    finish();
                }

            }
        });
    }

    private String LongToString(long data) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = new Date(data);
        return simpleDateFormat.format(date);
    }
}
