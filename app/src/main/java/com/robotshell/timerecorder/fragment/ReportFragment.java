package com.robotshell.timerecorder.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robotshell.timerecorder.R;
import com.robotshell.timerecorder.view.CircleProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportFragment extends BaseFragment {
    private final static String PREFERENCE_NAME = "chronometer_persist";

    @BindView(R.id.day_progress)
    protected CircleProgressBar dayProgress;

    private int progress = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Integer.MAX_VALUE) {
                progress++;
                if (progress >= 80) {
                    progress = 80;
                    dayProgress.setProgress(progress);
                    removeCallbacksAndMessages(null);
                } else {
                    dayProgress.setProgress(progress);
                    sendEmptyMessageDelayed(Integer.MAX_VALUE, 200);
                }
            }
            super.handleMessage(msg);
        }
    };

    public ReportFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time_report, container, false);
        ButterKnife.bind(this, rootView);

        mHandler.sendEmptyMessageDelayed(Integer.MAX_VALUE, 200);

        return rootView;
    }

    @Override
    public void refreshFragment() {
    }
}