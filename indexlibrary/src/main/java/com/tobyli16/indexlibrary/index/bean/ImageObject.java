package com.tobyli16.indexlibrary.index.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;




import java.util.ArrayList;

public class ImageObject {
    final static public String imgUrl_key = "img_url";
    final static public String weight_key = "weight";

    public String imgUrl = null;
    public String weight = null;


    private ImageObject(){

    }

    public ImageObject(String imgUrl){
        this.imgUrl = imgUrl;
    }

    public static ImageObject JSONObj2ImageObj(JSONObject jsonObj)  {
        ImageObject imgObj = new ImageObject();
        imgObj.imgUrl = jsonObj.getString(imgUrl_key);
        imgObj.weight = jsonObj.getString(weight_key);
        return imgObj;
    }

    public static ArrayList<ImageObject> JSONArray2ImageArray(JSONArray jsonArray){
       int len = jsonArray.size();
       ArrayList<ImageObject> imgObjs = new ArrayList<>();
        for (int i =0;i<len;i++){
            ImageObject imgObj = JSONObj2ImageObj(jsonArray.getJSONObject(i));
            imgObjs.add(imgObj);
        }
        return imgObjs;
    }
}
