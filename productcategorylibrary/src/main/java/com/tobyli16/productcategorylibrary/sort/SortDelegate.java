package com.tobyli16.productcategorylibrary.sort;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.tobyli16.productcategorylibrary.R;
import com.tobyli16.productcategorylibrary.sort.content.ContentDelegate;
import com.tobyli16.productcategorylibrary.sort.list.VerticalListDelegate;

import cn.bupt.wfshop.delegates.WangfuDelegate;
import cn.bupt.wfshop.delegates.bottom.BottomItemDelegate;

/**
 * Created by tobyli
 */

public class SortDelegate extends BottomItemDelegate {
    //在父类中调用该方法，绑定自身的布局
    @Override
    public Object setLayout() {
        return R.layout.delegate_sort;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        $(R.id.sort_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //用ARouter跨模块调用，不直接使用类的实例或类(而是使用名称)，这样可以减少耦合
                Fragment fragment = (Fragment) ARouter.getInstance().build("/search/search/").withString("getSearchWord","手机").navigation();
                if (fragment != null) {
                    getParentDelegate().getSupportDelegate().start((WangfuDelegate)fragment);

                }
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        final VerticalListDelegate listDelegate = new VerticalListDelegate();
        getSupportDelegate().loadRootFragment(R.id.vertical_list_container, listDelegate);
        //设置右侧第一个分类显示，默认显示分类一
        getSupportDelegate().loadRootFragment(R.id.sort_content_container, ContentDelegate.newInstance(1));
    }
}
