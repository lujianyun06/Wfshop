package com.tobyli16.indexlibrary.index.search;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.StringUtils;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;
import com.tobyli16.indexlibrary.R;

import java.util.ArrayList;
import java.util.List;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.net.RestClient;
import cn.bupt.wfshop.net.callback.IError;
import cn.bupt.wfshop.net.callback.ISuccess;
import cn.bupt.wfshop.ui.recycler.MultipleItemEntity;
import cn.bupt.wfshop.util.storage.WangfuPreference;

/**
 * Created by tobyli
 */

public class SearchDelegate extends WangfuDelegate {

    private AppCompatEditText mSearchEdit = null;

    void onClickSearch() {
        RestClient.builder()
                .url("search.php?key=")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final String searchItemText = mSearchEdit.getText().toString();
                        saveItem(searchItemText);
                        mSearchEdit.setText("");
                        Toast.makeText(getContext(), "搜索的是 " + searchItemText, Toast.LENGTH_LONG).show();
                        //展示一些东西
                        //弹出一段话
                        SearchProductDelegate searchProductDelegate = SearchProductDelegate.create(searchItemText);
                        getSupportDelegate().startWithPop(searchProductDelegate);  //这里为什么要用pop？
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(getContext(), "搜索失败" + " code：" + code + "\nmsg: " + msg, Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .get();
    }

    @SuppressWarnings("unchecked") //保存搜索记录，历史记录是一次全读，然后全写，先把以前的字符记录转成数组，然后加入新值，然后再转成字符存储起来
    private void saveItem(String item) {
        if (!StringUtils.isEmpty(item) && !StringUtils.isSpace(item)) {
            List<String> history;
            final String historyStr =
                    WangfuPreference.getCustomAppProfile(SearchDataConverter.TAG_SEARCH_HISTORY);
            if (StringUtils.isEmpty(historyStr)) {
                history = new ArrayList<>();
            } else {
                history = JSON.parseObject(historyStr, ArrayList.class);
            }
            history.add(item);
            final String json = JSON.toJSONString(history);

            WangfuPreference.addCustomAppProfile(SearchDataConverter.TAG_SEARCH_HISTORY, json);
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        final RecyclerView recyclerView = $(R.id.rv_search);
        mSearchEdit = $(R.id.et_search_view);

        $(R.id.tv_top_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSearch();
            }
        });

        $(R.id.icon_top_search_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportDelegate().pop();
            }
        });

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        final List<MultipleItemEntity> data = new SearchDataConverter().convert();
        final SearchAdapter adapter = new SearchAdapter(data);
        recyclerView.setAdapter(adapter);

        final DividerItemDecoration itemDecoration = new DividerItemDecoration();
        itemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {
            @Override
            public Divider getVerticalDivider(int position) {
                return null;
            }

            @Override
            public Divider getHorizontalDivider(int position) {
                return new Divider.Builder()
                        .size(2)
                        .margin(20, 20)
                        .color(Color.GRAY)
                        .build();
            }
        });

        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_search;
    }


}
