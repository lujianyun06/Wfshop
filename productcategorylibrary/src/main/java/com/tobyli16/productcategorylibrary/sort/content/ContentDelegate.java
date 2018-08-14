package com.tobyli16.productcategorylibrary.sort.content;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.tobyli16.productcategorylibrary.R;
import com.tobyli16.productcategorylibrary.sort.list.VerticalListDataConverter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.net.RestClient;
import cn.bupt.wfshop.net.callback.ISuccess;

/**
 * Created by tobyli
 */

public class ContentDelegate extends WangfuDelegate {

    private static final String ARG_CONTENT_ID = "CONTENT_ID";
    private int mContentId = -1;
    private List<SectionBean> mData = null;

    private RecyclerView mRecyclerView = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mContentId = args.getInt(ARG_CONTENT_ID);
        }
    }

    public static ContentDelegate newInstance(int contentId) {
        final Bundle args = new Bundle();
        args.putInt(ARG_CONTENT_ID, contentId);
        final ContentDelegate delegate = new ContentDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_list_content;
    }

    private void initData() {
        //本来是应该再发一次请求的，但demo做的是一次请求吧所有数据都传过来,所以在verticalConvert中做了处理，吧每类物品的json
        //数据放进静态变量中，直接从那边获取
//        RestClient.builder()
////                .url("http://admin.swczyc.com/hyapi/ymmall/product/category/sub_categories?category_id=" + mContentId)
//                                .url("sort_content_list.php?contentId=" + mContentId)
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//                        if (response.contains("失败")) {
//                            return;
//                        }
//                        mData = new SectionDataConverter().convert(response);
//                        final SectionAdapter sectionAdapter =
//                                new SectionAdapter(R.layout.item_section_content,
//                                        R.layout.item_section_header, mData);
//                        mRecyclerView.setAdapter(sectionAdapter);
//                    }
//                })
//                .build()
//                .get();
        ArrayList<JSONObject> jsonObjects =  VerticalListDataConverter.getCategoryJsonData();
        if (mContentId < jsonObjects.size()){
            JSONObject data = VerticalListDataConverter.getCategoryJsonData().get(mContentId);
            mData = new SectionDataConverter().convert(data.toJSONString());
            final SectionAdapter sectionAdapter = new SectionAdapter(R.layout.item_section_content,
                                        R.layout.item_section_header, mData);
            mRecyclerView.setAdapter(sectionAdapter);
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = $(R.id.rv_list_content);
        final StaggeredGridLayoutManager manager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        initData();
    }
}
