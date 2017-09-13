package com.robotshell.timerecorder;

import android.app.Application;

import com.robotshell.timerecorder.data.ContributionDataManager;

import cn.bmob.v3.Bmob;

/**
 * Created by zhaojie on 17-9-10.
 */

public class TimeRecorderApplication extends Application {
    private static final String BOMB_APPID = "7cdf15a8ee01c424e09c57e37218339e";

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, BOMB_APPID);

        ContributionDataManager.getInstance().init(this);
    }

}
