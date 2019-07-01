package com.example.mytcpandws.tcpconnect;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.mytcpandws.params.TCPParams;
import com.example.mytcpandws.utils.MyLog;

import java.io.UnsupportedEncodingException;

import static com.example.mytcpandws.params.TCPParams.RCEV;
import static com.example.mytcpandws.params.TCPParams.TCP_HANDLE_CONNECTING;
import static com.example.mytcpandws.params.TCPParams.TCP_HANDLE_CONNECT_BREAK;
import static com.example.mytcpandws.params.TCPParams.TCP_HANDLE_CONNECT_SUCCESS;
import static com.example.mytcpandws.params.TCPParams.TCP_HANDLE_RECEIVE;


/**
 * Created by huanghao on 2018/7/24.
 */

public class ConnectThread<T extends Handler> extends Thread implements ConnectUntil.ReciveMsgListener {

    private ConnectHandler mConnectHandler;
    private String ip;
    private int port;
    private ConnectUntil mConnectUntil;
    private T mainHandler;

    public ConnectThread(String ip, int port, T mMainHandler) {
        this.ip = ip;
        this.port = port;
        this.mainHandler = mMainHandler;
    }

    public void send(long id, String msg) {
        Message message = Message.obtain();
        message.what = ConnectHandler.SEND;
        Bundle bundle = new Bundle();
        bundle.putString("msg", msg);
        message.setData(bundle);
        if (mConnectHandler != null)
            mConnectHandler.sendMessage(message);
    }

    @Override
    public void run() {
        super.run();
        //正在连接网络
        if (mainHandler != null)
            mainHandler.sendEmptyMessage(TCP_HANDLE_CONNECTING);
        mConnectUntil = new ConnectUntil(ip, port);
        mConnectUntil.setReciveMsgListener(this);
        mConnectUntil.start();
        //网络连接完成
        if (mainHandler != null)
            mainHandler.sendEmptyMessage(1);
        Looper.prepare();
        mConnectHandler = new ConnectHandler();
        Looper.loop();
    }

    @Override
    public void onRecive(ConnectUntil connectUntil, byte[] msg) {
        MyLog.i("数据为： " + new String(msg, 0, msg.length));
        String info = new String(msg, 0, msg.length);

        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString(RCEV, info);
        message.setData(bundle);
        message.what = TCP_HANDLE_RECEIVE;
        mainHandler.sendMessage(message);

    }

    @Override
    public void onConnect(ConnectUntil connectUntil) {
        MyLog.i("onConnect: 连接成功: ");
        mainHandler.sendEmptyMessage(TCP_HANDLE_CONNECT_SUCCESS);
    }

    @Override
    public void onDisConnect(ConnectUntil connectUntil) {
        mainHandler.sendEmptyMessage(TCP_HANDLE_CONNECT_BREAK);//连接断开
        mConnectHandler.sendEmptyMessage(ConnectHandler.CLOSE);
    }

    @Override
    public void onConnectFail(ConnectUntil connectUntil) {
        MyLog.i("onDisConnect: 连接失败");
        if (TCPParams.isNetWork.get()) {
            //在网络正常的时候才试图重连
            connectUntil.restart();
        } else {
            connectUntil.close();
        }
    }

    public void close() {
        mConnectHandler.sendEmptyMessage(ConnectHandler.CLOSE);
    }

    class ConnectHandler extends Handler {
        public static final int SEND = 0x01;//发送指令
        public static final int CLOSE = 0x02;//发送指令

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SEND:
                    String info = msg.getData().getString("msg");
                    if (mConnectUntil != null && mConnectUntil.isConnect())
                        try {
                            mConnectUntil.send(info.getBytes("GBK"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    break;
                case CLOSE:

                    mConnectHandler.removeCallbacksAndMessages(null);
                    mainHandler = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        Looper.myLooper().quitSafely();
                    } else {
                        Looper.myLooper().quit();
                    }
                    mConnectUntil.close();
                    break;
            }
        }
    }
}
