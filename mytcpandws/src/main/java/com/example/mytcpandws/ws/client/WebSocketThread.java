package com.example.mytcpandws.ws.client;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.example.mytcpandws.params.WSParams;

/**
 * createBy ${huanghao}
 * on 2019/7/1
 * data 一般websocket只会开启一个吧，所以不用过多管理(暂时= =)
 */
public class WebSocketThread<T extends Handler> extends Thread implements WebSocketUtil.MsgListener {


    private String address;

    private int port;

    private T mUIWebSocketHandle;

    private WsConnectHandler mConnectHandler;

    private WebSocketUtil mWebSocketUtil;

    public WebSocketThread(String address, int port, T mWebSocketHandle) {
        this.address = address;
        this.port = port;
        this.mUIWebSocketHandle = mWebSocketHandle;
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

    private void sendUi(int type, String msg) {
        if (mUIWebSocketHandle != null) {
            if (type == WSParams.WS_HANDLE_RECEIVE && !TextUtils.isEmpty(msg)) {
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString(WSParams.RCEV, msg);
                message.setData(bundle);
                message.what = type;
                mUIWebSocketHandle.sendMessage(message);
            } else {
                mUIWebSocketHandle.sendEmptyMessage(type);
            }
        }
    }


    @Override
    public void onOpen() {
        //连接成功
        sendUi(WSParams.WS_HANDLE_CONNECT_SUCCESS, null);
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        //连接关闭
        sendUi(WSParams.WS_HANDLE_CONNECT_BREAK, null);

    }

    @Override
    public void onReceive(String msg) {
        //接受到数据
        sendUi(WSParams.WS_HANDLE_RECEIVE, msg);
    }

    @Override
    public void onError() {
        //数据错误
        sendUi(WSParams.WS_HANDLE_ERROR, null);
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
                    mUIWebSocketHandle = null;
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
