package com.example.myapplication.home;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.SqlHelper;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView detail_img;
    private TextView detail_name, detail_author, detail_introduce;
    private RecyclerView mat_rec, gc_rec;

    private Handler handler;
    private int id;

    private SqlHelper myDatabaseHelper;

    private String title, editor, content;
    private byte[] img;
    private LinearLayout fav;
    private ImageView show_fav;
    private TextView show_fav_text;
    private boolean IsFavourite;
    private String NoFav = "收藏";
    private String HasFav = "已收藏";
    private String LoginUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        fav = findViewById(R.id.fav);
        fav.setOnClickListener(this);
        show_fav = findViewById(R.id.show_fav);
        show_fav_text = findViewById(R.id.show_fav_text);
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
                    case 1:
                        show_fav.setBackgroundResource(R.drawable.ic_favorite_border_red_200_24dp);
                        show_fav_text.setText(NoFav);

//                        collectionAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        show_fav.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                        show_fav_text.setText(HasFav);

//                        collectionAdapter.notifyDataSetChanged();
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

        //判断是否收藏
        SharedPreferences sp = getSharedPreferences("CurrentLogin", MODE_PRIVATE);
        LoginUserName = sp.getString("username", "none");
        Cursor cursor3 = db.query("favourite", null, "information_id = ? and username = ?", new String[]{String.valueOf(id), LoginUserName}, null, null, null);
        if (cursor3.getCount() == 0) {
            Message message = Message.obtain();
            message.what = 1;
            handler.sendMessage(message);

            IsFavourite = false;
        } else {
            Message message = Message.obtain();
            message.what = 2;
            handler.sendMessage(message);

            IsFavourite = true;
        }

        cursor3.close();
        cursor.close();
        db.close();

        Message message = Message.obtain();
        message.what = 0;
        handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fav:
                if (!IsFavourite) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SqlHelper myDatabaseHelper = new SqlHelper(getApplicationContext());
                            SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("information_id", id);
                            contentValues.put("username", LoginUserName);
                            sqLiteDatabase.insert("favourite", null, contentValues);
                            sqLiteDatabase.close();

                            IsFavourite = true;
                            sqLiteDatabase.close();
                            Message message = Message.obtain();
                            message.what = 2;
                            handler.sendMessage(message);
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SqlHelper myDatabaseHelper = new SqlHelper(getApplicationContext());
                            SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                            sqLiteDatabase.delete("favourite", "information_id = ? and username = ?", new String[]{String.valueOf(id), LoginUserName});
                            sqLiteDatabase.close();

                            IsFavourite = false;
                            sqLiteDatabase.close();
                            Message message = Message.obtain();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }).start();

                }
                break;
        }
    }
}
