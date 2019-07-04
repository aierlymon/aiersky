package com.example.mytcpandws.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.mytcpandws.params.TCPParams;

/**
 * Created by huanghao on 2018/9/6.
 */

public class NetWorkStateBroadcast extends BroadcastReceiver {

    public interface OnNetStateChangListener{
        void onNetWorkSuccess();
        void onNetWorkFail();
    }

    private OnNetStateChangListener mOnNetStateChangListener;

    public void setmOnNetStateChangListener(OnNetStateChangListener mOnNetStateChangListener) {
        this.mOnNetStateChangListener = mOnNetStateChangListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        TCPParams.isNetWork.set(true);
                        if(mOnNetStateChangListener!=null)mOnNetStateChangListener.onNetWorkSuccess();
                    }
                } else {
                    if(mOnNetStateChangListener!=null)mOnNetStateChangListener.onNetWorkFail();
                    TCPParams.isNetWork.set(false);
                }
            }
        }
    }


}
