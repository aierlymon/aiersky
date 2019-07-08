package com.example.myframework.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ProgressBar;

import com.example.baselib.base.BaseMVPFragment;
import com.example.baselib.base.BaseMvpActivity;
import com.example.baselib.http.bean.TestBean;
import com.example.baselib.utils.LoadDialogUtil;
import com.example.baselib.widget.CustomDialog;
import com.example.myframework.R;
import com.example.myframework.mvp.presenters.SecondPresenter;
import com.example.myframework.mvp.views.SecondView;
import com.example.myframework.ui.adapter.ActSecPagerAdapter;
import com.example.myframework.ui.fragment.FirstTabFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/*
 * 这个Activity主要严重的是
 * tablayout+viewpager+fragment(AutoRecyclerView+下拉刷新+上拉加载+AppbarLayout)
 * */
public class SecondActivivty extends BaseMvpActivity<SecondView, SecondPresenter> implements SecondView {

    @BindView(R.id.head_tablayout)
    TabLayout tabLayout;

    @BindView(R.id.body_viewpager)
    ViewPager viewPager;

    private List<BaseMVPFragment> fragmentList;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_second;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        fragmentList = new ArrayList<>();
        fragmentList.add(FirstTabFragment.newInstance("第一页"));
        for(int i=0;i<fragmentList.size();i++){
            tabLayout.addTab(tabLayout.newTab().setText(fragmentList.get(i).getTitle()));
        }
        viewPager.setAdapter(new ActSecPagerAdapter(getSupportFragmentManager(), fragmentList));
    }

    @Override
    public void showLoading() {
        LoadDialogUtil.getInstance(this, "正在加载", CustomDialog.FoldingCube).show();
    }

    @Override
    public void hideLoading() {
        LoadDialogUtil.getInstance(this, "正在加载", CustomDialog.FoldingCube).cancel();
    }


    @Override
    public void showError(String msg) {

    }

    @Override
    protected SecondPresenter createPresenter() {
        return new SecondPresenter();
    }

    @Override
    public void refreshUi(List<TestBean> testBeanList) {
        //这个根据以后布局再定义
    }
}
