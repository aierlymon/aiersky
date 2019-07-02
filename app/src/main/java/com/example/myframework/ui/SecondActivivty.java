package com.example.myframework.ui;

import com.example.baselib.base.BaseMvpActivity;
import com.example.myframework.R;
import com.example.myframework.mvp.presenters.SecondPresenter;
import com.example.myframework.mvp.views.SecondView;

public class SecondActivivty extends BaseMvpActivity<SecondView, SecondPresenter> implements SecondView{


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void startRequest() {
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

    @Override
    protected SecondPresenter createPresenter() {
        return new SecondPresenter();
    }
}
