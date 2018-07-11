package com.tobyli16.profilelibrary.personal.fencheng;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import cn.bupt.wfshop.ui.recycler.DataConverter;
import cn.bupt.wfshop.ui.recycler.MultipleItemEntity;

/**
 * Created by tobyli
 */

public class FenchengListDataConverter extends DataConverter {

    @Override
    public ArrayList<MultipleItemEntity> convert() {

        //        final JSONArray array = JSON.parseObject(getJsonData()).getJSONObject("obj").getJSONArray("rows");
        final JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("data");
        //        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final int size = array.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = array.getJSONObject(i);
            //            final String date = myFmt.format(new Date(data.getLong("ordertime")));
            final String date = data.getString("date");
            //            final String username = data.getJSONObject("businessOrder").getJSONObject("wechatAccount").getString("wechatName");
            final String username = data.getString("username");
            final String product = data.getString("product");
            final String shop = data.getString("shop");
            //            final double total_price = data.getDouble("totalAmount");
            final double total_price = data.getDouble("total_price");
            //            final double fencheng_price = data.getDouble("weBusinessAmount");
            final double fencheng_price = data.getDouble("fencheng_price");

            final MultipleItemEntity entity = MultipleItemEntity.builder()
                    .setItemType(FenchengListItemType.ITEM_FENCHENGT_LIST)
                    .setField(FenchengItemFields.DATE, date)
                    .setField(FenchengItemFields.USERNAME, username)
                    .setField(FenchengItemFields.PRODUCT, product)
                    .setField(FenchengItemFields.SHOP, shop)
                    .setField(FenchengItemFields.TOTAL_PRICE, total_price)
                    .setField(FenchengItemFields.FENCHENG_PRICE, fencheng_price)
                    .build();

            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
