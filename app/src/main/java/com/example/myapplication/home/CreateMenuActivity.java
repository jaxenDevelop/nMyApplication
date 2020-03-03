package com.example.myapplication.home;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.SqlHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView cancel;
    private ImageView set_create_img;
    private EditText input_menu, input_introduce;
    private Handler handler;
    private Uri uri;
    private Button publish_menu;
    private String LoginUserName;
    //首页图片uri存储
    private List<Uri> TitleImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_menu);

        TitleImgUri = new ArrayList<>();

        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        set_create_img = findViewById(R.id.set_create_img);
        set_create_img.setImageResource(R.drawable.fm);
        set_create_img.setOnClickListener(this);


        input_menu = findViewById(R.id.input_menu);
        input_introduce = findViewById(R.id.input_introduce);

        publish_menu = findViewById(R.id.publish_menu);
        publish_menu.setOnClickListener(this);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 0:
                        //标题图片uri存储
                        try {
                            if (TitleImgUri.size() == 0)
                                TitleImgUri.add(uri);
                            else {
                                TitleImgUri.clear();
                                TitleImgUri.add(uri);
                            }
                            //展示选择的图片
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(CreateMenuActivity.this.getContentResolver(), uri);
                            set_create_img.setImageResource(0);
                            set_create_img.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return false;
            }
        });

        //获取当前登录账号
        SharedPreferences sp = getSharedPreferences("CurrentLogin", MODE_PRIVATE);
        LoginUserName = sp.getString("username", "none");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_create_img:
                //从相册选择图片
                getPicFromAlbm();
                break;

            case R.id.publish_menu:
                if (TitleImgUri.size() == 0)
                    Toast.makeText(getApplicationContext(), "请添加封面图片！", Toast.LENGTH_LONG).show();
                else if (input_menu.getText().toString().length() < 1
                        || input_introduce.getText().toString().length() < 1)
                    Toast.makeText(getApplicationContext(), "标题、介绍不能为空！", Toast.LENGTH_LONG).show();
                else {
                    //存储数据
                    SqlHelper sqlHelper = new SqlHelper(getApplicationContext());
                    SQLiteDatabase db = sqlHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("editor", LoginUserName);
                    values.put("title", input_menu.getText().toString());
                    values.put("content", input_introduce.getText().toString());
                    values.put("img", UriToByte(TitleImgUri.get(0)));
                    values.put("time", LongToData(System.currentTimeMillis()));
                    values.put("read_number", 0);
                    db.insert("information", null, values);
                    db.close();

                    Toast.makeText(getApplicationContext(), "发布成功！", Toast.LENGTH_LONG).show();

                    //发送广播更新home列表
                    Intent intent = new Intent();
                    intent.setAction("PUBLISHINFO");
                    sendBroadcast(intent);
                    finish();
                }
                break;

            case R.id.cancel:
                finish();
                break;
        }
    }

    //时间戳转日期格式
    private String LongToData(long currentTimeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(currentTimeMillis);
        return simpleDateFormat.format(date);
    }

    /**
     * 从相机获取图片
     */
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                //调用相册后返回
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    //handler更新ui
                    Message message = Message.obtain();
                    message.what = 0;
                    handler.sendMessage(message);
                }

                break;
        }
    }

    //uri转比特
    public byte[] UriToByte(Uri uri) {
        Bitmap bitmap1 = null;
        try {
            bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int size = bitmap1.getWidth() * bitmap1.getHeight() * 4;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imagedata1 = baos.toByteArray();

        return imagedata1;
    }
}
