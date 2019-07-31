package com.example.myframework.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.baselib.base.BaseMVPFragment;

import java.util.List;

/**
 * createBy ${huanghao}
 * on 2019/7/8
 */
public class ActSecPagerAdapter extends FragmentPagerAdapter {

    private List<BaseMVPFragment> fragmentList;
    public ActSecPagerAdapter(FragmentManager fm, List<BaseMVPFragment> fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
