package com.example.myframework.ui.fragment;

import android.support.v4.app.Fragment;

import com.example.baselib.base.BaseMVPFragment;
import com.example.myframework.R;
import com.example.myframework.mvp.presenters.FirstFraPresenter;
import com.example.myframework.mvp.views.FirstFragView;
import com.example.mytcpandws.utils.MyLog;

import java.util.List;

/**
 * createBy ${huanghao}
 * on 2019/7/8
 */
public class FirstTabFragment extends BaseMVPFragment<FirstFragView, FirstFraPresenter> implements FirstFragView {



    public static FirstTabFragment newInstance(String info) {
        FirstTabFragment fragment = new FirstTabFragment();
      /*  Bundle args = new Bundle();
        args.putString("info", info);
        fragment.setArguments(args);*/
        fragment.setTitle(info);
        return fragment;
    }


    @Override
    protected FirstFraPresenter createPresenter() {
        return new FirstFraPresenter();
    }

    @Override
    protected void initData() {

    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_second;
    }

    @Override
    protected void lazyLoadData() {
        MyLog.i("我到了加载网络这里: "+getTitle());
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {
        showToast(msg);
    }


}
