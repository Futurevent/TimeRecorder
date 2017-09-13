package com.robotshell.timerecorder.data;


import android.content.Context;

import com.robotshell.timerecorder.R;
import com.robotshell.timerecorder.bean.SeasonRecord;

import java.util.ArrayList;

public class SeasonRecordSet {
    public Context context;

    private ArrayList<SeasonRecord> seasonRecords = new ArrayList<>();

    public SeasonRecordSet(Context contxt) {
        this.context = contxt;
    }

    public void buildRecords() {
        seasonRecords.add(new SeasonRecord("bg_spring.png", context.getString(R.string.Spring), DataConstants.SEASON_SPRING));
        seasonRecords.add(new SeasonRecord("bg_summer.png", context.getString(R.string.Summer), DataConstants.SEASON_SUMMER));
        seasonRecords.add(new SeasonRecord("bg_autumn.png", context.getString(R.string.Autumn), DataConstants.SEASON_AUTUMN));
        seasonRecords.add(new SeasonRecord("bg_winter.png", context.getString(R.string.Winter), DataConstants.SEASON_WINTER));
    }

    public ArrayList<SeasonRecord> getSeasonRecords() {
        return seasonRecords;
    }
}
