package com.robotshell.timerecorder.bean;


public class SeasonRecord {
    public String album;
    public String name;
    public int season;

    public SeasonRecord() {
    }

    public SeasonRecord(String album, String name, int season) {
        this.album = album;
        this.name = name;
        this.season = season;
    }
}
