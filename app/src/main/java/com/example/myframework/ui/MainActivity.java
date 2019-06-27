package com.example.myframework.ui;

import android.annotation.SuppressLint;
import android.widget.TextView;

import com.example.baselib.base.BaseMvpTitleActivity;
import com.example.baselib.utils.MyLog;
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
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void showDefaultMsg(String msg) {

    }

    @Override
    public void showMsg(String msg) {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showMainMsg(String a) {
        MyLog.i("我走到了这里快要吐司了");
        showToast(a);
    }

    @OnClick(R.id.btn)
    public void onClick(){
        mPresenter.success("Hello World");
    }


}
