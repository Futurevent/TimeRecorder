package com.robotshell.timerecorder.data;

import android.content.Context;

import com.robotshell.timerecorder.bean.Day;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhaojie on 17-9-10.
 */

public class ContributionDataManager {
    private static final String DB_NAME = "days";
    private static final String TIME_FORMAT = "yyyy-MM-dd";

    private DB daysDB;
    private Context context;
    private SimpleDateFormat dataFormat;

    public static class Holder {
        public static final ContributionDataManager gInstance = new ContributionDataManager();
    }

    private ContributionDataManager() {
        dataFormat = new SimpleDateFormat(TIME_FORMAT);
    }

    public static ContributionDataManager getInstance() {
        return Holder.gInstance;
    }

    public void init(Context context) {
        this.context = context;
        try {
            daysDB = DBFactory.open(context, DB_NAME);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public void punchOnce() {
        String time = dataFormat.format(new Date());
        Day day;
        try {
            if (daysDB.exists(time)) {
                day = daysDB.getObject(time, Day.class);
                day.contribution++;
            } else {
                day = new Day();
                day.time = time;
                day.contribution++;
            }
            daysDB.put(time, day);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public int getContributionValue(String time) {
        Day day = null;
        try {
            if (daysDB.exists(time)) {
                day = daysDB.getObject(time, Day.class);
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        if (day != null) {
            return day.contribution;
        } else {
            return 0;
        }
    }
}
