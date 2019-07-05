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
    private StringBuilder mProgressBuild=new StringBuilder();

    public interface OnConnectStateChangeListener {
        void onConnect(ConnectUntil connectUntil);

        void onDisConnect();

        void onConnectFail(ConnectUntil connectUntil);

        void onReceive(String msg);

        //可以看到完整的一条数据的发送过程
        void onProgressLog(String log);
    }

    private OnConnectStateChangeListener mConnectStateChangeListener;

    public ConnectThread(String ip, int port, OnConnectStateChangeListener mConnectStateChangeListener) {
        this.ip = ip;
        this.port = port;
        this.mConnectStateChangeListener = mConnectStateChangeListener;
    }


    public void send(String msg) {
        mProgressBuild.delete(0,mProgressBuild.length());
        Message message = Message.obtain();
        message.what = TcpConnectHandler.SEND;
        Bundle bundle = new Bundle();
        bundle.putString(SEND, msg);
        message.setData(bundle);
        if (mConnectHandler != null){
            mProgressBuild.append("准备发送就绪->");
            mConnectHandler.sendMessage(message);
        }else{
            mProgressBuild.append("mConnectHandler 没有生成，建议延时");
            mConnectStateChangeListener.onProgressLog(mProgressBuild.toString());
        }
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
    public void onDisConnect() {
        closeClient();
        statechange(TCP_HANDLE_CONNECT_BREAK,null,null);//连接断开
    }

    @Override
    public void onConnectFail(ConnectUntil connectUntil) {
        statechange(TCP_HANDLE_CONNECT_ERROR,connectUntil,null);//无法连接主机
    }

    public void statechange(int type, ConnectUntil connectUntil, String msg) {
        switch (type) {
            case TCP_HANDLE_RECEIVE:
                //接受到数据
                mConnectStateChangeListener.onReceive(msg);
                break;
            case TCP_HANDLE_CONNECT_SUCCESS:
                //连接成功
                mConnectStateChangeListener.onConnect(connectUntil);
                break;
            case TCP_HANDLE_CONNECT_BREAK:
                //断开连接
                mConnectStateChangeListener.onDisConnect();
                break;
            case TCP_HANDLE_CONNECT_ERROR:
                //无法连接到主机
                mConnectStateChangeListener.onConnectFail(connectUntil);
                break;
        }
    }

    //关闭线程的方法，关闭了线程就结束了
    public void closeClient() {
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
                    if (mConnectUntil != null && mConnectUntil.isActive()){
                        try {
                           int state= mConnectUntil.send(info.getBytes("utf-8"));
                           if(state==ConnectUntilBox.SendSuccess){
                               mProgressBuild.append("恭喜你，发送成功");
                           }else{
                               mProgressBuild.append("很可惜，通道在准备发送的时候关闭了，数据发送不出");
                           }
                            mConnectStateChangeListener.onProgressLog(mProgressBuild.toString());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }else{
                        mProgressBuild.append("tcp突然就关闭了，你的数据发送不出去了");
                        mConnectStateChangeListener.onProgressLog(mProgressBuild.toString());
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
                    break;
            }
        }
    }

    public boolean isActive(){
        if(mConnectUntil==null)return false;
        return mConnectUntil.isActive();
    }


}
