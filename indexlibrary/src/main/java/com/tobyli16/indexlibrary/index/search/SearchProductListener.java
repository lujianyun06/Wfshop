package com.tobyli16.indexlibrary.index.search;

import android.support.v4.app.Fragment;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.ui.recycler.MultipleFields;
import cn.bupt.wfshop.ui.recycler.MultipleItemEntity;

/**
 * Created by Toby on 2018/3/15 0015.
 */

public class SearchProductListener extends SimpleClickListener {

    private final WangfuDelegate DELEGATE;

    public SearchProductListener(WangfuDelegate delegate) {
        this.DELEGATE = delegate;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MultipleItemEntity multipleItemEntity = (MultipleItemEntity) adapter.getData().get(position);
        int id = multipleItemEntity.getField(MultipleFields.ID);
        Fragment fragment = (Fragment) ARouter.getInstance().build("/productDetail/productId/").withString("goodsId",String.valueOf(id)).navigation();
        DELEGATE.getSupportDelegate().start((WangfuDelegate)fragment);
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
