package com.example.baselib.utils;

import android.util.Log;

import com.example.baselib.BuildConfig;

/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
public class MyLog {
    public static void i(String info){
        if(BuildConfig.DEBUG){
            Log.i("mylog", info);
        }
    }
}
