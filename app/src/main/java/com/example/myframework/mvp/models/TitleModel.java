package com.example.myframework.mvp.models;

import com.example.myframework.ui.adapter.base.BaseMulDataModel;

/**
 * author huanghao
 * Created by 98733 on 2018/8/21.
 * info 这个是列表展示的标题实体类
 */

public class TitleModel extends BaseMulDataModel {
    private String title;
    private int row;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public TitleModel(String title, int type) {
        setType(type);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
