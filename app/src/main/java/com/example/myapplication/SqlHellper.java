package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqlHellper extends SQLiteOpenHelper {
    private static final String TAG = "TestSQLite";
    private static final String DBNAME = "managestudent.db";          //数据库名字
    private static final int DATABASE_VERSION = 1;                //数据库版本号


    public SqlHellper(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //管理员表
        String sql = "CREATE TABLE manager(id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT ,password TEXT)";
        //用户表:账号，密码，注册时间
        String sql0 = "CREATE TABLE userInfo(id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT ,password TEXT, time TEXT)";
        //课程信息：课程id，课程名称，周几，第几节
        String sql1 = "CREATE TABLE course(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,day TEXT,number TEXT)";
        //成绩表：姓名，科目，成绩
        String sql2 = "CREATE TABLE score(id INTEGER PRIMARY KEY AUTOINCREMENT,account TEXT,course TEXT,grade TEXT)";
        //学生信息表：登录账号名，学生姓名，班级，照片
        String sql3 = "CREATE TABLE student(id INTEGER PRIMARY KEY AUTOINCREMENT,nickname TEXT,name TEXT,class TEXT, img BLOB)";
        //通知表：ID，标题，内容，img
        String sql4 = "CREATE TABLE notification(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,content TEXT, img BLOB)";
        //报名表：ID，标题，已报名账号
        String sql5 = "CREATE TABLE signrecord(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT, account TEXT)";

        Log.i(TAG, "Create database userinfo-----------");

        db.execSQL(sql);
        db.execSQL(sql0);
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Update database -----------");
    }
}
