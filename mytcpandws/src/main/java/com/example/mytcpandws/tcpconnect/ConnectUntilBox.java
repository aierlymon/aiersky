package com.example.mytcpandws.tcpconnect;


import java.util.HashMap;
import java.util.Map;

import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Created by huanghao on 2017/8/14.
 */
public class ConnectUntilBox {

    private final static String TAG = "ConnectUntilBox";
    public static final int OpenSocketsuccessful = 0x01;//连接成功
    public static final int NoRouteToHost = 0x00;//连接失败
    public static final int SendSuccess = 0x02;//发送成功
    public static final int Fail = 0x03;//发送失败


    protected static NioEventLoopGroup group = new NioEventLoopGroup(1);

    private volatile static Map<String, ConnectUntil> map = new HashMap<>();

    protected synchronized static Map<String, ConnectUntil> getMap() {
        return map;
    }


    public synchronized static void closeAllContainGroup(boolean isGroup) {
        for (Map.Entry<String, ConnectUntil> entry : map.entrySet()) {
            if (((ConnectUntil) entry.getValue()) != null) {
                ((ConnectUntil) entry.getValue()).close();

            }
        }
        map.clear();
        if (group != null&&isGroup) group.shutdownGracefully();
    }

    public synchronized static void close(String ip, int port) {
        ConnectUntil c1 = map.get(ip + ":" + port);
        if (c1 != null) {
            c1.closeClient();
            map.remove(ip + ":" + port);
        }
    }

}
