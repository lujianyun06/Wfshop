package com.tobyli16.profilelibrary.personal.order;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.tobyli16.profilelibrary.R;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.net.RestClient;
import cn.bupt.wfshop.ui.widget.AutoPhotoLayout;
import cn.bupt.wfshop.ui.widget.StarLayout;
import cn.bupt.wfshop.util.callback.CallbackManager;
import cn.bupt.wfshop.util.callback.CallbackType;
import cn.bupt.wfshop.util.callback.IGlobalCallback;


/**
 * Created by tobyli
 */

@Route(path = "/profile/order/comment/")
public class OrderCommentDelegate extends WangfuDelegate {

    private StarLayout mStarLayout = null;
    private AutoPhotoLayout mAutoPhotoLayout = null;
    private AppCompatImageView mImgOrderComment = null;

    void onClickSubmit() {
        RestClient.builder()
                .url("https://wfshop.andysheng.cn/order/"+orderId+"/comment/")
                .build()
                .post();
        Toast.makeText(getContext(), "评分： " + mStarLayout.getStarCount(), Toast.LENGTH_LONG).show();
        Fragment fragment = (Fragment) ARouter.getInstance().build("/profile/order/list/").withString("currentTabPos",String.valueOf(4)).navigation();
        if (fragment != null) {
            getSupportDelegate().startWithPop((WangfuDelegate)fragment);
        }
    }

    @Autowired
    String imgUrl;

    @Autowired
    String orderId;

    @Override
    public Object setLayout() {
        return R.layout.delegate_order_comment;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        ARouter.getInstance().inject(this);

        mStarLayout = $(R.id.custom_star_layout);
        mAutoPhotoLayout = $(R.id.custom_auto_photo_layout);
        mImgOrderComment = $(R.id.img_order_comment);

        if (imgUrl!=null) {
            Glide.with(getContext()).load(imgUrl).into(mImgOrderComment);
        }

        $(R.id.top_tv_comment_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubmit();
            }
        });
        mAutoPhotoLayout.setDelegate(this);
        CallbackManager.getInstance()
                .addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                    @Override
                    public void executeCallback(@Nullable Uri args) {
                        mAutoPhotoLayout.onCropTarget(args);
                    }
                });
    }
}
