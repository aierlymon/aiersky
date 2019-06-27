package com.example.baselib.utils;

import android.util.Log;

/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
public class MyLog {
    private static boolean debug=true;
    public static void i(String info){
        if(debug){
            Log.i("mylog", info);
        }
    }
}
