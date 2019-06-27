package com.example.myframework.mvp.presenters;

import com.example.baselib.mvp.BasePresenter;
import com.example.myframework.mvp.views.TestFraView;

/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
public class TestFraPresenter extends BasePresenter<TestFraView> {
    @Override
    protected boolean isUseEventBus() {
        return false;
    }
}
