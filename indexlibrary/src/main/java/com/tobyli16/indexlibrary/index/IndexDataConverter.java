package com.tobyli16.indexlibrary.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tobyli16.indexlibrary.index.bean.BannerBean;
import com.tobyli16.indexlibrary.index.bean.GridViewBean;
import com.tobyli16.indexlibrary.index.bean.HomePageObj;
import com.tobyli16.indexlibrary.index.bean.HomePageObjFactory;
import com.tobyli16.indexlibrary.index.bean.SeparatorViewBean;
import com.tobyli16.indexlibrary.index.refresh.HomeDataConvert;
import com.tobyli16.indexlibrary.index.refresh.HomeMultiItemEntity;

import java.util.ArrayList;

/**
 * Created by tobyli
 */
//将json数据提取然后构造一个商品实体（包含商品的所有信息）把所有商品实体放入数组中
public final class IndexDataConverter extends HomeDataConvert {
    @Override
    public ArrayList<HomeMultiItemEntity> convert() {
        final JSONArray dataArray = ((JSONObject) JSON.parseObject(getJsonData())
                .getJSONObject("data")).getJSONArray("rows");
        int len = dataArray.size();
        for (int i = 0; i < len; i++) {


            BannerBean bannerBean = null;
            GridViewBean gridViewBean  = null;
            SeparatorViewBean separatorViewBean = null;
            int itemType = 1;

            HomePageObj homePageObj = HomePageObjFactory.createHomePageObj(dataArray.getJSONObject(i));
            Object dataObj = homePageObj.data;
            if (dataObj instanceof BannerBean) {
                itemType = HomeMultiItemEntity.BANNER;
            } else if (dataObj instanceof GridViewBean) {
                itemType = HomeMultiItemEntity.GRID;
            } else if (dataObj instanceof SeparatorViewBean) {
                itemType = HomeMultiItemEntity.SEPARATOR;
            }

            HomeMultiItemEntity item = new HomeMultiItemEntity(itemType);
            item.setDataBean(dataObj);


            ENTITIES.add(item);

        }


        return ENTITIES;
    }


    //discard
//    @Override
//    public ArrayList<MultipleItemEntity> convert() {
//        final JSONArray dataArray = JSON.parseObject(getJsonData()).getJSONArray("data");
//        final int size = dataArray.size();
//        for (int i = 0; i < size; i++) {
//            final JSONObject data = dataArray.getJSONObject(i);
//
//            final String imageUrl = data.getString("imageUrl");
//            final String text = data.getString("text");
//            final int spanSize = data.getInteger("spanSize");
//            final int id = data.getInteger("goodsId");
//            final JSONArray banners = data.getJSONArray("banners");
//
//            final String isSperateImage = data.getString("isSeperatePic");
//            final String isIcon = data.getString("isIcon");
//
//            final ArrayList<String> bannerImages = new ArrayList<>();
//            int type = 0;
//            if (imageUrl == null && text != null) {
//                type = ItemType.TEXT;
//            } else if (imageUrl != null && text == null) {
//                type = ItemType.IMAGE;
//            } else if (imageUrl != null) {
//                type = ItemType.TEXT_IMAGE;
//            } else if (banners != null) {
//                type = ItemType.BANNER;
//                //Banner的初始化
//                final int bannerSize = banners.size();
//                for (int j = 0; j < bannerSize; j++) {
//                    final String banner = banners.getString(j);
//                    bannerImages.add(banner);
//                }
//            }
//
//            if (isSperateImage != null) {
//                type = ItemType.INDEX_SPERATE_IMAGE;
//            }
//
//            if (isIcon != null) {
//                type = ItemType.INDEX_ICON_IMAGE;
//            }
//
//            final MultipleItemEntity entity = MultipleItemEntity.builder()
//                    .setField(MultipleFields.ITEM_TYPE,type)
//                    .setField(MultipleFields.SPAN_SIZE,spanSize)
//                    .setField(MultipleFields.ID,id)
//                    .setField(MultipleFields.TEXT,text)
//                    .setField(MultipleFields.IMAGE_URL,imageUrl)
//                    .setField(MultipleFields.BANNERS,bannerImages)
//                    .build();
//
//            ENTITIES.add(entity);
//
//        }
//
//        return ENTITIES;
//    }
}
