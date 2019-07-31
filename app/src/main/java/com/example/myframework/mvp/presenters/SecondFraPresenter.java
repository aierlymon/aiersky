package com.example.myframework.mvp.presenters;

import android.content.Context;

import com.example.baselib.mvp.BasePresenter;
import com.example.myframework.mvp.views.SecondFragView;

/**
 * createBy ${huanghao}
 * on 2019/7/9
 */
public class SecondFraPresenter extends BasePresenter<SecondFragView> {

    @Override
    public void showError(String msg) {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }


}
