package com.example.baselib.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselib.R;

/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
public class CustomToast {
    private static Toast mToast;

    public static void showToast(Context mContext, String text, int duration) {
        if(mToast==null)
        mToast=new Toast(mContext);
        mToast.setDuration(duration);
        View view= LayoutInflater.from(mContext).inflate(R.layout.toast_custom,null);
        TextView tx=view.findViewById(R.id.tv_prompt);
        tx.setText(text);
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        mToast.setText(text);

        mToast.show();
    }

    public static void showToast(Context mContext, int resId, int duration) {
        showToast(mContext, mContext.getResources().getString(resId), duration);
    }

}
