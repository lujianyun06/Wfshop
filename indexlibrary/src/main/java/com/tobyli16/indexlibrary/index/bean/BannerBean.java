package com.tobyli16.indexlibrary.index.bean;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class BannerBean extends BaseViewBean{
    public ArrayList<ImageObject> getBannImgs() {
        return bannImgs;
    }

    public int getDuration() {
        return duration;
    }

    ArrayList<ImageObject> bannImgs = new ArrayList<>();
    private int duration = 5;  //default value;

    public static BannerBean create(JSONObject rawObj){
        BannerBean bannerBean = new BannerBean();
        bannerBean.duration = rawObj.getInteger("duration");
        bannerBean.bannImgs =
                ImageObject.JSONArray2ImageArray(rawObj.getJSONArray("images"));
        return bannerBean;

    }

}
