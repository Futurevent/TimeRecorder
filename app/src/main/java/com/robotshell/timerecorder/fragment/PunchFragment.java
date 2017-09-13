package com.robotshell.timerecorder.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.Toast;

import com.robotshell.timerecorder.R;
import com.robotshell.timerecorder.bean.Wisdom;
import com.robotshell.timerecorder.data.ContributionDataManager;
import com.robotshell.timerecorder.view.ChronometerPersist;
import com.robotshell.timerecorder.view.CircleButton;
import com.robotshell.timerecorder.view.ContributionView;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PunchFragment extends BaseFragment {
    private final static String PREFERENCE_NAME = "chronometer_persist";

    private CircleButton punchButton;
    private Chronometer chronometer;
    private FABRevealLayout fabRevealLayout;
    private ChronometerPersist chronometerPersist;
    private SharedPreferences sharedPreferences;
    private ContributionView contributionView;

    private ContributionDataManager contributionDataManager;

    public PunchFragment() {
        contributionDataManager = ContributionDataManager.getInstance();
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        //查找Person表里面id为6b6c11c537的数据
        BmobQuery<Wisdom> bmobQuery = new BmobQuery<Wisdom>();
        bmobQuery.getObject("e0siJJJS", new QueryListener<Wisdom>() {
            @Override
            public void done(Wisdom object, BmobException e) {
                if (e == null) {
                    Toast.makeText(context, "查询成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "查询失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static PunchFragment newInstance() {
        PunchFragment fragment = new PunchFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        chronometerPersist.resumeState();
        if (chronometerPersist.isRunning()) {
            fabRevealLayout.revealSecondaryView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time_punch, container, false);
        punchButton = (CircleButton) rootView.findViewById(R.id.punch);
        punchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometerPersist.stopChronometer();
                long duration = chronometer.getBase();
                punchOnce(duration);
                punchButton.setClickable(false);
                fabRevealLayout.revealMainView();
                contributionView.refreshView();
            }
        });

        punchButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.cancel_confirm_title))
                        .setContentText(getString(R.string.cancel_confirm_content))
                        .setConfirmText(getString(R.string.cancel_confirm))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                chronometerPersist.stopChronometer();
                                punchButton.setClickable(false);
                                fabRevealLayout.revealMainView();
                                sDialog
                                        .setTitleText(getString(R.string.canceled))
                                        .setContentText(getString(R.string.canceled_content))
                                        .setConfirmText(getString(R.string.canceled_ok))
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .show();
                return true;
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
                chronometerPersist.resumeState();
                if (!chronometerPersist.isRunning()) {
                    chronometerPersist.startChronometer();
                }
            }
        });

        contributionView = (ContributionView) rootView.findViewById(R.id.contribution_record_punch);
        return rootView;
    }

    private void punchOnce(long time) {
        contributionDataManager.punchOnce(time);
    }

    @Override
    public void refreshFragment() {
    }
}