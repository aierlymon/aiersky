package com.example.mytcpandws.ws.client;

import com.example.mytcpandws.utils.MyLog;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * createBy ${huanghao}
 * on 2019/7/1
 */
public class JWebSocketClient extends WebSocketClient {

    public JWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        MyLog.i("ws onOpen");
    }

    @Override
    public void onMessage(String message) {
        MyLog.i("ws onMessage");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        MyLog.i("ws onClose");
    }

    @Override
    public void onError(Exception ex) {
        MyLog.i("ws onError");
    }
}
