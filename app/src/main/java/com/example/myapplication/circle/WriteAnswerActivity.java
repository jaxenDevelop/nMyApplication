package com.example.myapplication.circle;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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


/**
 * 单独添加一个成绩
 **/
public class WriteAnswerActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_write_answer);

        SharedPreferences sp = getSharedPreferences("CurrentLogin", MODE_PRIVATE);
        LoginUserName = sp.getString("username", "none");

        Intent intent = getIntent();
        Id = intent.getIntExtra("circle_id", 0);
        reply_people_name = intent.getStringExtra("reply_people_name");

        input_comment = findViewById(R.id.input_comment);

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = input_comment.getText().toString().trim();
                if (comment.length() == 0)
                    Toast.makeText(getApplicationContext(), "内容不能为空", Toast.LENGTH_LONG).show();
                else {
                    String sql4 = "CREATE TABLE discuss(id INTEGER PRIMARY KEY AUTOINCREMENT,circle_id Integer,username TEXT, reply_people_name TEXT,time TEXT, content TEXT)";
                    //塞数据
                    SqlHelper sqlHelper = new SqlHelper(getApplicationContext());
                    SQLiteDatabase db = sqlHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("circle_id", Id);
                    values.put("username", LoginUserName);
                    values.put("reply_people_name", reply_people_name);
                    values.put("time", LongToString(System.currentTimeMillis()));
                    values.put("content", comment);
                    db.insert("discuss", null, values);

                    values.clear();
                    int number = 0;
                    Cursor cursor = db.query("comment", null, "id = ?", new String[]{String.valueOf(Id)}, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            number = cursor.getInt(cursor.getColumnIndex("discuss_number"));
                        } while (cursor.moveToNext());

                    }
                    number++;
                    values.put("discuss_number", number);
                    db.update("comment", values, "id = ?", new String[]{String.valueOf(Id)});

                    Toast.makeText(getApplicationContext(), "添加成功！", Toast.LENGTH_LONG).show();

                    //发送广播，更新成绩单数据
                    Intent intent = new Intent();
                    intent.setAction("AddComment");
                    sendBroadcast(intent);

                    Intent intent1 = new Intent();
                    intent1.setAction("AddDiscuss");
                    sendBroadcast(intent1);
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
