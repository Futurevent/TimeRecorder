package com.robotshell.timerecorder.data;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.robotshell.timerecorder.R;
import com.robotshell.timerecorder.fragment.BaseFragment;
import com.robotshell.timerecorder.fragment.PunchFragment;
import com.robotshell.timerecorder.fragment.RecordFragment;

import java.util.HashMap;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private HashMap<Integer, BaseFragment> fragments = new HashMap<>();

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = fragments.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = RecordFragment.newInstance();
                    break;
                case 1:
                    fragment = PunchFragment.newInstance();
                    break;
                case 2:
                    fragment = PunchFragment.newInstance();
                    break;
            }
            fragments.put(position, fragment);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.page_title_record);
            case 1:
                return context.getString(R.string.page_title_punch);
            case 2:
                return context.getString(R.string.page_title_report);
        }
        return null;
    }

    public void refreshFragment(int position) {
        BaseFragment fragment = (BaseFragment) getItem(position);
        fragment.refreshFragment();
    }
}