package com.example.myframework.ui;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.baselib.base.BaseMvpTitleActivity;
import com.example.baselib.utils.MyLog;
import com.example.myframework.R;
import com.example.myframework.mvp.presenters.MainPresenter;
import com.example.myframework.mvp.views.MainView;
import com.example.mytcpandws.broadcast.NetWorkStateBroadcast;
import com.example.mytcpandws.params.TCPParams;
import com.example.mytcpandws.params.WSParams;
import com.example.mytcpandws.tcpconnect.ConnectThread;
import com.example.mytcpandws.ws.client.WebSocketThread;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseMvpTitleActivity<MainView, MainPresenter> implements MainView {


    @BindView(R.id.textview1)
    TextView tx;

    @BindView(R.id.send_data)
    TextView send_data;

    @BindView(R.id.receive_data)
    TextView receive_data;

    private MyHandler myHandler;
    private ConnectThread<MyHandler> connectThread;
    private NetWorkStateBroadcast netWorkStateBroadcast;
    private WSHandler mWSHandler;
    private WebSocketThread<WSHandler> myHandlerWebSocketThread;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void initView() {
        //状态就是看到wifi图标的那一个细栏
        //setStatusBarColor
        setStatusBarColor(R.color.design_default_color_primary);
        super.initView();
        setTitle("hello");
        setTitleColor(R.color.colorAccent);

        //绑定黄油刀
        ButterKnife.bind(this);
        tx.setText("你好");

        myHandler = new MyHandler(this);
        mWSHandler = new WSHandler(this);
        registe();
    }

    private void registe() {
        netWorkStateBroadcast = new NetWorkStateBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateBroadcast, filter);
    }

    @Override
    protected void startRequest() {
        //开始tcp连接

        connectThread = new ConnectThread("192.168.43.104", 8083, myHandler);
       // connectThread.start();

        myHandlerWebSocketThread  = new WebSocketThread<>("121.40.165.18", 8800, mWSHandler);
        myHandlerWebSocketThread.start();
    }


    @Override
    protected int getBodyLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean hasBackHome() {
        return true;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    public void showError(String msg) {
        showToast("出错误了： " + msg);
    }

    @Override
    public void responseNet(String a) {
        showToast(a);
    }

    @OnClick({R.id.btn, R.id.tcp_btn, R.id.ws_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                mPresenter.success("Hello World");
                break;
            case R.id.tcp_btn:
                connectThread.send("Hello Java");
                send_data.setText("Hello Java");//这个意思一下= =
                break;
            case R.id.ws_btn:
                myHandlerWebSocketThread.send("Hello C");
                send_data.setText("Hello C");//这个意思一下= =
                break;
        }

    }

    //这个是tcp的handler
    class MyHandler extends Handler {
        private WeakReference<MainActivity> mWeakReference;

        public MyHandler(MainActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity mainActivity = mWeakReference.get();
            if (mainActivity == null) {
                return;
            }
            switch (msg.what) {
                case TCPParams.TCP_HANDLE_CONNECT_ERROR:
                    MyLog.i("MainActivity的接受: TCP_HANDLE_CONNECT_ERROR");
                    break;
                case TCPParams.TCP_HANDLE_CONNECT_BREAK:
                    MyLog.i("MainActivity的接受: TCP_HANDLE_CONNECT_BREAK");
                    break;
                case TCPParams.TCP_HANDLE_CONNECT_SUCCESS:
                    MyLog.i("MainActivity的接受: TCP_HANDLE_CONNECT_SUCCESS");
                    break;
                case TCPParams.TCP_HANDLE_RECEIVE:
                    MyLog.i("MainActivity的接受: TCP_HANDLE_RECEIVE");
                    Bundle bundle = msg.getData();
                    String receive = bundle.getString(TCPParams.RCEV);
                    receive_data.setText("接收的数据是: " + receive);
                    break;
            }
        }
    }

    //这个式ws的handler
    class WSHandler extends Handler {
        private WeakReference<MainActivity> mWeakReference;

        public WSHandler(MainActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity mainActivity = mWeakReference.get();
            if (mainActivity == null) {
                return;
            }
            switch (msg.what) {
                case WSParams.WS_HANDLE_CONNECT_SUCCESS:
                    MyLog.i("MainActivity的接受: WS_HANDLE_CONNECT_SUCCESS");
                    break;
                case WSParams.WS_HANDLE_CONNECT_BREAK:
                    MyLog.i("MainActivity的接受: WS_HANDLE_CONNECT_BREAK");
                    break;
                case WSParams.WS_HANDLE_RECEIVE:
                    MyLog.i("MainActivity的接受: WS_HANDLE_RECEIVE");
                    Bundle bundle = msg.getData();
                    String receive = bundle.getString(WSParams.RCEV);
                    receive_data.setText("接收的数据是: " + receive);
                    break;
                case WSParams.WS_HANDLE_ERROR:
                    MyLog.i("MainActivity的接受: WS_HANDLE_ERROR");
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkStateBroadcast);
    }
}
