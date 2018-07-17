package cn.bupt.wfshop.delegates.bottom;

import android.widget.Toast;

import cn.bupt.wfshop.R;
import cn.bupt.wfshop.app.Wangfu;
import cn.bupt.wfshop.delegates.WangfuDelegate;

/**
 * Created by tobyli
 */

public abstract class BottomItemDelegate extends WangfuDelegate {
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(_mActivity, "双击退出" + Wangfu.getApplicationContext().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
