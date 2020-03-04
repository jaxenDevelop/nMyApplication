package com.example.myapplication.circle;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.SqlHelper;

import java.util.ArrayList;
import java.util.List;

public class ShowDiscussAcitvity extends AppCompatActivity implements View.OnClickListener {
    private TextView show_username, show_time, show_content, show_fav_state;
    private int id;
    private DiscussRecycleAdapter discussRecycleAdapter;
    private RecyclerView circle_view;
    private List<DiscussInfo> menuInfos;
    private String circle_username;
    private LinearLayout line_fav, line_answer;
    private String LoginUserName, reply_user;
    private boolean IsFav;
    private ImageView show_favourite;
    private ReceiveMessage receiveMessage;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_discuss_acitvity);

        SharedPreferences sp = getSharedPreferences("CurrentLogin", MODE_PRIVATE);
        LoginUserName = sp.getString("username", "none");

        show_fav_state = findViewById(R.id.show_fav_state);
        show_favourite = findViewById(R.id.show_favourite);
        menuInfos = new ArrayList<>();
        line_fav = findViewById(R.id.line_fav);
        line_fav.setOnClickListener(this);
        line_answer = findViewById(R.id.line_answer);
        line_answer.setOnClickListener(this);
        show_username = findViewById(R.id.show_username);
        show_time = findViewById(R.id.show_time);
        show_content = findViewById(R.id.show_content);
        circle_view = findViewById(R.id.circle_view);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        reply_user = intent.getStringExtra("reply_people_name");


        TextViewInit();

        initRec();
        discussRecycleAdapter = new DiscussRecycleAdapter(this, menuInfos, circle_username);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        circle_view.setAdapter(discussRecycleAdapter);
        circle_view.setLayoutManager(linearLayoutManager);
        /**nestedScroll嵌套recycleView，添加此段代码使滑动带惯性**/
        circle_view.setNestedScrollingEnabled(false);

        receiveMessage = new ReceiveMessage();
        intentFilter = new IntentFilter();
        intentFilter.addAction("AddComment");
        registerReceiver(receiveMessage, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiveMessage);
    }

    private void initRec() {
        menuInfos.clear();
        SqlHelper sqlHelper = new SqlHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = sqlHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("discuss", null, "circle_id = ?", new String[]{String.valueOf(id)}, null, null, "id desc");
        if (cursor.moveToFirst()) {
            do {
                menuInfos.add(new DiscussInfo(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("circle_id")),
                        cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getString(cursor.getColumnIndex("reply_people_name")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("content"))
                ));
            }
            while (cursor.moveToNext());
        }

        Cursor cursor1 = sqLiteDatabase.query("comment", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor1.moveToFirst()) {
            do {
                circle_username = cursor1.getString(cursor1.getColumnIndex("username"));
            } while (cursor1.moveToNext());

        }
        cursor1.close();
        cursor.close();
        sqLiteDatabase.close();
    }

    private void TextViewInit() {
        SqlHelper sqlHelper = new SqlHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = sqlHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("comment", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            show_username.setText(cursor.getString(cursor.getColumnIndex("username")));
            show_time.setText(cursor.getString(cursor.getColumnIndex("time")));
            show_content.setText(cursor.getString(cursor.getColumnIndex("content")));
        }

        Cursor cursor1 = sqLiteDatabase.query("favourite", null, "information_id = ? and username = ?", new String[]{String.valueOf(id), LoginUserName}, null, null, null);
        if (cursor1.moveToFirst()) {
            show_favourite.setImageResource(R.drawable.ic_favorite_black_24dp);
            show_fav_state.setText("已收藏");
            IsFav = true;

        } else {
            show_favourite.setImageResource(R.drawable.ic_favorite_border_red_200_24dp);
            show_fav_state.setText("收藏");
            IsFav = false;
        }

        cursor.close();
        sqLiteDatabase.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.line_fav:
                if (IsFav) {
                    show_favourite.setImageResource(R.drawable.ic_favorite_border_red_200_24dp);
                    show_fav_state.setText("收藏");
                    IsFav = false;

                    SqlHelper myDatabaseHelper = new SqlHelper(getApplicationContext());
                    SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                    sqLiteDatabase.delete("favourite", "information_id = ? and username = ?", new String[]{String.valueOf(id), LoginUserName});
                    sqLiteDatabase.close();

                    Toast.makeText(getApplicationContext(), "取消收藏！", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setAction("AddComment");
                    sendBroadcast(intent);
                } else {
                    show_favourite.setImageResource(R.drawable.ic_favorite_black_24dp);
                    show_fav_state.setText("已收藏");
                    IsFav = true;

                    SqlHelper myDatabaseHelper = new SqlHelper(getApplicationContext());
                    SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("information_id", id);
                    contentValues.put("username", LoginUserName);
                    sqLiteDatabase.insert("favourite", null, contentValues);

                    sqLiteDatabase.close();
                    Toast.makeText(getApplicationContext(), "已收藏！", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setAction("AddComment");
                    sendBroadcast(intent);
                }
                break;

            case R.id.line_answer:
                Intent intent = new Intent(getApplicationContext(), WriteAnswerActivity.class);
//                //flag： 0：回复发帖人  1：回复其他的人
//                intent.putExtra("flag", 0);
                intent.putExtra("circle_id", id);
                intent.putExtra("reply_people_name", reply_user);
                startActivity(intent);
                break;
        }
    }

    public class ReceiveMessage extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            initRec();
            discussRecycleAdapter.notifyDataSetChanged();
        }
    }
}
