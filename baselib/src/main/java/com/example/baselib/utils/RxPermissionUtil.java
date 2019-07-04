package com.example.baselib.utils;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * createBy ${huanghao}
 * on 2019/7/3
 */
public class RxPermissionUtil {


    private RxPermissionUtil() {
    }



    private static RxPermissionUtil rxPermissionUtil;


    //肯定在主线程，不用同步
    public static RxPermissionUtil getInstance() {
        if (rxPermissionUtil == null) {
            rxPermissionUtil = new RxPermissionUtil();
        }
        return rxPermissionUtil;
    }

    public void permission(Activity activity, String[] list) {
        RxPermissions rxPermissions = new RxPermissions((FragmentActivity) activity);
        rxPermissions.requestEach(list).subscribe(permission -> {
            if (permission.granted) {
                MyLog.i("获取了" + permission.name + "权限");
            } else if (permission.shouldShowRequestPermissionRationale) {
                MyLog.i("应该获取了" + permission.name + "权限");
            } else {
                MyLog.i("没有获取到" + permission.name + "权限");
            }
        });
    }
}
