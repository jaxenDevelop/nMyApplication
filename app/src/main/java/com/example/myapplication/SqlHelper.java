package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqlHelper extends SQLiteOpenHelper {
    private static final String TAG = "TestSQLite";
    private static final String DBNAME = "agriculture.db";          //数据库名字
    private static final int DATABASE_VERSION = 1;                //数据库版本号


    public SqlHelper(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //管理员表
        String sql = "CREATE TABLE manager(id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT ,password TEXT)";
        //用户表:账号，密码，注册时间
        String sql0 = "CREATE TABLE userInfo(id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT,password TEXT,time TEXT,img BLOB)";
        //信息表：新闻id，作者，标题，时间，图片，读者人数，内容
        String sql1 = "CREATE TABLE information(id INTEGER PRIMARY KEY AUTOINCREMENT,editor TEXT,title TEXT,time TEXT, img BLOB, read_number Integer, content TEXT)";
        //收藏表：帖子id，收藏的用户名
        String sql2 = "CREATE TABLE favourite(id INTEGER PRIMARY KEY AUTOINCREMENT,information_id INTEGER,username TEXT)";
        //评论表：发表帖子id， 用户名，时间
        String sql3 = "CREATE TABLE comment(id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT,time TEXT, content TEXT,discuss_number Integer)";
        //回复表：回复内容id，圈子中评论的ID，发表圈子的人员用户名，回复人员用户名，回复的人的名称, 时间，上级id,帖子等级，内容
        String sql4 = "CREATE TABLE discuss(id INTEGER PRIMARY KEY AUTOINCREMENT,circle_id Integer,username TEXT, reply_people_name TEXT,time TEXT, content TEXT)";
        //申请专家：申请ID，用户名，申请图片，申请文字
        String sql5 = "CREATE TABLE specialist(id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT,img BLOB,material TEXT)";
//        //成绩表：姓名，科目，成绩
//        String sql2 = "CREATE TABLE score(id INTEGER PRIMARY KEY AUTOINCREMENT,account TEXT,course TEXT,grade TEXT)";
//        //学生信息表：登录账号名，学生姓名，班级，照片
//        String sql3 = "CREATE TABLE student(id INTEGER PRIMARY KEY AUTOINCREMENT,nickname TEXT,name TEXT,class TEXT, img BLOB)";
//        //通知表：ID，标题，内容，img
//        String sql4 = "CREATE TABLE notification(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,content TEXT, img BLOB)";
//        //报名表：ID，标题，已报名账号
//        String sql5 = "CREATE TABLE signrecord(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT, account TEXT)";

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
