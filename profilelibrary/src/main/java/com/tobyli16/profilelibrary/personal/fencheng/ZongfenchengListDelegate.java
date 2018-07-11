package com.tobyli16.profilelibrary.personal.fencheng;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tobyli16.profilelibrary.R;

import java.util.List;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.net.RestClient;
import cn.bupt.wfshop.net.callback.ISuccess;
import cn.bupt.wfshop.ui.recycler.MultipleItemEntity;

/**
 * Created by tobyli
 */

public class ZongfenchengListDelegate extends WangfuDelegate {

    private String mType = null;

    private RecyclerView mRecyclerView = null;
    private AppCompatTextView mTvTotalPrice = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_zongfencheng;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = $(R.id.rv_zongfencheng);
        mTvTotalPrice = $(R.id.tv_todayfencheng_amount);

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClient.builder()
                .loader(getContext())
//                .url("http://tobyli16.com/rifencheng_list.php")
                .url("http://tobyli16.com:8080/wfshop/fencheng/total")
                .success(new ISuccess() {
                    public void onSuccess(String response) {
                        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        mRecyclerView.setLayoutManager(manager);
                        final List<MultipleItemEntity> data =
                                new FenchengListDataConverter().setJsonData(response).convert();
                        final FenchengListAdapter adapter = new FenchengListAdapter(data);
                        mRecyclerView.setAdapter(adapter);
                        mTvTotalPrice.setText("￥"+adapter.getTotalFenchengPrice());
                    }
                })
                .build()
                .get();
    }
}
