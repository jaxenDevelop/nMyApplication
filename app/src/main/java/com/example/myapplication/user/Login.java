package com.example.myapplication.user;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.SqlHellper;

import static android.widget.Toast.makeText;

public class Login extends Activity {
    private EditText et_user;
    private EditText et_password;
    private Button btn_login, manager_login;
    private TextView icon_show;
    private boolean show = false;
    private TextView register;
    private Boolean has_register;
    private Handler handler;
    private int IsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //初始化控件
        initView();
        //初始化管理员账号与预置课程
        initManagerAccount();

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 0:
                        login(IsManager);
                        break;
                }
                return false;
            }
        });
    }

    private void initManagerAccount() {
        SqlHellper myDatabaseHelper = new SqlHellper(getApplicationContext());
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query("manager", null, null, null, null, null, null);
        //初始化管理员账号
        if (cursor.getCount() < 1) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", "admin");
            contentValues.put("password", "admin");
            db.insert("manager", null, contentValues);

            //预置课程
            contentValues.clear();
            contentValues.put("name", "c++语言程序设计");
            contentValues.put("day", "1");
            contentValues.put("number", "2");
            db.insert("course", null, contentValues);


            contentValues.clear();
            contentValues.put("name", "Java编程");
            contentValues.put("day", "2");
            contentValues.put("number", "1");
            db.insert("course", null, contentValues);

            contentValues.clear();
            contentValues.put("name", "编译原理");
            contentValues.put("day", "3");
            contentValues.put("number", "3");
            db.insert("course", null, contentValues);

            contentValues.clear();
            contentValues.put("name", "计算机系统");
            contentValues.put("day", "4");
            contentValues.put("number", "4");
            db.insert("course", null, contentValues);

            contentValues.clear();
            contentValues.put("name", "计算机原理");
            contentValues.put("day", "5");
            contentValues.put("number", "2");
            db.insert("course", null, contentValues);



//            contentValues.clear();
//            contentValues.put("account", "123");
//            contentValues.put("course", "网络编程");
//            contentValues.put("grade", "97");
//            db.insert("score", null, contentValues);


        }


        cursor.close();
        db.close();

    }

    protected void initView() {
        TextView icon_user = findViewById(R.id.icon_user);
        TextView icon_pass = findViewById(R.id.icon_pass);
        et_user = findViewById(R.id.et_user);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        manager_login = findViewById(R.id.manager_login);

        icon_show = findViewById(R.id.icon_show);
        register = findViewById(R.id.register);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        icon_user.setTypeface(font);
        icon_pass.setTypeface(font);
        icon_show.setTypeface(font);
        icon_user.setText(getResources().getString(R.string.user));
        icon_pass.setText(getResources().getString(R.string.password));
        icon_show.setText(getResources().getString(R.string.eye_close));

        //学生登录
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IsManager = 0;

                Message message = Message.obtain();
                message.what = 0;
                handler.sendMessage(message);
            }
        });

        //管理员登录
        manager_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                IsManager = 1;
                login(IsManager);
            }
        });

        //显示、隐藏密码
        icon_show.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                show = !show;
                if (show) {
                    icon_show.setText(getResources().getString(R.string.eye_open));
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //密码不可见
                    icon_show.setText(getResources().getString(R.string.eye_close));
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //注册按钮
        register.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

    }

    //获取账号
    public String getUser() {
        return et_user.getText().toString().trim();
    }

    //获取密码
    public String getPassword() {
        return et_password.getText().toString().trim();
    }

    //点击登录按钮后的逻辑判断
    public void login(int i) {
        if (getUser().isEmpty()) {
            showToast("请输入用户名！");
            return;
        }
        if (getPassword().isEmpty()) {
            showToast("请输入密码！");
            return;
        }
        setLoginBtnClickable(false);
        //判断账号密码
        SqlHellper sqlHellper = new SqlHellper(this);
        SQLiteDatabase db = sqlHellper.getWritableDatabase();
        String sql = null;
        if (i == 0)
            sql = "select * from userInfo where username = ? and password = ?";
        else if (i == 1)
            sql = "select * from manager where username = ? and password = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{getUser(), getPassword()});
        if (cursor.moveToFirst()) {
            cursor.close();
            showToast("登陆成功");
            SaveUserInfo(i);

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("IsManager", i);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

//            finish();
        } else {
            showToast("账号或密码不正确");
        }
        setLoginBtnClickable(true);
    }

    //保存当前登录账号
    private void SaveUserInfo(int i) {
        SharedPreferences sp = getSharedPreferences("CurrentLogin", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", getUser());
        if (i == 0)
            editor.putInt("IsManager", 0);
        else if (i == 1)
            editor.putInt("IsManager", 1);

        editor.apply();
    }

    public void setLoginBtnClickable(Boolean clickable) {
        btn_login.setClickable(clickable);
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
