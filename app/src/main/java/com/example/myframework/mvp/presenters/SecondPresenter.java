package com.example.myframework.mvp.presenters;

import com.example.baselib.mvp.BasePresenter;
import com.example.myframework.mvp.views.SecondView;

public class SecondPresenter extends BasePresenter<SecondView> {


    @Override
    public void showError(String msg) {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    public void requestHttp() {
        getView().showLoading();
    }
}
