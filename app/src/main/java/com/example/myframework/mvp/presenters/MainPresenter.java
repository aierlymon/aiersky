package com.example.myframework.mvp.presenters;

import com.example.baselib.mvp.BasePresenter;
import com.example.myframework.mvp.views.MainView;

public class MainPresenter extends BasePresenter<MainView>{
    @Override
    protected boolean isUseEventBus() {
        return false;
    }
    
    public void  success(String s){
        getView().showMainMsg(s);
    }
}
