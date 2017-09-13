package com.robotshell.timerecorder.bean;


public class Wisdom {
    public int id;
    public String wisdom;
    public String author;

    @Override
    public String toString() {
        return "Wisdom{" +
                "id=" + id +
                ", wisdom='" + wisdom + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
