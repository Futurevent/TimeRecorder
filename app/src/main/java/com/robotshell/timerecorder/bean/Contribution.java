package com.robotshell.timerecorder.bean;

import java.io.Serializable;

/**
 * Created by zhaojie on 17-9-11.
 */

public class Contribution implements Serializable {
    public static final int TYPE_LIFE = 1;

    public int type;
    public long startTime;
    public long duration;

    public Contribution() {
    }

    public Contribution(int type, long startTime, long duration) {
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Contribution{" +
                "type=" + type +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
