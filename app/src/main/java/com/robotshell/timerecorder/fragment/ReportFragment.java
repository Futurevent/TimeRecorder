package com.robotshell.timerecorder.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robotshell.timerecorder.R;
import com.robotshell.timerecorder.data.ContributionDataManager;
import com.robotshell.timerecorder.view.CircleProgressBar;
import com.robotshell.timerecorder.view.DayBar;
import com.robotshell.timerecorder.view.FadeinTextViewHelper;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportFragment extends BaseFragment {
    private final static String PREFERENCE_NAME = "chronometer_persist";

    @BindView(R.id.day_progress)
    protected CircleProgressBar dayProgress;
    @BindView(R.id.today_bar)
    protected DayBar todayBar;
    @BindView(R.id.report_content)
    protected TextView reportContent;
    private FadeinTextViewHelper fadeinTextViewHelper;

    private int pastDayProgress = 0;

    private int progress = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Integer.MAX_VALUE) {
                progress++;
                if (progress >= pastDayProgress) {
                    progress = pastDayProgress;
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

        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "font/font.TTF");
        reportContent.setTypeface(typeFace);
        fadeinTextViewHelper = new FadeinTextViewHelper(reportContent,
                ContributionDataManager.getInstance().getReprotContent(), 200);
        fadeinTextViewHelper.startTv(1);
        return rootView;
    }

    @Override
    public void refreshFragment() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        pastDayProgress = day * 100 / 365;
        progress = 0;
        mHandler.sendEmptyMessageDelayed(Integer.MAX_VALUE, 200);

        todayBar.invalidate();

        reportContent.setText("");
        fadeinTextViewHelper.setText(ContributionDataManager.getInstance().getReprotContent());
        fadeinTextViewHelper.setText("");
        fadeinTextViewHelper.startTv(1);
    }
}