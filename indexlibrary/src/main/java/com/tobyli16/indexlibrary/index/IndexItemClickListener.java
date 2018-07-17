package com.tobyli16.indexlibrary.index;

import android.support.v4.app.Fragment;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.ui.recycler.MultipleFields;
import cn.bupt.wfshop.ui.recycler.MultipleItemEntity;

/**
 * Created by tobyli
 */

public class IndexItemClickListener extends SimpleClickListener {

    private final WangfuDelegate DELEGATE;

    private IndexItemClickListener(WangfuDelegate delegate) {
        this.DELEGATE = delegate;
    }

    public static SimpleClickListener create(WangfuDelegate delegate) {
        return new IndexItemClickListener(delegate);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final MultipleItemEntity entity = (MultipleItemEntity) baseQuickAdapter.getData().get(position);
        final int goodsId = entity.getField(MultipleFields.ID);
//        final GoodsDetailDelegate delegate = GoodsDetailDelegate.create(goodsId);
        Fragment fragment = (Fragment) ARouter.getInstance().build("/productDetail/productId/").withString("goodsId",String.valueOf(goodsId)).navigation();
        if (fragment != null) {
            DELEGATE.getSupportDelegate().start((WangfuDelegate)fragment);
        }
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
