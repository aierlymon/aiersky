package com.example.myframework.ui;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.example.baselib.base.BaseMvpTitleActivity;
import com.example.myframework.R;
import com.example.myframework.mvp.presenters.MainPresenter;
import com.example.myframework.mvp.views.MainView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseMvpTitleActivity<MainView, MainPresenter> implements MainView {


    @BindView(R.id.textview1)
    TextView tx;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void initView() {
        //状态就是看到wifi图标的那一个细栏
        //setStatusBarColor
        setStatusBarColor(R.color.design_default_color_primary);
        super.initView();
        setTitle("hello");
        setTitleColor(R.color.colorAccent);

        //绑定黄油刀
        ButterKnife.bind(this);
        tx.setText("你好");
    }

    @Override
    protected void startRequest() {

    }


    @Override
    protected int getBodyLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean hasBackHome() {
        return true;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }



    @Override
    public void showError(String msg) {
        showToast("出错误了： "+msg);
    }

    @Override
    public void responseNet(String a) {
        showToast(a);
    }

    @OnClick(R.id.btn)
    public void onClick(){
        mPresenter.success("Hello World");
    }


}
