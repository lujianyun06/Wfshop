package com.tobyli16.indexlibrary.index.bean;

import com.alibaba.fastjson.JSONObject;

public class HomePageObjFactory {
    final static private  String STYLE_ID = "style_id";
    final static private  String RATIO = "RATIO";
    final static private  String STYLE_banner = "carousel_view";
    final static private  String STYLE_grid_view = "grid_view";
    final static private  String STYLE_separator_view = "separator_view";  //根据是否有title或者imgurl判断是文字还是图片，再做判断

    public static HomePageObj createHomePageObj(JSONObject jsonObj){
        HomePageObj homePageObj = new HomePageObj();
        homePageObj.style_id = jsonObj.getString(STYLE_ID);
        homePageObj.ratio = jsonObj.getString(RATIO);
        JSONObject rawData = jsonObj.getJSONObject("data");

        if (homePageObj.style_id.equals(STYLE_banner)){
            homePageObj.data = BannerBean.create(rawData);
        } else if(homePageObj.style_id.equals(STYLE_grid_view)){
            homePageObj.data = GridViewBean.create(rawData);
        } else if(homePageObj.style_id.equals(STYLE_separator_view)){
            homePageObj.data = SeparatorViewBean.create(rawData);
        }
        return homePageObj;
    }
}
