package com.tobyli16.productdetaillibrary.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.tobyli16.productdetaillibrary.R;

import cn.bupt.wfshop.delegates.WangfuDelegate;

/**
 * Created by tobyli
 */

public class GoodsInfoDelegate extends WangfuDelegate {

    private static final String ARG_GOODS_DATA = "ARG_GOODS_DATA";
    private JSONObject mData = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_goods_info;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        final String goodsData;
        if (args != null) {
            goodsData = args.getString(ARG_GOODS_DATA);
            mData = JSON.parseObject(goodsData);
        }
    }

    public static GoodsInfoDelegate create(String goodsInfo) {
        final Bundle args = new Bundle();
        args.putString(ARG_GOODS_DATA, goodsInfo);
        final GoodsInfoDelegate delegate = new GoodsInfoDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        final AppCompatTextView goodsInfoTitle = $(R.id.tv_goods_info_title);
        final AppCompatTextView goodsInfoDesc = $(R.id.tv_goods_info_desc);
        final AppCompatTextView goodsInfoPrice = $(R.id.tv_goods_info_price);

        final ImageView shopUrl = $(R.id.iv_shop_img);
        final AppCompatTextView shopName = $(R.id.tv_shop_name);
        final AppCompatTextView shopSubName = $(R.id.tv_shop_subname);

        //        final String name = mData.getJSONObject("specialty").getString("name");
        //        final String desc = mData.getJSONObject("specialty").getString("descriptions");
        //        final double price = mData.getDouble("mPrice");
        final String name = mData.getString("name");
        final String desc = mData.getString("sub_title");
        final double price = mData.getDouble("price");
        goodsInfoTitle.setText(name);
        goodsInfoDesc.setText(desc);
        goodsInfoPrice.setText(String.valueOf(price));

        //        final JSONObject shopObject = mData.getJSONObject("specialty").getJSONObject("provider");
        //        String shopNameStr = shopObject.getString("providerName");
        //        String shopSubNameStr = shopObject.getString("introduction");
        //        String shopUrlStr = "";

        final JSONObject shopObject = mData.getJSONObject("shop");
        String shopNameStr = shopObject.getString("shop_name");
        String shopSubNameStr = shopObject.getString("sub_title");
        String shopUrlStr = shopObject.getString("cover_img");

        Glide.with(getContext()).load(shopUrlStr).into(shopUrl);
        shopName.setText(shopNameStr);
        shopSubName.setText(shopSubNameStr);
    }
}
