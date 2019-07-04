package com.example.mytcpandws.tcpconnect;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.mytcpandws.params.TCPParams;

import java.io.UnsupportedEncodingException;

import static com.example.mytcpandws.params.TCPParams.SEND;
import static com.example.mytcpandws.params.TCPParams.TCP_HANDLE_CONNECT_BREAK;
import static com.example.mytcpandws.params.TCPParams.TCP_HANDLE_CONNECT_ERROR;
import static com.example.mytcpandws.params.TCPParams.TCP_HANDLE_CONNECT_SUCCESS;
import static com.example.mytcpandws.params.TCPParams.TCP_HANDLE_RECEIVE;


/**
 * Created by huanghao on 2018/7/24.
 * data 好像这个不用线程装起来，因为io.netty自带线程，不过管他呢，问题不大
 */

public class ConnectThread extends Thread implements ConnectUntil.ReciveMsgListener {

    private TcpConnectHandler mConnectHandler;
    private String ip;
    private int port;
    private ConnectUntil mConnectUntil;

    public interface OnConnectStateChangeListener {
        void onConnect();

        void onDisConnect(ConnectUntil connectUntil);

        void onConnectFail(ConnectUntil connectUntil);

        void onReceive(String msg);
    }

    private OnConnectStateChangeListener mConnectStateChangeListener;

    public ConnectThread(String ip, int port, OnConnectStateChangeListener mConnectStateChangeListener) {
        this.ip = ip;
        this.port = port;
        this.mConnectStateChangeListener = mConnectStateChangeListener;
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


    @Override
    public void onRecive(ConnectUntil connectUntil, byte[] msg) {
        String info = new String(msg, 0, msg.length);
        statechange(TCP_HANDLE_RECEIVE,connectUntil,info);
    }

    @Override
    public void onConnect(ConnectUntil connectUntil) {
        statechange(TCP_HANDLE_CONNECT_SUCCESS,connectUntil,null);//连接成功
    }

    @Override
    public void onDisConnect(ConnectUntil connectUntil) {
        statechange(TCP_HANDLE_CONNECT_BREAK,connectUntil,null);//连接断开
        close();
    }

    @Override
    public void onConnectFail(ConnectUntil connectUntil) {
        statechange(TCP_HANDLE_CONNECT_BREAK,connectUntil,null);//无法连接主机
        //这里是设置一直尝试重新连接
        if (TCPParams.isNetWork.get()) {
            //在网络正常的时候才试图重连
            connectUntil.restart();
        } else {
            connectUntil.close();
        }
    }

    public void statechange(int type, ConnectUntil connectUntil, String msg) {
        switch (type) {
            case TCP_HANDLE_RECEIVE:
                //接受到数据
                mConnectStateChangeListener.onReceive(msg);
                break;
            case TCP_HANDLE_CONNECT_SUCCESS:
                //连接成功
                mConnectStateChangeListener.onConnect();
                break;
            case TCP_HANDLE_CONNECT_BREAK:
                //断开连接
                mConnectStateChangeListener.onDisConnect(connectUntil);
                break;
            case TCP_HANDLE_CONNECT_ERROR:
                //无法连接到主机
                mConnectStateChangeListener.onConnectFail(connectUntil);
                break;
        }
    }

    public void close() {
        if (mConnectHandler != null)
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
                    if (mConnectUntil != null && mConnectUntil.isActive())
                        try {
                            mConnectUntil.send(info.getBytes("GBK"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    break;
                case CLOSE:
                    mConnectHandler.removeCallbacksAndMessages(null);
                    mConnectHandler = null;
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

    public boolean isActive(){
        return mConnectUntil.isActive();
    }

    public boolean isWritable(){
        return mConnectUntil.isWritable();
    }
}
