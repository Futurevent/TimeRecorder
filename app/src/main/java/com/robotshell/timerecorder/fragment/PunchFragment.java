package com.robotshell.timerecorder.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.robotshell.timerecorder.R;
import com.robotshell.timerecorder.bean.Wisdom;
import com.robotshell.timerecorder.data.ContributionDataManager;
import com.robotshell.timerecorder.data.WisdomDataManager;
import com.robotshell.timerecorder.view.ChronometerPersist;
import com.robotshell.timerecorder.view.CircleButton;
import com.robotshell.timerecorder.view.ContributionView;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PunchFragment extends BaseFragment {
    private final static String PREFERENCE_NAME = "chronometer_persist";

    private CircleButton punchButton;
    private Chronometer chronometer;
    private FABRevealLayout fabRevealLayout;
    private ChronometerPersist chronometerPersist;
    private SharedPreferences sharedPreferences;
    private ContributionView contributionView;

    @BindView(R.id.wisdom_text)
    protected TextView tvWisdom;
    @BindView(R.id.wisdom_author)
    protected TextView tvWisdomAuthor;

    private ContributionDataManager contributionDataManager;
    private WisdomDataManager wisdomDataManager;

    private WisdomDataManager.GetWisdomCallback wisdomCallback = new WisdomDataManager.GetWisdomCallback() {
        @Override
        public void onGetWisdom(Wisdom wisdom) {
            tvWisdom.setText("\u3000\u3000" + wisdom.wisdom);
            if (!TextUtils.isEmpty(wisdom.author)) {
                tvWisdomAuthor.setText("——" +wisdom.author);
            }
        }
    };

    public PunchFragment() {
        contributionDataManager = ContributionDataManager.getInstance();
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        wisdomDataManager = WisdomDataManager.getInstance(context);
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
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
        ButterKnife.bind(this, rootView);

        wisdomDataManager.randomWisdom(wisdomCallback);
        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "font/font.TTF");
        tvWisdom.setTypeface(typeFace);
        tvWisdomAuthor.setTypeface(typeFace);

        punchButton = (CircleButton) rootView.findViewById(R.id.punch);
        punchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long duration = SystemClock.elapsedRealtime() - chronometer.getBase();
                chronometerPersist.stopChronometer();
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