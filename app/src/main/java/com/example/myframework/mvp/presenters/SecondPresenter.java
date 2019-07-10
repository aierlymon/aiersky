package com.example.myframework.mvp.presenters;

import com.example.myframework.http.HttpMethod;
import com.example.myframework.http.bean.TestBean;
import com.example.myframework.http.myrxsubcribe.MySubscriber;
import com.example.baselib.mvp.BasePresenter;
import com.example.myframework.mvp.views.SecondView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SecondPresenter extends BasePresenter<SecondView> {
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
                        getView().refreshUiForActivity();
                    }

                    @Override
                    public void onFail(Throwable e) {
                        //这个是测试，在公司虚拟机不能联网
                        getView().hideLoading();
                    }

                    @Override
                    public void onCompleted() {
                        getView().hideLoading();
                    }
                });
        getView().hideLoading();
    }
}
