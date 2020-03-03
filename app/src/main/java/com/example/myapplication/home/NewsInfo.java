package com.example.myapplication.home;

public class NewsInfo {

    public int id;
    public String editor, title, time, read_number, content;
    public byte[] img;

    public NewsInfo(int id, String editor, String title, String time, byte[] img, String read_number, String content) {

        this.id = id;
        this.editor = editor;
        this.title = title;
        this.time = time;
        this.img = img;
        this.read_number = read_number;
        this.content = content;
    }
}