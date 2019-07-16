package com.example.mytcpandws.tcpconnect;


import com.example.mytcpandws.utils.MyLog;

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

    //这个是绑定所有ConnectUntil，不可以删除（连接过程跳转如果用clientMap记录不到状态）
    private volatile static Map<String, ConnectUntil> map = new HashMap<>();


    public synchronized static Map<String, ConnectUntil> getMap() {
        return map;
    }

    public synchronized static void closeAllContainGroup(boolean isGroup) {
        MyLog.i("进来了ConnectUntilBox方法closeAllContainGroup  "+map.size());
        for (Map.Entry<String, ConnectUntil> entry : map.entrySet()) {
            if (((ConnectUntil) entry.getValue()) != null) {
                ((ConnectUntil) entry.getValue()).stopThread();
                ((ConnectUntil) entry.getValue()).closeClient();
            }
        }
        map.clear();
        if (group != null&&isGroup) group.shutdownGracefully();
    }



}
