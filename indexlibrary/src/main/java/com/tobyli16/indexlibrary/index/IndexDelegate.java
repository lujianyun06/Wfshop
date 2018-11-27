package com.tobyli16.indexlibrary.index;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.joanzapata.iconify.widget.IconTextView;
import com.tobyli16.indexlibrary.R;
import com.tobyli16.indexlibrary.index.refresh.RefreshHandler;
import com.tobyli16.indexlibrary.index.search.SearchDelegate;

import cn.bupt.wfshop.app.ConfigKeys;
import cn.bupt.wfshop.app.Wangfu;
import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.delegates.bottom.BottomItemDelegate;
import cn.bupt.wfshop.net.URLManager;
import cn.bupt.wfshop.ui.recycler.BaseDecoration;
import cn.bupt.wfshop.util.callback.CallbackManager;
import cn.bupt.wfshop.util.callback.CallbackType;
import cn.bupt.wfshop.util.callback.IGlobalCallback;

/**
 * Created by tobyli
 */

public class IndexDelegate extends BottomItemDelegate {

    private RecyclerView mRecyclerView = null;
    private SwipeRefreshLayout mRefreshLayout = null;

    private RefreshHandler mRefreshHandler = null;

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mRecyclerView = $(R.id.rv_index);
        mRefreshLayout = $(R.id.srl_index);

        final IconTextView mIconScan = $(R.id.icon_index_scan);
        final AppCompatEditText mSearchView = $(R.id.et_search_view);

        $(R.id.icon_index_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanWithCheck(getParentDelegate());
            }
        });



        //创建刷新handler
        mRefreshHandler = RefreshHandler.create(mRefreshLayout, mRecyclerView, new IndexDataConverter());

        //处理二维码后的回调，需要注意的是，这些代码是一个初始化的过程，回调先不会使用，但会将这个事务加入callback队列，，当扫描二维码完成后
        //会在ScannerDelegate中调用executeCallback
        CallbackManager.getInstance()
                .addCallback(CallbackType.ON_SCAN, new IGlobalCallback() {
                            @Override
                            public void executeCallback(@Nullable Object args) {

                                String url = (String)args;
                                if (url!=null && url.startsWith("/productDetail/productId/")){
                                    String goodsId = url.substring(25,url.length());
                                    Fragment fragment = (Fragment) ARouter.getInstance().build("/productDetail/productId/").withString("goodsId",goodsId).navigation();
                                    if (fragment != null) {
                                        getParentDelegate().getSupportDelegate().startWithPop((WangfuDelegate)fragment);
                                    }
                                } else {
                                    Toast.makeText(getContext(), "得到的二维码是" + args, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                );
        //当搜索栏获得焦点时，弹出搜索页面
        mSearchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    getParentDelegate().start(new SearchDelegate());
                } else {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(),0);
                    }
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mRefreshLayout.setProgressViewOffset(true, 120, 300);
    }

    private void initRecyclerView() {
//        int spanCount;
//        try{
//            spanCount = Wangfu.getConfiguration(ConfigKeys.SPAN_COUNT);
//        } catch (NullPointerException e) {
//            spanCount = 120;
//        }
//        final GridLayoutManager manager = new GridLayoutManager(getContext(), spanCount);
        final Context context = getContext();
        //TODO 这句代码中的 stopscroll 导致下拉刷新球无法被拉出,并且swipelayout和recycleView共同使用可能会导致滑动冲突
//        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        if (context != null) {
            mRecyclerView.addItemDecoration
                    (BaseDecoration.create(ContextCompat.getColor(context, R.color.app_background), 5));
        }
        mRecyclerView.addOnItemTouchListener(IndexItemClickListener.create(this.getParentDelegate()));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        initRecyclerView();
        mRefreshHandler.firstPage(URLManager.HOME_PAGE);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_index;
    }
}
