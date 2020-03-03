package com.example.myapplication.user;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.SqlHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.makeText;

public class Register extends Activity {
    private EditText register_user;
    private EditText register_password;
    private EditText register_repassword;
    private TextView register_show, back;
    private TextView register_show2;
    private boolean register_password_show = false;
    private boolean register_repassword_show = false;
    private Button register_btn;
    private SqlHelper sqlHelper;
    private ImageView back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initView();
        setupEvents();
    }

    //点击返回按钮
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            overridePendingTransition(R.anim.back_left_in, R.anim.back_right_out);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void initView() {
        register_user = (EditText) findViewById(R.id.register_user);
        register_password = (EditText) findViewById(R.id.register_password);
        register_repassword = (EditText) findViewById(R.id.register_repassword);
        register_show = (TextView) findViewById(R.id.register_show);
        register_show2 = (TextView) findViewById(R.id.register_show2);
        register_btn = (Button) findViewById(R.id.register_btn);
        back_arrow = findViewById(R.id.back_arrow);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        register_show.setTypeface(font);
        register_show2.setTypeface(font);
        register_show.setText(getResources().getString(R.string.eye_close));
        register_show2.setText(getResources().getString(R.string.eye_close));

        sqlHelper = new SqlHelper(this);
    }

    public String getUsername() {
        return register_user.getText().toString().trim();
    }

    public String getPass() {
        return register_password.getText().toString().trim();
    }

    public String getRepass() {
        return register_repassword.getText().toString().trim();
    }

    //设置点击监听
    protected void setupEvents() {
        register_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_password_show = !register_password_show;
                if (register_password_show) {
                    register_show.setText(getResources().getString(R.string.eye_open));
                    register_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //密码不可见
                    register_show.setText(getResources().getString(R.string.eye_close));
                    register_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        register_show2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_repassword_show = !register_repassword_show;
                if (register_repassword_show) {
                    register_show2.setText(getResources().getString(R.string.eye_open));
                    register_repassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //密码不可见
                    register_show2.setText(getResources().getString(R.string.eye_close));
                    register_repassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoAndCheck();
            }
        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getInfoAndCheck() {
        Boolean has_register = false;
        if (getUsername().isEmpty()) {
            showToast("请输入用户名");
            return;
        }
        if (getPass().isEmpty()) {
            showToast("请输入密码");
            return;
        }
        if (getRepass().isEmpty()) {
            showToast("请确认重复密码");
            return;
        }
        if (!getPass().equals(getRepass())) {
            showToast("两次密码不一致");
            return;
        }
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        Cursor cursor = db.query("userInfo", new String[]{"username", "password"}, "username=?", new String[]{getUsername()}, null, null, null);
        has_register = cursor.moveToNext();
        if (has_register) {
            showToast("您已注册过");
            return;
        } else {
            ContentValues values = new ContentValues();
            values.put("username", getUsername());
            values.put("password", getPass());
            values.put("time", LongToDate(System.currentTimeMillis()));
            long id = db.insert("userInfo", null, values);
            showToast("注册成功，请牢记您的账号！");
            finish();
        }
        cursor.close();//关闭游标
        db.close();
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                makeText(Register.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String LongToDate(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = new Date(time);
        return simpleDateFormat.format(date);
    }
}
