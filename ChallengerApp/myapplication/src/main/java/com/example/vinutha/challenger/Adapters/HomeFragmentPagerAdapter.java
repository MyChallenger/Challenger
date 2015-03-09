package com.example.vinutha.challenger.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.vinutha.challenger.Fragment.CompletedChallengesFragment;
import com.example.vinutha.challenger.Fragment.OpenChallengesFragment;
import com.example.vinutha.challenger.R;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Open Challenges", "Completed Challenges" };
    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
         return  OpenChallengesFragment.newInstance(position);
        else
            return  new CompletedChallengesFragment();
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
