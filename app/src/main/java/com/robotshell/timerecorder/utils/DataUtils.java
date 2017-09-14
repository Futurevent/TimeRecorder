package com.robotshell.timerecorder.utils;


import com.robotshell.timerecorder.data.DataConstants;

import java.util.Calendar;

public class DataUtils {
    public static int getCurMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getCurSeason() {
        int month = getCurMonth();
        if (month >= 1 && month <= 3) {
            return DataConstants.SEASON_SPRING;
        } else if (month >= 4 && month <= 6) {
            return DataConstants.SEASON_SUMMER;
        } else if (month >= 7 && month <= 9) {
            return DataConstants.SEASON_AUTUMN;
        } else if (month >= 10 && month <= 12) {
            return DataConstants.SEASON_WINTER;
        } else {
            return DataConstants.SEASON_WINTER;
        }
    }

    public static int getCurDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static long getTodayMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long todayMillis = cal.getTimeInMillis();
        return todayMillis;
    }
}
