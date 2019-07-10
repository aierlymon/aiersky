package com.example.myframework.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.baselib.base.BaseMvpTitleActivity;
import com.example.baselib.utils.LoadDialogUtil;
import com.example.baselib.utils.MyLog;
import com.example.baselib.utils.RxPermissionUtil;
import com.example.baselib.widget.CustomDialog;
import com.example.myframework.R;
import com.example.myframework.mvp.presenters.MainPresenter;
import com.example.myframework.mvp.views.MainView;
import com.example.mytcpandws.broadcast.NetWorkStateBroadcast;
import com.example.mytcpandws.params.WSParams;
import com.example.mytcpandws.tcpconnect.ConnectUntilBox;
import com.example.mytcpandws.tcpconnect.TcpClient;
import com.example.mytcpandws.ws.client.WebSocketThread;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*
* 这个activity主要验证的是TCP和WebSocket
* 的通信
* */
public class MainActivity extends BaseMvpTitleActivity<MainView, MainPresenter> implements MainView {


    private String list[]={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @BindView(R.id.textview1)
    TextView tx;

    @BindView(R.id.send_data)
    TextView send_data;

    @BindView(R.id.receive_data)
    TextView receive_data;

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
        MyLog.i("创建了");
        //setStatusBarColor
        setStatusBarColor(R.color.design_default_color_primary);
        super.initView();
        setTitle("hello");
        setTitleColor(R.color.colorAccent);

        //绑定黄油刀
        ButterKnife.bind(this);
        tx.setText("你好");

        registe();


        mWSHandler = new WSHandler(this);



        //LoadDialogUtil.getInstance(this, "正在加载", CustomDialog.Pulse).show();
        RxPermissionUtil.getInstance().permission(this,list);

        //测试单例强引用导致的内存泄漏，测试leakcanary
      /*  TestManager manager = TestManager.getInstance(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gl);
        manager.test(bitmap);*/
    }

    private void registe() {
        netWorkStateBroadcast = new NetWorkStateBroadcast();
        netWorkStateBroadcast.setmOnNetStateChangListener(new NetWorkStateBroadcast.OnNetStateChangListener() {
            @Override
            public void onNetWorkSuccess() {

                //开启ws
                myHandlerWebSocketThread = new WebSocketThread<>("192.168.6.149", 8008, mWSHandler);
                myHandlerWebSocketThread.start();


                for (int i = 8; i < 16; i++) {
                    TcpClient tcpClient = new TcpClient("192.168.80." + i, 8085, true, true);
                   // tcpClient.connect();
                }
                tcpClient = new TcpClient("192.168.80.1", 8085, true, true);
               // tcpClient.connect();
            }

            @Override
            public void onNetWorkFail() {
                //断网要清空全部连接
                ConnectUntilBox.closeAllContainGroup(true);
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateBroadcast, filter);
    }

    private TcpClient tcpClient;


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
        LoadDialogUtil.getInstance(this, "正在加载", CustomDialog.DoubleBounce).show();
    }

    @Override
    public void hideLoading() {
        LoadDialogUtil.getInstance(this, "正在加载", CustomDialog.DoubleBounce).cancel();
    }


    @Override
    public void showError(String msg) {
        showToast("出错误了： " + msg);
    }

    @Override
    public void responseNet(String a) {
        showToast(a);
    }

    @OnClick({R.id.btn, R.id.tcp_btn, R.id.ws_btn, R.id.btn_intent,R.id.btn_third})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                mPresenter.success("Hello World");
                break;
            case R.id.tcp_btn:
                tcpClient.sendBytes("你好".getBytes());
                send_data.setText("Hello Java");//这个意思一下= =
                break;
            case R.id.ws_btn:
                myHandlerWebSocketThread.send("Hello C");
                send_data.setText("Hello C");//这个意思一下= =
                break;
            case R.id.btn_intent:
                Intent intent = new Intent(MainActivity.this, SecondActivivty.class);
                startActivity(intent);
                //  finish();
                break;
            case R.id.btn_third:
                Intent intent3 = new Intent(MainActivity.this, ThirdActivity.class);
                startActivity(intent3);
                break;
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
        //关闭tcp连接
        ConnectUntilBox.closeAllContainGroup(true);
        //取消广播注册
        unregisterReceiver(netWorkStateBroadcast);
        //关闭ws
        if (myHandlerWebSocketThread != null) {
            myHandlerWebSocketThread.close();
        }
    }
}
