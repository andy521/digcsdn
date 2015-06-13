package com.bob.digcsdn.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bob.digcsdn.fragment.BlogFragment;

/**
 * Created by bob on 15-5-10.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    public static final String[] TITLE = new String[] { "首页", "生活杂谈", "Android启蒙",
            "Java进阶", "数据结构与算法", "Windows", "Mac", "Linux", "考研之计科", "走进摄影","日语学习" };
    public TabPagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: break;
            default:break;
        }
        return new BlogFragment(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position % TITLE.length].toUpperCase();
    }

    @Override
    public int getCount() {//获取数据集大小
        return TITLE.length;
    }
}
