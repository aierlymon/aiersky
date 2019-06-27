package com.example.baselib.base;

import com.example.baselib.mvp.IPresenter;
import com.example.baselib.mvp.IView;
/**
 * createBy ${huanghao}
 * on 2019/6/26
 */
public abstract class BaseMVPFragment<V extends IView,P extends IPresenter> extends BaseFragment implements IView{

    P mPresenter;

    protected abstract P createPresenter();



    //懒加载
    @Override
    protected void lazyLoadData() {

    }

    //这个是父类在oncreate调用的方法
    @Override
    public void init() {
        super.init();//没执行什么要不要无所谓
        mPresenter=createPresenter();
        if(mPresenter!=null){
            mPresenter.attachView((V)this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPresenter!=null){
            mPresenter.detachView();
        }
    }
}
