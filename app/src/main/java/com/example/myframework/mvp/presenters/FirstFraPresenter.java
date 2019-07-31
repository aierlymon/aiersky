package com.example.myframework.mvp.presenters;

import com.example.baselib.mvp.BasePresenter;
import com.example.myframework.mvp.models.DataViewModel;
import com.example.myframework.mvp.models.TitleModel;
import com.example.myframework.mvp.views.FirstFragView;
import com.example.myframework.ui.adapter.base.BaseMulDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
public class FirstFraPresenter extends BasePresenter<FirstFragView> {


    @Override
    public void showError(String msg) {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }



    public void requestHttp() {
        List<BaseMulDataModel> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                DataViewModel dataViewModel = new DataViewModel(5, 10, "公仔" + i, i);
                list.add(dataViewModel);
            } else {
                TitleModel titleModel = new TitleModel("标题" + i, 1);
                list.add(titleModel);
            }
        }
        getView().refreshUi(list);
    }
}
