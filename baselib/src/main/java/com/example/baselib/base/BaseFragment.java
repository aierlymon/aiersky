package com.example.baselib.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baselib.utils.CustomToast;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
* 1.获取权限处理类
* 2.实现懒加载
* 3.eventBus(使用要重写isUseEventBus 和 在子类 实@Subscrice)
* 4。butterknife
* */

/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
abstract class BaseFragment extends Fragment {

    private final int duration=2000;
    private RxPermissions mRxPermissions;
    private Unbinder unbinder;

    private boolean isViewCreated;//视图是否已经创建
    private boolean isUiVisible;//该fragment是否对用户可见

    //加载layout的Res
    protected abstract int getLayoutRes();

    //懒加载数据
    protected abstract void lazyLoadData();

    //是否开启事件总线
    public boolean isUseEventBus(){return false;};

    //一些初始化信息，可以被子类重写
    public void init(){};

    //作为初始化控件
    void initView(View view){};

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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            isUiVisible = true;
            lazyLoad();
        } else {
            isUiVisible = false;
        }
    }



    private void lazyLoad(){
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,
        // 并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUiVisible) {
            lazyLoadData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUiVisible = false;

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isUseEventBus()){
            EventBus.getDefault().register(this);
        }
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        lazyLoad();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(getLayoutRes(),container,false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(isUseEventBus()){
            EventBus.getDefault().unregister(this);
        }
    }

    public void showToast(String msg){
        CustomToast.showToast(getContext(),msg,duration);
    }
}
