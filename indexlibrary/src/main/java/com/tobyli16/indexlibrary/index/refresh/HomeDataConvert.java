package com.tobyli16.indexlibrary.index.refresh;

import java.util.ArrayList;

public abstract class HomeDataConvert {

    protected final ArrayList<HomeMultiItemEntity> ENTITIES = new ArrayList<>();
    private String mJsonData = null;

    public abstract ArrayList<HomeMultiItemEntity> convert();

    public HomeDataConvert setJsonData(String json) {
        this.mJsonData = json;
        return this;
    }

    protected String getJsonData() {
        if (mJsonData == null || mJsonData.isEmpty()) {
            throw new NullPointerException("DATA IS NULL!");
        }
        return mJsonData;
    }

    public void clearData(){
        ENTITIES.clear();
    }
}
