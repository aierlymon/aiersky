package com.example.myframework.mvp.presenters;

import com.example.baselib.http.HttpMethod;
import com.example.baselib.http.bean.TestBean;
import com.example.baselib.mvp.BasePresenter;
import com.example.baselib.utils.MyLog;
import com.example.myframework.mvp.views.MainView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
                .subscribe(new Observer<TestBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        MyLog.i("拿到了任务: "+Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(TestBean testBeanHttpResult) {
                        MyLog.i("完成了任务: "+Thread.currentThread().getName()+" bean: "+testBeanHttpResult.getWeatherinfo().toString());
                        getView().showMainMsg(testBeanHttpResult.getWeatherinfo().toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        MyLog.i("只要没有正确拿到数据都是都会返回错误了这里: "+e.getMessage());
                        getView().showError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        MyLog.i("结束了请求");
                    }
                });

    }
}
