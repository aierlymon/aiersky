package com.example.myframework.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myframework.R;
import com.example.myframework.mvp.models.TitleModel;
import com.example.myframework.ui.adapter.base.BaseMulDataModel;
import com.example.myframework.ui.adapter.base.BaseMulViewHolder;
import com.example.mytcpandws.utils.MyLog;

import java.net.URI;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * createBy ${huanghao}
 * on 2019/7/9
 */
public class FragSecondRecyVAdapter extends RecyclerView.Adapter<BaseMulViewHolder> {

    private List<BaseMulDataModel> list;
    private Context mContext;

    public FragSecondRecyVAdapter(List<BaseMulDataModel> list, Context mContext) {
        this.list = list;
        this.mContext=mContext;
    }



    @NonNull
    @Override
    public BaseMulViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.frg_second_title,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseMulViewHolder baseMulViewHolder, int i) {
        MyLog.i("信息: "+list.get(i));
        baseMulViewHolder.bindData(list.get(i),i);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends BaseMulViewHolder<TitleModel>{

        @BindView(R.id.tx_sec_title)
        TextView textView;

        @BindView(R.id.jz_video)
        JzvdStd jzvdStd;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindData(TitleModel dataModel, int position) {
            textView.setText(dataModel.getTitle());
            jzvdStd.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4","罗小白架子鼓");
            jzvdStd.thumbImageView.setImageResource(R.mipmap.ic_launcher);
        }
    }

}
