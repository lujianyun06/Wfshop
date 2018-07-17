package debug;

import android.graphics.Color;

import com.tobyli16.indexlibrary.index.IndexDelegate;

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
        return builder.addItems(items).build();
    }

    @Override
    public int setIndexDelegate() {
        return 0;
    }

    @Override
    public int setClickedColor() {
        return Color.parseColor("#fff03327");
    }
}
