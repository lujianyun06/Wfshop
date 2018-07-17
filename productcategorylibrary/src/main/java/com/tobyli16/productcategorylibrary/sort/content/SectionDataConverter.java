package com.tobyli16.productcategorylibrary.sort.content;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tobyli
 */

public class SectionDataConverter {

    final List<SectionBean> convert(String json) {
        final List<SectionBean> dataList = new ArrayList<>();
        //        final JSONArray dataArray = JSON.parseObject(json).getJSONArray("obj");
        final JSONArray dataArray = JSON.parseObject(json).getJSONArray("data");

        final int size = dataArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = dataArray.getJSONObject(i);
            final int id = data.getInteger("id");
            //            final String title = "标题";
            final String title = data.getString("section");

            //添加title
            final SectionBean sectionTitleBean = new SectionBean(true, title);
            sectionTitleBean.setId(id);
            //            sectionTitleBean.setIsMore(false);
            sectionTitleBean.setIsMore(true);
            dataList.add(sectionTitleBean);

            final JSONArray goods = data.getJSONArray("goods");
            //商品内容循环
            final int goodSize = goods.size();
            for (int j = 0; j < goodSize; j++) {
                final JSONObject contentItem = goods.getJSONObject(j);
                final int goodsId = contentItem.getInteger("goods_id");
                final String goodsName = contentItem.getString("goods_name");
                final String goodsThumb = contentItem.getString("goods_thumb");
                //获取内容
                final SectionContentItemEntity itemEntity = new SectionContentItemEntity();
                itemEntity.setGoodsId(goodsId);
                itemEntity.setGoodsName(goodsName);
                itemEntity.setGoodsThumb(goodsThumb);
                //添加内容
                dataList.add(new SectionBean(itemEntity));
            }
//            final int goodsId = data.getInteger("id");
//            final String goodsName = data.getString("name");
//            final String goodsThumb = data.getString("iconUrl");
//            //获取内容
//            final SectionContentItemEntity itemEntity = new SectionContentItemEntity();
//            itemEntity.setGoodsId(goodsId);
//            itemEntity.setGoodsName(goodsName);
//            itemEntity.setGoodsThumb(goodsThumb);
//            //添加内容
//            dataList.add(new SectionBean(itemEntity));
            //            //商品内容循环结束
        }
        //Section循环结束

        return dataList;
    }
}
