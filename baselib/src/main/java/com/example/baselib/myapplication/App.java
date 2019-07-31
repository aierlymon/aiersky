package com.example.baselib.myapplication;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import androidx.multidex.MultiDex;

import com.example.baselib.broadcast.NetWorkStateBroadcast;
import com.example.baselib.http.HttpConstant;
import com.example.baselib.utils.Utils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import cn.jpush.android.api.JPushInterface;

/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
public class App extends Application {

    private  RefWatcher mRefWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        mRefWatcher=setupLeakCanary();
        // 主要是添加下面这句代码
        MultiDex.install(this);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        HttpConstant.context=this.getApplicationContext();
        NetWorkStateBroadcast.isOnline.set(Utils.isNetworkConnected(this));


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    private RefWatcher setupLeakCanary() {
        RefWatcher refWatcher=null;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            refWatcher= RefWatcher.DISABLED;
        }else{
            refWatcher=LeakCanary.install(this);
        }
        return refWatcher;
    }

    public  RefWatcher getRefWatcher(Context context){
        return mRefWatcher;
    }
}
