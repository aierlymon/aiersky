package com.example.baselib.myapplication;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.text.TextUtils;

import androidx.multidex.MultiDex;

import com.example.baselib.broadcast.NetWorkStateBroadcast;
import com.example.baselib.http.HttpConstant;
import com.example.baselib.utils.MyLog;
import com.example.baselib.utils.Utils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.statistics.common.DeviceConfig;

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

        //极光推送的初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        HttpConstant.context=this.getApplicationContext();
        NetWorkStateBroadcast.isOnline.set(Utils.isNetworkConnected(this));


        //友盟的日志打印
        UMConfigure.setLogEnabled(true);

        //友盟初始化
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "1fe6a20054bcef865eeb0991ee84525b");

        //在友盟上用到，获取测试设备的一些信息填上去
        String[] apps=getTestDeviceInfo(this);
        //获取清单文件配置的通道号
        String channel=getAppMetaData(this,"UMENG_CHANNEL");
        MyLog.i("apps device id: "+apps[0]+"   mac: "+apps[1]+" channel: "+channel);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    public static String[] getTestDeviceInfo(Context context){
        String[] deviceInfo = new String[2];
        try {
            if(context != null){
                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context);
                deviceInfo[1] = DeviceConfig.getMac(context);
            }
        } catch (Exception e){
        }
        return deviceInfo;
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

    public static String getAppMetaData(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String channelNumber = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelNumber = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelNumber;
    }

}
