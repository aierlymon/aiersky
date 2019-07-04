package com.example.myframework.mvp.presenters;

import com.example.baselib.http.HttpMethod;
import com.example.baselib.http.bean.TestBean;
import com.example.baselib.http.myrxsubcribe.MySubscriber;
import com.example.baselib.mvp.BasePresenter;
import com.example.baselib.utils.MyLog;
import com.example.myframework.mvp.views.MainView;
import com.example.mytcpandws.tcpconnect.ConnectThread;
import com.example.mytcpandws.tcpconnect.ConnectUntil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<MainView> {
    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    //请求http连接
    public void success(String s) {
        HttpMethod.getInstance().getCityWeather("101190201")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<TestBean>(this) {

                    @Override
                    public void onSuccess(TestBean testBean) {
                        MyLog.i("testBean：" + testBean + "  Thread: " + Thread.currentThread().getName());
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

    private ConnectThread connectThread;

    //开始tcp客户端
    public ConnectThread startClient() {
        if (connectThread != null) {
            if (connectThread.isActive()) {
                return connectThread;
            } else {
                connectThread.closeTcpClient();
                connectThread = null;
            }
        }
        connectThread = new ConnectThread("192.168.1.101", 8085, new ConnectThread.OnConnectStateChangeListener() {
            @Override
            public void onConnect(ConnectUntil connectUntil) {

                int state = connectUntil.send("你好".getBytes());
                MyLog.i("连接上了: " + connectUntil + "  state：  " + state + "  thread name: " + Thread.currentThread().getName());
            }

            @Override
            public void onDisConnect(ConnectUntil connectUntil) {
                //连接主动断开，再次启动连接
                MyLog.i("连接主动断开: " + connectUntil + "  thread name: " + Thread.currentThread().getName());
                startClient();
                //connectUntil.start();
            }

            @Override
            public void onConnectFail(ConnectUntil connectUntil) {
                MyLog.i("连接tcp失败，找不到主机,再次连接： " + "  thread name: " + Thread.currentThread().getName());
                connectUntil.restart();
            }

            @Override
            public void onReceive(String msg) {
                MyLog.i("msg: "+msg);
            }

            @Override
            public void onProgressLog(String log) {
                MyLog.i("发送流程: " + log);
            }
        });
        connectThread.start();
        return connectThread;
    }


}
