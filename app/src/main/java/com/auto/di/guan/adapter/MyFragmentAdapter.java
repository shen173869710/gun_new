package com.auto.di.guan.adapter;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    public MyFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list){
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }
    @Override
    public int getCount() {
        return list.size();
    }
}
