package com.tobyli16.indexlibrary.index.refresh;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class HomeMultiItemEntity implements MultiItemEntity {
    final public static int BANNER = 1;
    final public static int GRID = 2;
    final public static int SEPARATOR = 3;

    private int itemType = 3; //必须预设一个已经定义的初值，否则会报

    public Object getDataBean() {
        return dataBean;
    }

    public void setDataBean(Object dataBean) {
        this.dataBean = dataBean;
    }

    private Object dataBean;

    public HomeMultiItemEntity(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

}
