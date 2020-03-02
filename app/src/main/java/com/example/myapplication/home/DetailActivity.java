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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dingcan.R;
import com.example.dingcan.tools.MenuInfo;
import com.example.dingcan.ui.db.SqlHellper;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView detail_img;
    private TextView detail_dishname, detail_price, detail_introduce, good_num;
    private RecyclerView pl_rec;
    private Button add_to_cart, add, decline;

    private Handler handler;
    private String identity;

    private SqlHellper myDatabaseHelper;


    private String DishName, Price, Introduce, LoginUserName;
    private byte[] img;

    private DetailCommentAdapter detailMaterialAdapter;

    private List<String> MaterialNameList, MaterialAmountList, GcStepList, GcIntroduceList;
    private List<byte[]> GcImgList;
    private List<MenuInfo> menuInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        SharedPreferences sp = getSharedPreferences("CurrentLogin", MODE_PRIVATE);
        LoginUserName = sp.getString("username", "none");

        add = findViewById(R.id.add);
        decline = findViewById(R.id.decline);
        add_to_cart = findViewById(R.id.add_to_cart);
        add.setOnClickListener(this);
        decline.setOnClickListener(this);
        add_to_cart.setOnClickListener(this);

        pl_rec = findViewById(R.id.pl_rec);
        good_num = findViewById(R.id.good_num);
        menuInfos = new ArrayList<>();
        myDatabaseHelper = new SqlHellper(getApplicationContext());
        MaterialNameList = new ArrayList<>();
        MaterialAmountList = new ArrayList<>();
        GcStepList = new ArrayList<>();
        GcImgList = new ArrayList<>();
        GcIntroduceList = new ArrayList<>();

        detailMaterialAdapter = new DetailCommentAdapter(getApplicationContext(), menuInfos);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        /**网格布局**/
        pl_rec.setLayoutManager(linearLayoutManager);
        pl_rec.setNestedScrollingEnabled(false);
        pl_rec.setAdapter(detailMaterialAdapter);


        detail_img = findViewById(R.id.detail_img);
        detail_dishname = findViewById(R.id.detail_dishname);
        detail_price = findViewById(R.id.detail_price);
        detail_introduce = findViewById(R.id.detail_introduce);


        Intent intent = getIntent();
        identity = intent.getStringExtra("flag");

        initData();

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 0:
                        detail_dishname.setText(DishName);
                        detail_price.setText("¥" + Price);
                        detail_introduce.setText(Introduce);
                        detail_img.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));

                        detailMaterialAdapter.notifyDataSetChanged();
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
                Cursor cursor = db.query("menu", null, "identity = ?", new String[]{identity}, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        DishName = cursor.getString(cursor.getColumnIndex("dishname"));
                        Price = cursor.getString(cursor.getColumnIndex("price"));
                        Introduce = cursor.getString(cursor.getColumnIndex("introduce"));

                        img = cursor.getBlob(cursor.getColumnIndex("img"));

                    } while (cursor.moveToNext());
                }


                Cursor cursor1 = db.query("discuss", null, "identity = ?", new String[]{identity}, null, null, null);
                if (cursor1.moveToFirst()) {
                    do {
                        menuInfos.add(new MenuInfo(
                                cursor1.getString(cursor1.getColumnIndex("username")),
                                cursor1.getString(cursor1.getColumnIndex("time")),
                                cursor1.getString(cursor1.getColumnIndex("comment"))
                        ));

                    } while (cursor1.moveToNext());
                }


                cursor1.close();
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
            case R.id.decline:
                Integer numberTemp = Integer.parseInt(good_num.getText().toString());
                numberTemp--;
                if (numberTemp < 1) {
                    numberTemp = 1;
                }
                good_num.setText(String.valueOf(numberTemp));
                break;

            case R.id.add:

                Integer numberTemp1 = Integer.parseInt(good_num.getText().toString());
                numberTemp1++;
                good_num.setText(String.valueOf(numberTemp1));

                break;

            case R.id.add_to_cart:
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("identity", identity);
                values.put("img", img);
                values.put("dishname", DishName);
                values.put("username", LoginUserName);
                values.put("price", Price);
                values.put("nums", good_num.getText().toString());
                values.put("active", "0");

                long id = db.insert("orderform", null, values);
                if (id != 0) {
                    Toast.makeText(DetailActivity.this, "添加购物车成功，可在购物车中查看", Toast.LENGTH_SHORT).show();
                    good_num.setText("1");
                    Intent intent = new Intent();
                    intent.setAction("REFRESHDATA");
                    sendBroadcast(intent);

                    finish();
                }
                break;
        }
    }
}
