package com.example.mytcpandws.tcpconnect;

import com.example.mytcpandws.broadcast.NetWorkStateBroadcast;
import com.example.mytcpandws.utils.MyLog;

/**
 * createBy ${huanghao}
 * on 2019/7/5
 */
public class TcpClient {

    private String ip;
    private int port;
    private volatile boolean mBreakToRestart;//如果连接成功后连接断开了(当切换网络/服务器主动断开的)是否进行重连
    private volatile boolean mFailToRestart;//如果一开始没连接成功,超时后是否继续连接成

    public TcpClient(String ip, int port,boolean mBreakToRestart,boolean mFailToRestart) {
        this.ip = ip;
        this.port = port;
        this.mBreakToRestart=mBreakToRestart;
        this.mFailToRestart=mFailToRestart;
    }


    //tcp连接线程
    private ConnectThread connectThread;

    public void connect() {
        if (connectThread != null) {
            if (connectThread.isActive()) {
                return;
            } else {
                connectThread.closeClient();
                connectThread = null;
            }
        }
        connectThread = new ConnectThread(ip, port, new ConnectThread.OnConnectStateChangeListener() {
            @Override
            public void onConnect(ConnectUntil connectUntil) {
                int state = connectUntil.send("你好");
                MyLog.i("连接上了: " + connectUntil + "  state：  " + state + "  thread name: " + Thread.currentThread().getName());
            }

            @Override
            public void onDisConnect() {
                //connectUntil 已经没救了了
                //连接主动断开，再次启动连接
                MyLog.i("连接主动断开: " + "  thread name: " + Thread.currentThread().getName());
                if(mBreakToRestart&&NetWorkStateBroadcast.isOnline.get())
                connect();
            }

            @Override
            public void onConnectFail(ConnectUntil connectUntil) {
                MyLog.i("连接tcp失败，找不到主机,再次连接： " + connectUntil.isActive()+"    mFailToRestart: "+mFailToRestart+"  connectUntil: "+connectUntil);
                if(mFailToRestart&&NetWorkStateBroadcast.isOnline.get())
                    connectUntil.restart();
            }

            @Override
            public void onReceive(String msg) {
                MyLog.i("msg: " + msg);
            }

            @Override
            public void onProgressLog(String log) {
                MyLog.i("发送流程: " + log + "  Thread:" + Thread.currentThread().getName());
            }

            @Override
            public void onStopThread() {
                mBreakToRestart=false;
                mFailToRestart=false;
            }
        });
        connectThread.start();
    }

    public void restart() {
        connect();
    }

    public boolean sendString(String msg) {
        if (connectThread != null) {
            connectThread.sendString(msg);
            return true;
        }
        return false;
    }

    public boolean sendBytes(byte[] bytes) {
        if (connectThread != null) {
            connectThread.sendBytes(bytes);
            return true;
        }
        return false;
    }


    public void close() {
        if (connectThread != null) {
            connectThread.closeClient();
        }
    }

    public boolean isActive(){
        if(connectThread==null)return false;
        return connectThread.isActive();
    }
}
