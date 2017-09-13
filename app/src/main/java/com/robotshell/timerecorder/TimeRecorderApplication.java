package com.robotshell.timerecorder;

import android.app.Application;

import com.robotshell.timerecorder.data.ContributionDataManager;
import com.tencent.bugly.Bugly;

import cn.bmob.v3.Bmob;

/**
 * Created by zhaojie on 17-9-10.
 */

public class TimeRecorderApplication extends Application {
    private static final String BOMB_APPID = "7cdf15a8ee01c424e09c57e37218339e";
    private static final String BUGLY_APPID = "13f5c71b09";
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, BOMB_APPID);
        Bugly.init(getApplicationContext(), BUGLY_APPID, false);
        ContributionDataManager.getInstance().init(this);
    }

}
