package com.example.myapplication.mine;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.SqlHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView select_img;
    private EditText input_text;
    private Handler handler;
    private Uri uri;
    private Button commit;
    private String LoginUserName;
    //首页图片uri存储
    private List<Uri> TitleImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(true);
        setContentView(R.layout.activity_application_specialist);

        TitleImgUri = new ArrayList<>();

        select_img = findViewById(R.id.select_img);
        select_img.setImageResource(R.drawable.bac);
        select_img.setOnClickListener(this);


        input_text = findViewById(R.id.input_text);
        commit = findViewById(R.id.commit);
        commit.setOnClickListener(this);

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
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(MessageActivity.this.getContentResolver(), uri);
                            select_img.setImageResource(0);
                            select_img.setImageBitmap(bitmap);
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
            case R.id.select_img:
                //从相册选择图片
                getPicFromAlbm();
                break;

            case R.id.commit:
                if (TitleImgUri.size() == 0)
                    Toast.makeText(getApplicationContext(), "请添加申请图片！", Toast.LENGTH_LONG).show();
                else if (input_text.getText().toString().length() < 1)
                    Toast.makeText(getApplicationContext(), "申请文字不能为空！", Toast.LENGTH_LONG).show();
                else {
                    String sql5 = "CREATE TABLE specialist(id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT,img BLOB,material TEXT)";
                    //存储数据
                    SqlHelper sqlHelper = new SqlHelper(getApplicationContext());
                    SQLiteDatabase db = sqlHelper.getReadableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("username", LoginUserName);
                    values.put("img", UriToByte(TitleImgUri.get(0)));
                    values.put("material", input_text.getText().toString());
                    db.insert("specialist", null, values);
                    db.close();

                    Toast.makeText(getApplicationContext(), "提交申请成功！", Toast.LENGTH_LONG).show();

                    finish();
                }
                break;
        }
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
