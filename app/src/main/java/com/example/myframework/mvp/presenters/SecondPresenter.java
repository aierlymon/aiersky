package com.example.myframework.mvp.presenters;

import com.example.baselib.base.BaseMvpActivity;
import com.example.baselib.mvp.BasePresenter;
import com.example.myframework.mvp.views.SecondView;

public class SecondPresenter extends BasePresenter<SecondView> {
    @Override
    protected boolean isUseEventBus() {
        return false;
    }


}
