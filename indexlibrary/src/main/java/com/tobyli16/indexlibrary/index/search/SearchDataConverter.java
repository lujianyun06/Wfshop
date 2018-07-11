package com.tobyli16.indexlibrary.index.search;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;

import cn.bupt.wfshop.ui.recycler.DataConverter;
import cn.bupt.wfshop.ui.recycler.MultipleFields;
import cn.bupt.wfshop.ui.recycler.MultipleItemEntity;
import cn.bupt.wfshop.util.storage.WangfuPreference;

/**
 * Created by tobyli
 */

public class SearchDataConverter extends DataConverter {

    public static final String TAG_SEARCH_HISTORY = "search_history";

    @Override
    public ArrayList<MultipleItemEntity> convert() {
        final String jsonStr =
                WangfuPreference.getCustomAppProfile(TAG_SEARCH_HISTORY);
        if (!jsonStr.equals("")) {
            final JSONArray array = JSONArray.parseArray(jsonStr);
            final int size = array.size();
            for (int i = 0; i < size; i++) {
                final String historyItemText = array.getString(i);
                final MultipleItemEntity entity = MultipleItemEntity.builder()
                        .setItemType(SearchItemType.ITEM_SEARCH)
                        .setField(MultipleFields.TEXT, historyItemText)
                        .build();
                ENTITIES.add(entity);
            }
        }

        return ENTITIES;
    }
}
