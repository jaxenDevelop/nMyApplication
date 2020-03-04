package com.example.myapplication.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.SqlHelper;

public class DetailActivity extends AppCompatActivity {

    private ImageView detail_img;
    private TextView detail_name, detail_author, detail_introduce;
    private RecyclerView mat_rec, gc_rec;

    private Handler handler;
    private int id;

    private SqlHelper myDatabaseHelper;

    private String title, editor, content;
    private byte[] img;
    private String LoginUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        myDatabaseHelper = new SqlHelper(getApplicationContext());
        detail_img = findViewById(R.id.detail_img);
        detail_name = findViewById(R.id.detail_name);
        detail_author = findViewById(R.id.detail_author);
        detail_introduce = findViewById(R.id.detail_introduce);

        Intent intent = getIntent();
        id = intent.getIntExtra("flag", 0);

        initData();

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 0:
                        detail_name.setText(title);
                        detail_author.setText(editor);
                        detail_introduce.setText(content);
                        detail_img.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));

                        break;
                }
                return false;
            }
        });

    }

    private void initData() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();
                Cursor cursor = db.query("information", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        editor = cursor.getString(cursor.getColumnIndex("editor"));
                        title = cursor.getString(cursor.getColumnIndex("title"));
                        content = cursor.getString(cursor.getColumnIndex("content"));
                        img = cursor.getBlob(cursor.getColumnIndex("img"));

                    } while (cursor.moveToNext());
                }

                cursor.close();
                db.close();

                Message message = Message.obtain();
                message.what = 0;
                handler.sendMessage(message);
            }
        }).start();
    }

}
