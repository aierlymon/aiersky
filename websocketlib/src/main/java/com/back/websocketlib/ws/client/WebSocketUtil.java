package com.back.websocketlib.ws.client;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * createBy ${huanghao}
 * on 2019/7/1
 */
public class WebSocketUtil {
    private String addr;
    private int port;
    private JWebSocketClient client;
    public boolean isConnect=false;

    public WebSocketUtil(String addr, int port) {
        this.addr = addr;
        this.port = port;
    }

    public interface MsgListener{
        void onOpen();

        void onClose(int code, String reason, boolean remote);

        void onReceive(String msg);

        void onError();
    }

    MsgListener msgListener;

    public void setMsgListener(MsgListener msgListener){
        this.msgListener=msgListener;
    }

    public void start() throws InterruptedException {
        URI uri = null;
        if (port == -1) {
            uri = URI.create("ws://" + addr + ":" + port);
        } else {
            uri = URI.create("ws://" + addr);
        }

        client  = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                //message就是接收到的消息
                msgListener.onReceive(message);
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
                isConnect=true;
                msgListener.onOpen();
            }

            @Override
            public void onError(Exception ex) {
                super.onError(ex);
                isConnect=false;
                msgListener.onError();
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                super.onClose(code, reason, remote);
                isConnect=false;
                msgListener.onClose(code,reason,remote);
            }
        };

        client.connectBlocking();

    }

    public void send(byte[] bys){
        if(client!=null&&client.isOpen())
        client.send(bys);
    }

    public void close(){
        if(client!=null){
            client.close();
            client=null;
        }
    }

    public void restart() throws InterruptedException {
        if(client.isOpen()){
            client.close();
        }
        client.connectBlocking();
    }
}
