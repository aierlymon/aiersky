package com.example.myframework.ui.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baselib.base.BaseMVPFragment;
import com.example.myframework.R;
import com.example.myframework.mvp.models.TitleModel;
import com.example.myframework.mvp.presenters.SecondFraPresenter;
import com.example.myframework.mvp.views.SecondFragView;
import com.example.myframework.ui.adapter.FragSecondRecyVAdapter;
import com.example.myframework.ui.adapter.base.BaseMulDataModel;
import com.example.myframework.ui.adapter.decoration.SpaceItemDecoration;
import com.example.mytcpandws.utils.MyLog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * createBy ${huanghao}
 * on 2019/7/9
 */
public class SecondTabFragment extends BaseMVPFragment<SecondFragView, SecondFraPresenter> implements SecondFragView {

    public static SecondTabFragment newInstance(String info) {
        SecondTabFragment fragment = new SecondTabFragment();
        fragment.setTitle(info);
        return fragment;
    }


    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.recyclerview_second)
    RecyclerView recyclerView;

    @Override
    protected SecondFraPresenter createPresenter() {
        return new SecondFraPresenter();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_second;
    }

    @Override
    protected void lazyLoadData() {
        MyLog.i("我来到了SecondTabFragment的懒加载：" + getTitle());
    }

    private List<BaseMulDataModel> list;
    private  FragSecondRecyVAdapter adapter;
    @Override
    protected void initView() {
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            MyLog.i("我触发了1");
            list.clear();
            for (int i = 0; i < 5; i++) {
                list.add(new TitleModel("我是标题: " + i, 1));
            }
            adapter.notifyDataSetChanged();
            refreshLayout.finishRefresh();
        });

        refreshLayout.setOnLoadMoreListener(refreshLayout1 -> {
            MyLog.i("我触发了2");
            for (int i = 0; i < 10; i++) {
                list.add(new TitleModel("我是标题: " + i, 1));
            }
            adapter.notifyDataSetChanged();
            refreshLayout.finishLoadMore();
        });
        list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            list.add(new TitleModel("我是标题: " + i, 1));
        }
        MyLog.i("SecondTabFragment list size: " + list.size());
        adapter = new FragSecondRecyVAdapter(list,getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10,10,20));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {

    }
}
