package com.robotshell.timerecorder.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robotshell.timerecorder.R;
import com.robotshell.timerecorder.data.ContributionDataManager;
import com.robotshell.timerecorder.view.CircleButton;

public class PunchFragment extends BaseFragment {
    private CircleButton punchButton;
    private ContributionDataManager contributionDataManager;

    public PunchFragment() {
        contributionDataManager = ContributionDataManager.getInstance();
    }

    public static PunchFragment newInstance() {
        PunchFragment fragment = new PunchFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time_punch, container, false);
        punchButton = (CircleButton) rootView.findViewById(R.id.punch);
        punchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                punchOnce();
            }
        });
        return rootView;
    }

    private void punchOnce() {
        contributionDataManager.punchOnce();
    }

    @Override
    public void refreshFragment() {
    }
}