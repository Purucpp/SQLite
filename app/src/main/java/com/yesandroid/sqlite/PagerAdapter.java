package com.yesandroid.sqlite;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.yesandroid.sqlite.ui.dashboard.Test_first;

public class PagerAdapter extends FragmentPagerAdapter {
    private int tabsNumber;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int tabs) {


        super(fm, behavior);
        this.tabsNumber = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Test_first();
            case 1:
                return new Test_first();

            default: return new Test_first();


        }
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }
}
