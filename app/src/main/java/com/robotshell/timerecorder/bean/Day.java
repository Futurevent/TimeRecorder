package com.robotshell.timerecorder.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/1/13.
 * 封装每天的属性，方便在绘制的时候进行计算
 */

public class Day implements Serializable {
    public String time;
    /**
     * 年
     **/
    public int year;
    /**
     * 月
     **/
    public int month;
    /**
     * 日
     **/
    public int date;
    /**
     * 周几
     **/
    public int week;
    /**
     * 贡献次数，默认0
     **/
    public int contributionCount = 0;

    public ArrayList<Contribution> contributions = new ArrayList<>();
    /**
     * 默认颜色,根据提交次数改变
     **/
    public int colour = 0xFFDDDDDD;

    /**
     * 方格坐标，左上点，右下点，确定矩形范围
     **/
    public float startX;
    public float startY;
    public float endX;
    public float endY;

    public void addContribution(Contribution contribution) {
        contributions.add(contribution);
    }

    @Override
    public String toString() {
        //这里直接在弹出框中显示
        return "" + year + "年" + month + "月" + date + "日周" + week + "," + contributionCount + "次";
    }
}
