package com.robotshell.timerecorder.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import com.robotshell.timerecorder.R;
import com.robotshell.timerecorder.data.ContributionDataManager;
import com.robotshell.timerecorder.view.ChronometerPersist;
import com.robotshell.timerecorder.view.CircleButton;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

public class ReportFragment extends BaseFragment {
    private final static String PREFERENCE_NAME = "chronometer_persist";

    private CircleButton punchButton;
    private Chronometer chronometer;
    private FABRevealLayout fabRevealLayout;
    private ChronometerPersist chronometerPersist;
    private SharedPreferences sharedPreferences;

    private ContributionDataManager contributionDataManager;

    public ReportFragment() {
        contributionDataManager = ContributionDataManager.getInstance();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        chronometerPersist.resumeState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time_report, container, false);
        punchButton = (CircleButton) rootView.findViewById(R.id.punch);
        punchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometerPersist.stopChronometer();
                long duration = chronometer.getBase();
                punchOnce(duration);
                punchButton.setClickable(false);
                fabRevealLayout.revealMainView();
            }
        });

        chronometer = (Chronometer) rootView.findViewById(R.id.chronometer);
        chronometerPersist = ChronometerPersist.getInstance(chronometer, sharedPreferences);
        chronometerPersist.hourFormat(true);

        fabRevealLayout = (FABRevealLayout) rootView.findViewById(R.id.fab_reveal_layout);
        fabRevealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabRevealLayout, View mainView) {
                punchButton.setClickable(true);
            }

            @Override
            public void onSecondaryViewAppeared(FABRevealLayout fabRevealLayout, View secondaryView) {
                chronometerPersist.startChronometer();
            }
        });
        return rootView;
    }

    private void punchOnce(long time) {
        contributionDataManager.punchOnce(time);
    }

    @Override
    public void refreshFragment() {
    }
}