package com.tobyli16.cartlibrary.cart.confirm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.tobyli16.cartlibrary.R;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.delegates.bottom.BottomItemDelegate;

/**
 * Created by tobyli
 */

public class PaySuccessDelegate extends BottomItemDelegate {

    @Override
    public Object setLayout() {
        return R.layout.delegate_pay_success;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        $(R.id.pay_success_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportDelegate().pop();
            }
        });
        $(R.id.tv_check_order_success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportDelegate().pop();
                Fragment fragment = (Fragment) ARouter.getInstance()
                        .build("/profile/order/list/")
                        .withString("currentTabPos","2")
                        .navigation();
                if (fragment != null) {
                    getSupportDelegate().startWithPop((WangfuDelegate)fragment);
                }
            }
        });


    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

    }

}
