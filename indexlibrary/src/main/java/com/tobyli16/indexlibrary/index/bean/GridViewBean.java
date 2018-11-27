package com.tobyli16.indexlibrary.index.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class GridViewBean  extends BaseViewBean{
    public String orientation = null;
    public String weight = null;
    public ArrayList<GridViewBean> subGridView = new ArrayList<>();
    public ArrayList<ImageObject> imgs = new ArrayList<>();


    public static GridViewBean create(JSONObject rawObj){
        GridViewBean gridBean = new GridViewBean();
        gridBean.orientation = rawObj.getString("orientation");
        gridBean.weight = rawObj.getString("weight");
        gridBean.addSubItem(rawObj.getJSONArray("cells"));
        return gridBean;
    }

    public void addSubItem(JSONArray cells){
        int len = cells.size();
        for (int i =0;i<len;i++){
            JSONObject jobj = (JSONObject) cells.get(i);
            String orienTest = jobj.getString("orientation");
            if (orienTest != null){
                this.subGridView.add(create(jobj));
            }
            else {
                ImageObject imgObj = ImageObject.JSONObj2ImageObj(jobj);
                this.imgs.add(imgObj);
            }
        }

    }

}
