package com.developer.ashwoolford.moviesfreak;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.developer.ashwoolford.moviesfreak.Fragment3.TopratedMoviesFragment;
import com.developer.ashwoolford.moviesfreak.first_fragment.Fragment1;
import com.developer.ashwoolford.moviesfreak.second_fragment.Fragment2;

/**
 * Created by ashwoolford on 11/11/2016.
 */
public class CustomAdapter extends FragmentPagerAdapter {
    private String fragments [] ={"Now Playing","Search","Top Rated"};
    public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment1();
            case 1:
                return new Fragment2();
            case 2:
                return new TopratedMoviesFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments[position];
    }
}
