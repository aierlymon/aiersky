package com.example.baselib.mvp;

/*
* author hh
* createby 2019-6-25
* */
public interface IView {
    /**
     * 显示加载
     */
     void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 使用默认的样式显示信息: CustomToast
     */
    void showDefaultMsg(String msg);

    /**
     * 显示信息
     */
    void showMsg(String msg);

    /**
     * 显示错误信息
     */
    void showError(String msg);

    void showToast(String msg);
}
