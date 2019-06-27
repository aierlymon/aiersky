package com.example.baselib.myapplication;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

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
