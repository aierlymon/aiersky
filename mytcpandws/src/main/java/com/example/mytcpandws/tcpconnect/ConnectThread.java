package com.example.mytcpandws.tcpconnect;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.example.mytcpandws.params.TCPParams;

import java.io.UnsupportedEncodingException;

import static com.example.mytcpandws.params.TCPParams.RCEV;
import static com.example.mytcpandws.params.TCPParams.SEND;
import static com.example.mytcpandws.params.TCPParams.TCP_HANDLE_CONNECT_BREAK;
import static com.example.mytcpandws.params.TCPParams.TCP_HANDLE_CONNECT_ERROR;
import static com.example.mytcpandws.params.TCPParams.TCP_HANDLE_CONNECT_SUCCESS;
import static com.example.mytcpandws.params.TCPParams.TCP_HANDLE_RECEIVE;


/**
 * Created by huanghao on 2018/7/24.
 * data 好像这个不用线程装起来，因为io.netty自带线程，不过管他呢，问题不大
 */

public class ConnectThread<T extends Handler> extends Thread implements ConnectUntil.ReciveMsgListener {

    private TcpConnectHandler mConnectHandler;
    private String ip;
    private int port;
    private ConnectUntil mConnectUntil;
    private T mainHandler;

    public ConnectThread(String ip, int port, T mMainHandler) {
        this.ip = ip;
        this.port = port;
        this.mainHandler = mMainHandler;
    }

    public void send(String msg) {
        Message message = Message.obtain();
        message.what = TcpConnectHandler.SEND;
        Bundle bundle = new Bundle();
        bundle.putString(SEND, msg);
        message.setData(bundle);
        if (mConnectHandler != null)
            mConnectHandler.sendMessage(message);
    }

    @Override
    public void run() {
        super.run();
        //正在连接网络
        mConnectUntil = new ConnectUntil(ip, port);
        mConnectUntil.setReciveMsgListener(this);
        mConnectUntil.start();
        Looper.prepare();
        mConnectHandler = new TcpConnectHandler();
        Looper.loop();
    }


    private void sendUi(int type, String msg) {
        if (mainHandler != null) {
            if (type == TCP_HANDLE_RECEIVE && !TextUtils.isEmpty(msg)) {
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString(TCPParams.RCEV, msg);
                message.setData(bundle);
                message.what = type;
                mainHandler.sendMessage(message);
            } else {
                mainHandler.sendEmptyMessage(type);
            }
        }
    }

    @Override
    public void onRecive(ConnectUntil connectUntil, byte[] msg) {
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
        sendUi(TCP_HANDLE_CONNECT_SUCCESS,null);
    }

    @Override
    public void onDisConnect(ConnectUntil connectUntil) {
        sendUi(TCP_HANDLE_CONNECT_BREAK,null);//连接断开
        mConnectHandler.sendEmptyMessage(TcpConnectHandler.CLOSE);
    }

    @Override
    public void onConnectFail(ConnectUntil connectUntil) {
        sendUi(TCP_HANDLE_CONNECT_ERROR,null);

        if (TCPParams.isNetWork.get()) {
          //  Log.i("mylog", "restart()");
            //在网络正常的时候才试图重连
            connectUntil.restart();
        } else {
           // Log.i("mylog", "close()");
            connectUntil.close();
        }
    }

    public void close() {
        mConnectHandler.sendEmptyMessage(TcpConnectHandler.CLOSE);
    }

    class TcpConnectHandler extends Handler {
        public static final int SEND = 0x01;//发送指令
        public static final int CLOSE = 0x02;//发送指令

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SEND:
                    String info = msg.getData().getString(TCPParams.SEND);
                    if (mConnectUntil != null && mConnectUntil.isConnect())
                        try {
                            mConnectUntil.send(info.getBytes("GBK"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    break;
                case CLOSE:
                    mConnectHandler.removeCallbacksAndMessages(null);
                    mConnectHandler = null;
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
