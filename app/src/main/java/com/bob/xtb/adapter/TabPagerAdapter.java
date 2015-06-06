package com.bob.xtb.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by bob on 15-5-10.
 */
public class TabPagerAdapter extends FragmentPagerAdapter{
    private List<Fragment>fragments;
    public TabPagerAdapter(FragmentManager fm, List<Fragment>fragments){
        super(fm);
        this.fragments= fragments;
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);//返回需要显示的控件
    }

    @Override
    public int getCount() {//获取数据集大小
        return fragments.size();
    }
}
