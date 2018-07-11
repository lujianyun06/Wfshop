package com.tobyli16.indexlibrary.index.search;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import cn.bupt.wfshop.ui.recycler.DataConverter;
import cn.bupt.wfshop.ui.recycler.MultipleFields;
import cn.bupt.wfshop.ui.recycler.MultipleItemEntity;

/**
 * Created by tobyli
 */

public class SearchProductDataConverter extends DataConverter {


    public static String searchWord = "";

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        String str = getJsonData();
        Log.e("search",str);
//        JSONArray jsonArray = JSON.parseObject(getJsonData()).getJSONObject("obj").getJSONArray("rows");
        JSONArray jsonArray = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
//            final JSONObject data = jsonArray.getJSONObject(i).getJSONObject("specialty");
//            final JSONObject specification = jsonArray.getJSONObject(i).getJSONObject("specification");
//            final int id = data.getInteger("id");
//            final String name = data.getString("name")+" "+specification.getString("specification");
//            final double price = jsonArray.getJSONObject(i).getDouble("mPrice");
//            final String description = data.getString("descriptions");
//            final String img_url = jsonArray.getJSONObject(i).getJSONObject("iconURL").getString("thumbnailPath");
            final JSONObject data = jsonArray.getJSONObject(i);
            final int id = data.getInteger("id");
            final String name = data.getString("name");
            final double price = data.getDouble("price");
            final String description = searchWord;
            final String img_url = data.getString("img_url");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(SearchProductItemType.ITEM_SEARCH)
                    .setField(MultipleFields.ID, id)
                    .setField(MultipleFields.IMAGE_URL, img_url)
                    .setField(MultipleFields.NAME, name)
                    .setField(MultipleFields.DESCRIPTION, description)
                    .setField(MultipleFields.PRICE,price)
                    .build();

            ENTITIES.add(entity);
        }
        return ENTITIES;
    }
}
