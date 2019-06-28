package com.example.baselib.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.baselib.myapplication.App;
import com.example.baselib.utils.CustomToast;
import com.example.baselib.utils.KeyBoardUtil;
import com.example.baselib.utils.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

/*
* create by hh
* date 2019-6-25
* */

/*
* 1.使用rxpermissions
* 2.EventBus(使用要重写isUseEventBus 和 在子类 实@Subscrice)
* 3.StatusBarColor
* 4.KeyBoard键盘和输入法的弹出和销毁
* 5.LeakCanary的监听
* */

public abstract class BaseActivity extends AppCompatActivity  {

    private RxPermissions mRxPermissions;
    private final int duration=2000;


    //初始化视图
   abstract void initView();

    protected abstract int getLayoutRes();

   //开始请求(在初始化试图之后执行)
   protected abstract void startRequest();

   //是否使用事件总线
   public boolean isUseEventBus(){return false;};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        // 强制竖屏
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(getLayoutRes());



        //有注册事件总线,BasePresenter里面也注册了事件总线
        if (isUseEventBus()) {
            EventBus.getDefault().register(this);
        }
        initView();
        startRequest();
    }

    //获取RxPermissions,进行权限的动态设置
   public  RxPermissions getRxPermissions(){
       if(mRxPermissions==null){
           synchronized (this){
               if(mRxPermissions==null){
                   mRxPermissions=new RxPermissions(this);
               }else {
                   return mRxPermissions;
               }
           }
       }
       return mRxPermissions;
   }

   //设置状态栏的背景颜色
   public void setStatusBarColor(@ColorInt int color){
       StatusBarUtil.setColor(this, color, 0);
   }

    /**
     * 设置状态栏图标的颜色
     *
     * @param dark true: 黑色  false: 白色
     */
    public void  setStatusBarIcon(Boolean dark) {
        StatusBarUtil.setLightStatusBar(this, dark);
    }


    //是否弹出输入框
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            View v = getCurrentFocus();
            // 如果不是落在EditText区域，则需要关闭输入法
            if (KeyBoardUtil.isShouldHideKeyboard(v, ev)) {
                KeyBoardUtil.closeKeybord(v, this);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //菜单项
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消事件总线
        if (isUseEventBus()) {
            EventBus.getDefault().unregister(this);
        }

        //解决输入法引起的内存泄漏
        KeyBoardUtil.fixInputMethodManagerLeak(this);

        ((App) getApplication()).getRefWatcher(this).watch(this);


    }

    public void showToast(String msg) {
        CustomToast.showToast(this,msg,duration);
    }

}
