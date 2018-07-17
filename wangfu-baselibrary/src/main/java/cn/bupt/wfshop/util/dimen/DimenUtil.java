package cn.bupt.wfshop.util.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import cn.bupt.wfshop.app.Wangfu;

/**
 * Created by tobyli on 2017/4/2
 */

public final class DimenUtil {

    public static int getScreenWidth() {
        final Resources resources = Wangfu.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = Wangfu.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
