package com.robotshell.timerecorder;

import android.app.Application;

import com.robotshell.timerecorder.data.ContributionDataManager;

/**
 * Created by zhaojie on 17-9-10.
 */

public class TimeRecorderApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContributionDataManager.getInstance().init(this);
    }

}
