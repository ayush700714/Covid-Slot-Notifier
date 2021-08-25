package com.example.covidnotifier.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.covidnotifier.fragments.SlideOne;
import com.example.covidnotifier.fragments.SlideThree;
import com.example.covidnotifier.fragments.SlideTwo;


public class WalkThroughPagerAdapter extends FragmentStatePagerAdapter {
    public WalkThroughPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SlideOne tab0 = new SlideOne();
                return tab0;
            case 1:
                SlideTwo tab1 = new SlideTwo();
                return tab1;
            case 2:
                SlideThree tab2 = new SlideThree();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}