package com.serien.android.androidserienprojekt.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Igor on 07.09.2015.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

    List<Fragment> listFragments;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> listFrag) {
        super(fm);
        this.listFragments = listFrag;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }
}
