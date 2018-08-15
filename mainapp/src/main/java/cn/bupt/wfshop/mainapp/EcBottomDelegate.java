package cn.bupt.wfshop.mainapp;

import android.graphics.Color;

import com.tobyli16.cartlibrary.cart.ShopCartDelegate;
import com.tobyli16.indexlibrary.index.IndexDelegate;
import com.tobyli16.productcategorylibrary.sort.SortDelegate;
import com.tobyli16.profilelibrary.personal.PersonalDelegate;

import java.util.LinkedHashMap;

import cn.bupt.wfshop.delegates.bottom.BaseBottomDelegate;
import cn.bupt.wfshop.delegates.bottom.BottomItemDelegate;
import cn.bupt.wfshop.delegates.bottom.BottomTabBean;
import cn.bupt.wfshop.delegates.bottom.ItemBuilder;

/**
 * Created by tobyli
 */

public class EcBottomDelegate extends BaseBottomDelegate {

    @Override
    public LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder) {
        final LinkedHashMap<BottomTabBean, BottomItemDelegate> items = new LinkedHashMap<>();
        items.put(new BottomTabBean("{fa-home}", "主页"), new IndexDelegate());
        items.put(new BottomTabBean("{fa-sort}", "分类"), new SortDelegate());
//        items.put(new BottomTabBean("{fa-compass}", "发现"), new DiscoverDelegate());
        items.put(new BottomTabBean("{fa-shopping-cart}", "购物车"), new ShopCartDelegate());
        items.put(new BottomTabBean("{fa-user}", "我的"), new PersonalDelegate());
        return builder.addItems(items).build();
    }

    @Override
    public int initIndexDelegate() {
        return 0;
    }

    @Override
    public int setClickedColor() {
        return Color.parseColor("#fff03327");
    }
}
