package com.tobyli16.productdetaillibrary.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

/**
 * Created by tobyli
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<String> TAB_TITLES = new ArrayList<>();
    private final ArrayList<String> PICTURES = new ArrayList<>();
    private final ArrayList<ArrayList<String>> USER_COMMENTS = new ArrayList<>();

    public TabPagerAdapter(FragmentManager fm, JSONObject data, JSONArray data2) {
        super(fm);
        //获取tabs信息，注意，这里的tabs是一条信息
        TAB_TITLES.add("商品详情");
        TAB_TITLES.add("商品评价");

        final JSONArray intros = data.getJSONArray("intro_imgs");
        final int size = intros.size();
        for (int i = 0; i < size; i++) {
            final String url = intros.getJSONObject(i).getString("img_url");
            PICTURES.add(url);
        }

        ArrayList<String> USER_PICTURES = new ArrayList<>();
        ArrayList<String> BUYER_NAME = new ArrayList<>();
        ArrayList<String> USER_COMMENT = new ArrayList<>();
        ArrayList<String> STARS = new ArrayList<>();
        for (int i = 0; i < data2.size(); i++) {
            JSONObject buyerInfo = data2.getJSONObject(i).getJSONObject("buyer");
//            BUYER_NAME.add(data2.getJSONObject(i).getString("wechatName"));
            BUYER_NAME.add(buyerInfo.getString("buyer_name"));
            USER_PICTURES.add(buyerInfo.getString("avatar_url"));
//            USER_COMMENT.add(data2.getJSONObject(i).getString("appraiseContent"));
            USER_COMMENT.add(data2.getJSONObject(i).getString("comment"));
            STARS.add(data2.getJSONObject(i).getInteger("rating")+"星");
        }

        USER_COMMENTS.add(USER_PICTURES);
        USER_COMMENTS.add(BUYER_NAME);
        USER_COMMENTS.add(USER_COMMENT);
        USER_COMMENTS.add(STARS);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ImageDelegate.create(PICTURES);
        } else if (position == 1) {
            return CommentDelegate.create(USER_COMMENTS);
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_TITLES.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES.get(position);
    }
}
