package com.example.mytcpandws.params;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by huanghao on 2018/7/19.
 */

public class TCPParams {

    public static AtomicBoolean isNetWork=new AtomicBoolean();

    public static final int TCP_HANDLE_CONNECT_ERROR=0;//正在连接tcp
    public static final int TCP_HANDLE_CONNECT_SUCCESS=1;//连接成功
    public static final int TCP_HANDLE_CONNECT_BREAK=2;//连接断开
    public static final int TCP_HANDLE_RECEIVE=3;//接收到TCP返回的数据


    public static String SEND="msg";//发送数据的key
}
