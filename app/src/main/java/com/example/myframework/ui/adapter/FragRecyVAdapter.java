package com.example.myframework.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myframework.R;
import com.example.myframework.mvp.models.DataViewModel;
import com.example.myframework.mvp.models.TitleModel;
import com.example.myframework.ui.adapter.base.BaseMulDataModel;
import com.example.myframework.ui.adapter.base.BaseMulViewHolder;

import java.util.List;

/**
 * createBy ${huanghao}
 * on 2019/7/8
 */
public class FragRecyVAdapter extends RecyclerView.Adapter<BaseMulViewHolder> {

    public static final int TYPE_Title = 1;
    public static final int TYPE_View = 2;

    private List<BaseMulDataModel> modelList;

    public FragRecyVAdapter(List<BaseMulDataModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public BaseMulViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TYPE_Title:
                return new TitleViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.frg_first_title, viewGroup, false));
            case TYPE_View:
                return new DataViewViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.frg_first_data, viewGroup, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseMulViewHolder baseMulViewHolder, int pos) {
        baseMulViewHolder.bindData(modelList.get(pos),pos);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (modelList.get(position) instanceof TitleModel) {
            return TYPE_Title;
        } else {
            return TYPE_View;
        }
    }

    //泛型是放实体类的
    class TitleViewHolder extends BaseMulViewHolder<TitleModel> {


        public TitleViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void bindData(TitleModel dataModel, int position) {

        }
    }

    class DataViewViewHolder extends BaseMulViewHolder<DataViewModel>{

        public DataViewViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(DataViewModel dataModel, int position) {

        }
    }
}
