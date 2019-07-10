package com.example.myframework.mvp.presenters;

import com.example.myframework.http.HttpMethod;
import com.example.myframework.http.bean.TestBean;
import com.example.myframework.http.myrxsubcribe.MySubscriber;
import com.example.baselib.mvp.BasePresenter;
import com.example.myframework.mvp.models.DataViewModel;
import com.example.myframework.mvp.models.TitleModel;
import com.example.myframework.mvp.views.FirstFragView;
import com.example.myframework.ui.adapter.base.BaseMulDataModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
public class FirstFraPresenter extends BasePresenter<FirstFragView> {
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
        //Http
        HttpMethod.getInstance().getCityWeather("101190201")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<TestBean>(this) {
                    @Override
                    public void onSuccess(TestBean testBean) {

                    }

                    @Override
                    public void onFail(Throwable e) {
                        //这个是测试，在公司虚拟机不能联网
                        List<BaseMulDataModel> list = new ArrayList<>();
                        for (int i = 0; i < 20; i++) {
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

                    @Override
                    public void onCompleted() {
                        getView().hideLoading();
                    }
                });
    }
}
