package com.robotshell.timerecorder.bean;

import java.io.Serializable;

/**
 * Created by zhaojie on 17-9-11.
 */

public class Contribution implements Serializable {
    public static final int TYPE_LIFE = 1;

    public int Type;
    public long time;

    public Contribution() {
    }

    public Contribution(int type, long time) {
        Type = type;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Contribution{" +
                "Type=" + Type +
                ", time=" + time +
                '}';
    }
}
