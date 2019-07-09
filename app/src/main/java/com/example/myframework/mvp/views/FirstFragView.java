package com.example.myframework.mvp.views;

import com.example.baselib.http.bean.TestBean;
import com.example.baselib.mvp.IView;
import com.example.myframework.ui.adapter.base.BaseMulDataModel;

import java.util.List;

/**
 * createBy ${huanghao}
 * on 2019/7/8
 */
public interface FirstFragView extends IView {
    void refreshUi(List<BaseMulDataModel> beanList);
}
