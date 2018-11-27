package com.tobyli16.indexlibrary.index.search;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;
import com.tobyli16.indexlibrary.R;
import com.tobyli16.indexlibrary.index.IndexEvent.IndexEventTag;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.delegates.web.event.Event;
import cn.bupt.wfshop.event.WFEvent;
import cn.bupt.wfshop.net.RestClient;
import cn.bupt.wfshop.net.callback.IError;
import cn.bupt.wfshop.net.callback.ISuccess;
import cn.bupt.wfshop.ui.recycler.MultipleFields;
import cn.bupt.wfshop.ui.recycler.MultipleItemEntity;
import cn.bupt.wfshop.util.storage.WangfuPreference;

/**
 * Created by tobyli
 */

public class SearchDelegate extends WangfuDelegate {

    private AppCompatEditText mSearchEdit = null;

    void onClickSearch() {
        //目前搜索只能搜索到手机，因为没有后台只有手机的json-deom
        final String searchKeyWord = mSearchEdit.getText().toString();
        saveItem(searchKeyWord);
        SearchProductDelegate searchProductDelegate = SearchProductDelegate.create(searchKeyWord);
        getSupportDelegate().start(searchProductDelegate);
        Handler handler = new Handler();
        /*按理说搜索应该只有一次，就算关键字为空，则应该在服务端处理这种情况（如直接获取数据库的前N项返回），对于客户端不应该有区别。
        *
         */
//        RestClient.builder()
//                .url(URLManager.SEARCH_URL + "?key=" + )
//                .loader(getContext())
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//                        final String searchItemText = mSearchEdit.getText().toString();
//                        saveItem(searchItemText);
//
//
//                        SearchProductDelegate searchProductDelegate = SearchProductDelegate.create(searchItemText);
//                        getSupportDelegate().startWithPop(searchProductDelegate);  //这里为什么要用pop？
//                        EventBus.getDefault().post(new WFEvent(IndexEventTag.SEARCH_OK).addData("response", response)
//                                .addData("keyword", searchKeyWord));
//                    }
//                })
//                .error(new IError() {
//                    @Override
//                    public void onError(int code, String msg) {
//                        Toast.makeText(getContext(), "搜索失败" + " code：" + code + "\nmsg: " + msg, Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .build()
//                .get();
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
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);
                final String history = entity.getField(MultipleFields.TEXT);
                mSearchEdit.setText(history);
            }
        });
        recyclerView.setAdapter(adapter);
        initItemDecoration(recyclerView);
    }

    private void initItemDecoration(RecyclerView recyclerView){
        //列表每一项的分割线
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
