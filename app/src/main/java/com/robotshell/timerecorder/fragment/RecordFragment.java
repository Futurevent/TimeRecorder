package com.robotshell.timerecorder.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robotshell.timerecorder.R;
import com.robotshell.timerecorder.view.ContributionView;

public class RecordFragment extends BaseFragment {
    private ContributionView contributionView;

    public RecordFragment() {
    }

    public static RecordFragment newInstance() {
        RecordFragment fragment = new RecordFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time_record, container, false);
        contributionView = (ContributionView) rootView.findViewById(R.id.cc_chart);
        return rootView;
    }

    @Override
    public void refreshFragment() {
        if (contributionView != null) {
            contributionView.refreshView();
        }
    }
}