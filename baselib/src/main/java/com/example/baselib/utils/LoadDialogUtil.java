package com.example.baselib.utils;

import android.content.Context;

import com.example.baselib.widget.CustomDialog;

/**
 * createBy ${huanghao}
 * on 2019/7/3
 */
public class LoadDialogUtil {
    private LoadDialogUtil() {
    }


    private CustomDialog loadDialog;

    static LoadDialogUtil loadDialogUtil;

    public static LoadDialogUtil getInstance() {
        if (loadDialogUtil == null) {
            loadDialogUtil = new LoadDialogUtil();
        }
        return loadDialogUtil;
    }

    public CustomDialog getLoadDialog(Context context) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
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
        }
    }


}
