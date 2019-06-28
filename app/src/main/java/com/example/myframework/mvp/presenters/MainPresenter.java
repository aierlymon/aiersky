package com.example.myframework.mvp.presenters;

import com.example.baselib.http.HttpMethod;
import com.example.baselib.http.bean.TestBean;
import com.example.baselib.http.myrxsubcribe.MySubscriber;
import com.example.baselib.mvp.BasePresenter;
import com.example.baselib.utils.MyLog;
import com.example.myframework.mvp.views.MainView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<MainView> {
    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    public void success(String s) {

        HttpMethod.getInstance().getCityWeather("101190201")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<TestBean>(this) {

                    @Override
                    public void onSuccess(TestBean testBean) {
                        MyLog.i("testBean："+testBean+"  Thread: "+Thread.currentThread().getName());
                        getView().responseNet(testBean.getWeatherinfo().getCity());
                    }

                    @Override
                    public void onFail(Throwable e) {

                    }

                    @Override
                    public void onCompleted() {

                    }
                });

    }


}
