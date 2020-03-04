package com.example.myapplication.circle;

public class CircleInfo {

    public String username;
    public int id, IsFav, discuss_number;
    public String editor, title, time, read_number, content;

    public CircleInfo(int id, String username, String time, String content, int IsFav, int discuss_number) {

        this.id = id;
        this.username = username;
        this.time = time;
        this.content = content;
        this.IsFav = IsFav;
        this.discuss_number = discuss_number;
    }
}