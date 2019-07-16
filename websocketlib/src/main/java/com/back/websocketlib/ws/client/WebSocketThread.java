package com.back.websocketlib.ws.client;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.back.websocketlib.params.WSParams;

/**
 * createBy ${huanghao}
 * on 2019/7/1
 * data 一般websocket只会开启一个吧，所以不用过多管理(暂时= =)
 */
public class WebSocketThread extends Thread implements WebSocketUtil.MsgListener {


    private String address;

    private int port;


    private WsConnectHandler mConnectHandler;

    private WebSocketUtil mWebSocketUtil;


    public interface OnConnectStateChangeListener {
        void onConnect();

        void onDisConnect();

        void onConnectFail();

        void onReceive(String msg);
        
    }

    private OnConnectStateChangeListener onConnectStateChangeListener;



    public WebSocketThread(String address, int port,OnConnectStateChangeListener onConnectStateChangeListener ) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        super.run();
        try {
            mWebSocketUtil = new WebSocketUtil(address, port);
            mWebSocketUtil.setMsgListener(this);
            mWebSocketUtil.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Looper.prepare();
        mConnectHandler = new WsConnectHandler();
        Looper.loop();
    }



    @Override
    public void onOpen() {
        //连接成功
        onConnectStateChangeListener.onConnect();
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        //连接关闭
        onConnectStateChangeListener.onDisConnect();

    }

    @Override
    public void onReceive(String msg) {
        //接受到数据
        onConnectStateChangeListener.onReceive(msg);
    }

    @Override
    public void onError() {
        //数据错误
        onConnectStateChangeListener.onConnectFail();
    }

    public void send(String msg) {
        if (mConnectHandler != null) {
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString(WSParams.SEND, msg);
            message.what = WsConnectHandler.SEND;
            message.setData(bundle);
            mConnectHandler.sendMessage(message);
        }
    }

    public void close(){
        if( mConnectHandler!=null){
            mConnectHandler.sendEmptyMessage(WsConnectHandler.CLOSE);
        }
    }

    class WsConnectHandler extends Handler {
        private static final int SEND = 0;
        private static final int CLOSE = 1;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SEND:
                    if(mWebSocketUtil!=null&&mWebSocketUtil.isConnect){
                        String info=msg.getData().getString(WSParams.SEND);
                        mWebSocketUtil.send(info.getBytes());
                    }

                    break;
                case CLOSE:
                    mConnectHandler.removeCallbacksAndMessages(null);
                    mConnectHandler=null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        Looper.myLooper().quitSafely();
                    } else {
                        Looper.myLooper().quit();
                    }
                    mWebSocketUtil.close();
                    break;
            }
        }
    }
}
