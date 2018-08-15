package cn.bupt.wfshop.debug;

import android.graphics.Color;

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
