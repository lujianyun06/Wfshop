package com.tobyli16.profilelibrary.personal.othersettings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tobyli16.profilelibrary.R;
import com.tobyli16.profilelibrary.personal.PersonalClickListener;
import com.tobyli16.profilelibrary.personal.address.AddressDelegate;
import com.tobyli16.profilelibrary.personal.list.ListAdapter;
import com.tobyli16.profilelibrary.personal.list.ListBean;
import com.tobyli16.profilelibrary.personal.list.ListItemType;
import com.tobyli16.profilelibrary.personal.settings.SettingsDelegate;

import java.util.ArrayList;
import java.util.List;

import cn.bupt.wfshop.delegates.BaseDelegate;
import cn.bupt.wfshop.delegates.WangfuDelegate;

public class OtherSettingDelegate extends WangfuDelegate {

    RecyclerView rvSettings = null;
    List<ListBean> data = new ArrayList<>();

    @Override
    public Object setLayout() {
        return R.layout.delegate_othersetting;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        rvSettings = $(R.id.rv_othersetting);

        //设置RecyclerView
        initListBean();
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvSettings.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        rvSettings.setAdapter(adapter);
        rvSettings.addOnItemTouchListener(new PersonalClickListener(this));
    }

    private void initListBean(){

        final ListBean address = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(1)
                .setDelegate(new AddressDelegate())
                .setText("收货地址")
                .build();

        final ListBean system = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(2)
                .setDelegate(new SettingsDelegate())
                .setText("系统设置")
                .build();
        data.add(address);
        data.add(system);
    }
}
