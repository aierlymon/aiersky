package com.back.websocketlib.params;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * createBy ${huanghao}
 * on 2019/7/1
 */
public class WSParams {
    public static AtomicBoolean isNetWork=new AtomicBoolean();

    public final static int WS_HANDLE_CONNECTING=0;//正在连接ws
    public final static int WS_HANDLE_CONNECT_SUCCESS=1;//连接成功
    public final static int WS_HANDLE_CONNECT_BREAK=2;//连接断开
    public final static int WS_HANDLE_RECEIVE=3;//接收到ws返回的数据
    public final static int WS_HANDLE_ERROR=4;//接收到ws返回的数据


    public static String RCEV="data";//接受数据的key
    public static String SEND="msg";//发送数据的key
}
