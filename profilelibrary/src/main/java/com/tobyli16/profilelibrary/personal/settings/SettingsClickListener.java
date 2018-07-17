package com.tobyli16.profilelibrary.personal.settings;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import cn.bupt.wfshop.delegates.WangfuDelegate;
import com.tobyli16.profilelibrary.personal.list.ListBean;

/**
 * Created by tobyli
 */

public class SettingsClickListener extends SimpleClickListener {

    private final WangfuDelegate DELEGATE;

    public SettingsClickListener(WangfuDelegate delegate) {
        this.DELEGATE = delegate;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final ListBean bean = (ListBean) baseQuickAdapter.getData().get(position);
        int id = bean.getId();
        switch (id) {
            case 1:
                //这是消息推送的开关
                break;
            case 2:
                DELEGATE.getSupportDelegate().start(bean.getDelegate());
                break;
            case 3:
                DELEGATE.getSupportDelegate().start(bean.getDelegate());
            default:
                break;
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
