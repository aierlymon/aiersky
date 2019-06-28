package com.example.myframework.ui.fragment;

import com.example.baselib.base.BaseMVPFragment;
import com.example.myframework.R;
import com.example.myframework.mvp.presenters.TestFraPresenter;
import com.example.myframework.mvp.views.TestFraView;

/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
public class TestFragment extends BaseMVPFragment<TestFraView, TestFraPresenter> implements TestFraView {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public boolean isUseEventBus() {
        return true;
    }

    @Override
    public void init() {
        super.init();
    }



    @Override
    protected TestFraPresenter createPresenter() {
        return new TestFraPresenter();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    public void showError(String msg) {

    }
}
