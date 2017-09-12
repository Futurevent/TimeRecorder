package com.robotshell.timerecorder.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robotshell.timerecorder.R;
import com.robotshell.timerecorder.data.PhotoAdapter;
import com.robotshell.timerecorder.view.ContributionView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordFragment extends BaseFragment {
    private static final String TAG = "RecordFragment";

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.contribution_record_year)
    protected ContributionView contributionView;

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
        ButterKnife.bind(this, rootView);

        String[] dataSet = null;
        try {
            dataSet = getActivity().getAssets().list("demo-pictures");
        } catch (IOException e) {
            e.printStackTrace();
        }
        PhotoAdapter adapter = new PhotoAdapter(dataSet, getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
            }
        });
        mRecyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void refreshFragment() {
        if (contributionView != null) {
            contributionView.refreshView();
        }
    }
}