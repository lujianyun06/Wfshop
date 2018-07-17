package com.tobyli16.profilelibrary.personal.profile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tobyli16.profilelibrary.R;
import com.tobyli16.profilelibrary.personal.list.ListAdapter;
import com.tobyli16.profilelibrary.personal.list.ListBean;
import com.tobyli16.profilelibrary.personal.list.ListItemType;
import com.tobyli16.profilelibrary.personal.settings.NameDelegate;
import com.tobyli16.profilelibrary.personal.sign.AccountManager;
import com.tobyli16.profilelibrary.personal.sign.ISignListener;
import com.tobyli16.profilelibrary.personal.sign.SignHandler;
import com.tobyli16.profilelibrary.personal.sign.SignInDelegate;

import java.util.ArrayList;
import java.util.List;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.util.storage.WangfuPreference;

/**
 * Created by tobyli
 */

public class UserProfileDelegate extends WangfuDelegate {

    @Override
    public Object setLayout() {
        return R.layout.delegate_user_profile;
    }

    private ISignListener mISignListener = null;

    public void setmISignListener(ISignListener mISignListener) {
        this.mISignListener = mISignListener;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ISignListener) {
            mISignListener = (ISignListener) activity;
        }
    }

    public static UserProfileDelegate create(ISignListener iSignListener) {
        UserProfileDelegate userProfileDelegate = new UserProfileDelegate();
        userProfileDelegate.setmISignListener(iSignListener);
        return userProfileDelegate;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        final RecyclerView recyclerView = $(R.id.rv_user_profile);
        String headImgUrl = WangfuPreference.getCustomAppProfile("headimgurl");

        final ListBean image = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_AVATAR)
                .setId(1)
                .setImageUrl(headImgUrl, "http://i9.qhimg.com/t017d891ca365ef60b5.jpg")
                .build();

        final ListBean name = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(2)
                .setText("姓名")
                .setDelegate(new NameDelegate())
                .setValue("未设置姓名")
                .build();

        final ListBean gender = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(3)
                .setText("性别")
                .setValue("未设置性别")
                .build();

        final ListBean birth = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(4)
                .setText("生日")
                .setValue("未设置生日")
                .build();

        final List<ListBean> data = new ArrayList<>();
        data.add(image);
        data.add(name);
        data.add(gender);
        data.add(birth);

        //设置RecyclerView
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new UserProfileClickListener(this));

        $(R.id.btn_sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignHandler.onSignOut(mISignListener);
                getSupportDelegate().pop();
//                getSupportDelegate().startWithPop(new SignInDelegate());
            }
        }); {

        }
    }
}
