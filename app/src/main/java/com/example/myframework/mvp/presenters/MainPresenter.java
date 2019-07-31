package com.example.myframework.mvp.presenters;

import com.example.baselib.mvp.BasePresenter;
import com.example.myframework.mvp.views.MainView;

public class MainPresenter extends BasePresenter<MainView> {

    @Override
    public void showError(String msg) {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    //请求http连接
    public void success(String s) {

    }

}
