package com.tobyli16.profilelibrary.personal.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tobyli16.profilelibrary.R;

import cn.bupt.wfshop.delegates.WangfuDelegate;

/**
 * Created by tobyli
 */

public class NameDelegate extends WangfuDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_name;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }
}
