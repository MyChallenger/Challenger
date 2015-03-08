package com.example.vinutha.challenger.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.vinutha.challenger.Fragment.OpenChallengesFragment;
import com.example.vinutha.challenger.Fragment.PlaceHolderFragment;

/**
 * Created by devadutta on 3/7/2015.
 */
public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Challenges", "Videos" };
    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
         return  OpenChallengesFragment.newInstance(position);
        else
            return PlaceHolderFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
