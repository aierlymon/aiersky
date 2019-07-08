package com.example.myframework.mvp.presenters;

import com.example.baselib.http.HttpMethod;
import com.example.baselib.http.bean.TestBean;
import com.example.baselib.http.myrxsubcribe.MySubscriber;
import com.example.baselib.mvp.BasePresenter;
import com.example.myframework.mvp.views.FirstFragView;
import com.example.myframework.mvp.views.TestFraView;

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
        getView().showLoading();
        //Http
        HttpMethod.getInstance().getCityWeather("101190201")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<TestBean>(this) {
                    @Override
                    public void onSuccess(TestBean testBean) {
                        List<TestBean> list=new ArrayList<>();
                        for(int i=0;i<10;i++){
                            list.add(testBean);
                        }
                        getView().refreshUi(list);
                    }

                    @Override
                    public void onFail(Throwable e) {

                    }

                    @Override
                    public void onCompleted() {
                        getView().hideLoading();
                    }
                });
    }
}
