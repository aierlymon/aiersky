package com.example.myframework.mvp.models;

import com.example.myframework.ui.adapter.base.BaseMulDataModel;

/**
 * author huanghao
 * Created by 98733 on 2018/8/21.
 * info 列表式展示的数据实体类
 */

public class DataViewModel extends BaseMulDataModel {
    private int hasNum;
    private int count;
    private String prduceName;
    private int col;
    private int row;
    private String img_url;
    private String No;

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        this.No = no;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public DataViewModel(int hasNum, int count, String prduceName, int type) {
        setType(type);
        this.hasNum = hasNum;
        this.count = count;
        this.prduceName = prduceName;
    }

    public int getHasNum() {
        return hasNum;
    }

    public void setHasNum(int hasNum) {
        this.hasNum = hasNum;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPrduceName() {
        return prduceName;
    }

    public void setPrduceName(String prduceName) {
        this.prduceName = prduceName;
    }
}
