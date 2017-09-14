package com.robotshell.timerecorder.data;

import android.content.Context;

import com.robotshell.timerecorder.bean.Contribution;
import com.robotshell.timerecorder.bean.Day;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhaojie on 17-9-10.
 */

public class ContributionDataManager {
    private static final String DB_NAME = "days";
    private static final String TIME_FORMAT = "yyyy-MM-dd";
    private static final String TIME_STRING_FORMAT = "%d-%02d-%02d";

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

    public void punchOnce(long duration) {
        String time = dataFormat.format(new Date());
        Day day;

        try {
            if (daysDB.exists(time)) {
                day = daysDB.getObject(time, Day.class);
                day.contributionCount++;
            } else {
                day = new Day();
                day.time = time;
                day.contributionCount++;
            }

            if (duration != 0) {
                Contribution contribution = new Contribution(Contribution.TYPE_LIFE,
                        System.currentTimeMillis() - duration, duration);
                day.addContribution(contribution);
            }

            daysDB.put(time, day);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public void punchOnce() {
        punchOnce(0);
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
            return day.contributionCount;
        } else {
            return 0;
        }
    }

    public Day getToday() {
        Calendar cal = Calendar.getInstance();
        Day day = new Day();
        day.year = cal.get(Calendar.YEAR);
        day.month = cal.get(Calendar.MONTH) + 1;
        day.date = cal.get(Calendar.DAY_OF_MONTH);
        day.time = String.format(TIME_STRING_FORMAT, day.year, day.month, day.date);
        try {
            if (daysDB.exists(day.time)) {
                day = daysDB.getObject(day.time, Day.class);
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        return day;
    }
}
