package com.tobyli16.profilelibrary.personal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tobyli16.profilelibrary.R;
import com.tobyli16.profilelibrary.personal.address.AddressDelegate;
import com.tobyli16.profilelibrary.personal.fencheng.DaifenchengListDelegate;
import com.tobyli16.profilelibrary.personal.fencheng.RifenchengListDelegate;
import com.tobyli16.profilelibrary.personal.fencheng.ZongfenchengListDelegate;
import com.tobyli16.profilelibrary.personal.list.ListAdapter;
import com.tobyli16.profilelibrary.personal.list.ListBean;
import com.tobyli16.profilelibrary.personal.list.ListItemType;
import com.tobyli16.profilelibrary.personal.order.OrderListDelegate;
import com.tobyli16.profilelibrary.personal.othersettings.OtherSettingDelegate;
import com.tobyli16.profilelibrary.personal.profile.UserProfileDelegate;
import com.tobyli16.profilelibrary.personal.settings.SettingsDelegate;
import com.tobyli16.profilelibrary.personal.sign.AccountManager;
import com.tobyli16.profilelibrary.personal.sign.ISignListener;
import com.tobyli16.profilelibrary.personal.sign.SignInDelegate;

import java.util.ArrayList;
import java.util.List;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.delegates.bottom.BottomItemDelegate;
import cn.bupt.wfshop.util.storage.WangfuPreference;

/**
 * Created by tobyli
 */

public class PersonalDelegate extends BottomItemDelegate implements ISignListener {

    public static final String ORDER_TYPE = "ORDER_TYPE";
    private Bundle mArgs = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_personal;
    }

    private void onClickAllOrder() {
        mArgs.putString(ORDER_TYPE, "all");
        startOrderListByType();
    }

    private void onClickAvatar() {
        getParentDelegate().getSupportDelegate().start(UserProfileDelegate.create(this));
    }

    private void onClickAvatarSignIn() {
        getParentDelegate().getSupportDelegate().start(SignInDelegate.create(this));
    }

    private void startOrderListByType() {
        final OrderListDelegate delegate = new OrderListDelegate();
        delegate.setArguments(mArgs);
        getParentDelegate().getSupportDelegate().start(delegate);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = new Bundle();
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        initAvatarAndNickname();


        $(R.id.tv_all_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAllOrder();
            }
        });
        $(R.id.img_user_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WangfuPreference.getCustomAppProfile("headimgurl").length() == 0) {
                    onClickAvatarSignIn();
                } else {
                    onClickAvatar();
                }
            }
        });
        $(R.id.tv_rifencheng_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentDelegate().getSupportDelegate().start(new RifenchengListDelegate());
            }
        });
        $(R.id.tv_zongfencheng_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentDelegate().getSupportDelegate().start(new ZongfenchengListDelegate());
            }
        });
        $(R.id.tv_daifencheng_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentDelegate().getSupportDelegate().start(new DaifenchengListDelegate());
            }
        });


        WangfuDelegate parent = getParentDelegate();
        parent.getSupportDelegate().loadRootFragment(R.id.container_othersetting, new OtherSettingDelegate());
//        tv_all_coupon
//        final RecyclerView rvSettings = $(R.id.rv_personal_setting);
//        final ListBean address = new ListBean.Builder()
//                .setItemType(ListItemType.ITEM_NORMAL)
//                .setId(1)
//                .setDelegate(new AddressDelegate())
//                .setText("收货地址")
//                .build();
//
//        final ListBean system = new ListBean.Builder()
//                .setItemType(ListItemType.ITEM_NORMAL)
//                .setId(2)
//                .setDelegate(new SettingsDelegate())
//                .setText("系统设置")
//                .build();
//
//
//        final List<ListBean> data = new ArrayList<>();
//        data.add(address);
//        data.add(system);
//
//        //设置RecyclerView
//        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        rvSettings.setLayoutManager(manager);
//        final ListAdapter adapter = new ListAdapter(data);
//        rvSettings.setAdapter(adapter);
//        rvSettings.addOnItemTouchListener(new PersonalClickListener(this));
    }

    private void initAvatarAndNickname(){
        boolean isSignIn = WangfuPreference.getAppFlag(AccountManager.SignTag.SIGN_TAG.name());
        showAvatarBySignStatus(isSignIn);
    }

    private void showAvatarBySignStatus(boolean isSignIn){
        if(isSignIn){
            String headimgurl = WangfuPreference.getCustomAppProfile("headimgurl");
            String user_nickname = WangfuPreference.getCustomAppProfile("nickname");

            if (headimgurl.length() > 0) {
                ImageView avatarImageView = $(R.id.img_user_avatar);
                TextView nicknameTextView = $(R.id.user_nickname);
                nicknameTextView.setText(user_nickname);
                Glide.with(getContext())
                        .load(headimgurl)
                        .into(avatarImageView);
            }
        } else {
            ImageView avatarImageView = $(R.id.img_user_avatar);
            TextView nicknameTextView = $(R.id.user_nickname);
            nicknameTextView.setText(getString(R.string.log_out));
            Glide.with(getContext())
                    .load(R.mipmap.avatar)
                    .into(avatarImageView);
        }
    }

    @Override
    public void onSignInSuccess() {
        showAvatarBySignStatus(true);
    }

    @Override
    public void onSignUpSuccess() {

    }

    @Override
    public void onSignOutSuccess() {
        showAvatarBySignStatus(false);
        Toast.makeText(getContext(), "退出成功", Toast.LENGTH_SHORT).show();
    }
}
