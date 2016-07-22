package com.example.android.ireadalot.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.ireadalot.fragment.BookFragment;
import com.example.android.ireadalot.fragment.MyShelfFragment;

/**
 * Created by gjezzi on 22/07/16.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    public PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return BookFragment.newInstance();
            case 1:
                return MyShelfFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return NUM_ITEMS;
    }
}
