package com.example.baselib.utils;

import android.app.Activity;
import android.content.Context;

import com.example.baselib.widget.CustomDialog;

import java.lang.ref.WeakReference;

/**
 * createBy ${huanghao}
 * on 2019/7/3
 */
public class LoadDialogUtil {
    private LoadDialogUtil() {
    }

    private WeakReference<Activity> weakReference;
    private CustomDialog loadDialog;

    static LoadDialogUtil loadDialogUtil;

    public static LoadDialogUtil getInstance() {
        if (loadDialogUtil == null) {
            loadDialogUtil = new LoadDialogUtil();
        }
        return loadDialogUtil;
    }

    public CustomDialog getLoadDialog(Activity context) {
        weakReference=new WeakReference<>(context);
        CustomDialog.Builder builder = new CustomDialog.Builder(weakReference.get());
        builder.setMessage("正在加载");
        if(loadDialog!=null){
            loadDialog.hide();
            loadDialog=null;
        }
        loadDialog = builder.create();
        loadDialog.setCanceledOnTouchOutside(false);
        return loadDialog;
    }

    public void show(){
        if(loadDialog!=null){
            loadDialog.show();
        }
    }

    public void cancel(){
        if(loadDialog!=null){
            loadDialog.cancel();
            loadDialog=null;
            weakReference.clear();
            weakReference=null;
        }
    }


}
