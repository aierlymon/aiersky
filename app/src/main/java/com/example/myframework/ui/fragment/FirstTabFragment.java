package com.example.myframework.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;

import com.example.baselib.base.BaseMVPFragment;
import com.example.myframework.R;
import com.example.myframework.mvp.presenters.FirstFraPresenter;
import com.example.myframework.mvp.views.FirstFragView;
import com.example.myframework.ui.adapter.FragFirstRecyVAdapter;
import com.example.myframework.ui.adapter.base.BaseMulDataModel;
import com.example.myframework.ui.widgets.AutoPollRecyclerView;
import com.example.mytcpandws.utils.MyLog;

import java.util.List;

import butterknife.BindView;

/**
 * createBy ${huanghao}
 * on 2019/7/8
 */

/*
* 这个实现的是<!--这个是带有AutoRecyclerView和CoordinatorLayout+AppLayout+CollapsingToolbarLayout的界面-->
* */
public class FirstTabFragment extends BaseMVPFragment<FirstFragView, FirstFraPresenter> implements FirstFragView {

    @BindView(R.id.recyclerview)
    AutoPollRecyclerView recyclerView;

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
    protected void initView() {
        // 定义一个线性布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        // 设置布局管理器
        recyclerView.setLayoutManager(manager);

    }


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_first;
    }

    @Override
    protected void lazyLoadData() {
        mPresenter.requestHttp();
        MyLog.i("我到了FirstTabFragment的懒加载: "+getTitle());
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void refreshUi(List<BaseMulDataModel> beanList) {
        FragFirstRecyVAdapter fragRecyVAdapter=new FragFirstRecyVAdapter(beanList);
        recyclerView.setAdapter(fragRecyVAdapter);
       // recyclerView.start();
    }
}
