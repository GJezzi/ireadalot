package com.example.android.ireadalot.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.example.android.ireadalot.R;
import com.example.android.ireadalot.fragment.BookFragment;
import com.example.android.ireadalot.fragment.MyShelfFragment;

/**
 * Created by gjezzi on 22/07/16.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    private Context mContext;

    public PagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = BookFragment.newInstance();
                break;
            case 1:
                fragment = MyShelfFragment.newInstance();
                break;
            default:
                fragment = BookFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount(){
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position){

        switch (position) {
            case 0:
                return mContext.getString(R.string.pager_title_books_shelf);
            case 1:
            default:
                return mContext.getString(R.string.pager_title_my_shelf);
        }
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object object){
        FragmentManager fragmentManager = ((Fragment) object).getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove((Fragment)object);
        transaction.commit();
        super.destroyItem(viewGroup, position, object);
    }
}
