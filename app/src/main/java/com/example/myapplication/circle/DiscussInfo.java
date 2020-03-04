package com.example.myapplication.circle;

public class DiscussInfo {

    public String username;
    public int id, IsFav, discuss_number, topID, level, circle_id;
    public String editor, title, time, read_number, content, reply_people_name;

    String sql4 = "CREATE TABLE discuss(id INTEGER PRIMARY KEY AUTOINCREMENT,circle_id Integer,username TEXT, reply_people_name TEXT,time TEXT, content TEXT)";

    public DiscussInfo(int id, int circle_id, String username, String reply_people_name, String time, String content) {

        this.id = id;
        this.circle_id = circle_id;
        this.username = username;
        this.reply_people_name = reply_people_name;
        this.time = time;
        this.content = content;
    }
}