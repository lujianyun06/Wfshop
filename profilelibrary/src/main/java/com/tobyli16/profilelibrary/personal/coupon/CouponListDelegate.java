package com.tobyli16.profilelibrary.personal.coupon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tobyli16.profilelibrary.R;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.net.RestClient;
import cn.bupt.wfshop.net.callback.ISuccess;

/**
 * Created by tobyli
 */

public class CouponListDelegate extends WangfuDelegate {

    private String mType = null;

    private RecyclerView unusedRecyclerView = null;
    private RecyclerView usedRecyclerView = null;
    private RecyclerView expireRecyclerView = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_coupon;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        unusedRecyclerView = $(R.id.rv_discount_unused);
        usedRecyclerView = $(R.id.rv_discount_used);
        expireRecyclerView = $(R.id.rv_discount_expire);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClient.builder()
                .loader(getContext())
                .url("tobyli16.com:8080/wfshop/coupon")
                .success(new ISuccess() {
                    public void onSuccess(String response) {
                        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
//                        mRecyclerView.setLayoutManager(manager);
//                        final List<MultipleItemEntity> data =
//                                new FenchengListDataConverter().setJsonData(response).convert();
//                        final FenchengListAdapter adapter = new FenchengListAdapter(data);
//                        mRecyclerView.setAdapter(adapter);
//                        mTvTotalPrice.setText("￥"+adapter.getTotalFenchengPrice());
                    }
                })
                .build()
                .get();
    }
}
