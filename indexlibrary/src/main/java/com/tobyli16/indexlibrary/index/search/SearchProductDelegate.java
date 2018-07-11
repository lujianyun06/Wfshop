package com.tobyli16.indexlibrary.index.search;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;
import com.tobyli16.indexlibrary.R;

import java.util.List;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.net.RestClient;
import cn.bupt.wfshop.net.callback.IError;
import cn.bupt.wfshop.net.callback.IFailure;
import cn.bupt.wfshop.net.callback.ISuccess;
import cn.bupt.wfshop.ui.recycler.MultipleItemEntity;

/**
 * Created by tobyli
 */
@Route(path = "/search/search/")
public class SearchProductDelegate extends WangfuDelegate {

    private AppCompatEditText mSearchEdit = null;
    private String searchWord = "a";
    private RecyclerView mRecyclerView;

    @Autowired
    String getSearchWord;

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ARouter.getInstance().inject(this);
        Bundle args = getArguments();
        if (args != null) {
            searchWord = args.getString("search_key");
        }
        if (getSearchWord!=null) {
            searchWord = getSearchWord;
        }

        mRecyclerView = $(R.id.rv_search_product_list);
        mSearchEdit = $(R.id.et_search_view);
        mSearchEdit.setText(searchWord);

        $(R.id.et_search_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportDelegate().startWithPop(new SearchDelegate());
            }
        });

        $(R.id.icon_top_search_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportDelegate().pop();
            }
        });
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_search_product_list;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
//        String url = "http://admin.swczyc.com/hyapi/ymmall/product/search?specialty_name="+searchWord;
        String url = "https://wfshop.andysheng.cn/search?key="+searchWord;
        RestClient.builder()
                .loader(getContext())
                .url(url)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
                        mRecyclerView.setLayoutManager(manager);

                        SearchProductDataConverter.searchWord = searchWord;
                        final List<MultipleItemEntity> data =
                                new SearchProductDataConverter().setJsonData(response).convert();
                        final SearchProductAdapter adapter = new SearchProductAdapter(data);
                        mRecyclerView.setAdapter(adapter);
                        mRecyclerView.addOnItemTouchListener(new SearchProductListener(SearchProductDelegate.this));
//                        mRecyclerView.addOnItemTouchListener(
//                                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
//                                    @Override public void onItemClick(View view, int position) {
//                                        // do whatever
//                                    }
//                                })
//                        );
                        //分割线难道每次都会改变吗？所以需要在这里绘制？
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

                        mRecyclerView.addItemDecoration(itemDecoration);
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        System.out.println("SearchProductDelegate：load failed");
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        System.out.println(code+msg);
                    }
                })
                .build()
                .get();
    }

    public static SearchProductDelegate create(String search_key) {
        Bundle args = new Bundle();
        args.putString("search_key",search_key);
        final SearchProductDelegate delegate = new SearchProductDelegate();
        delegate.setArguments(args);
        return delegate;
    }
}
