package com.tobyli16.indexlibrary.index.bean;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class SeparatorViewBean extends BaseViewBean{
    public ImageObject getImgObj() {
        return imgObj;
    }

    public String getTitle() {
        return title;
    }

    ImageObject imgObj = null;
    String title  = null;

    public static SeparatorViewBean create(JSONObject rawObj){
        SeparatorViewBean svBean = new SeparatorViewBean();
        svBean.imgObj = new ImageObject(rawObj.getString(ImageObject.imgUrl_key));
        svBean.title = rawObj.getString("title");
        return svBean;

    }

}
